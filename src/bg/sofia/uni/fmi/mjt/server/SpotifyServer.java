package bg.sofia.uni.fmi.mjt.server;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.command.commands.Command;
import bg.sofia.uni.fmi.mjt.command.commands.LoginCommand;
import bg.sofia.uni.fmi.mjt.command.commands.RegisterCommand;
import bg.sofia.uni.fmi.mjt.command.factory.CommandFactory;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.database.DataBaseSnapShotter;
import bg.sofia.uni.fmi.mjt.exceptions.FailedDataBaseConnectionException;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.logger.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpotifyServer {
    private static final int BUFFER_SIZE = 1024;
    private static final String HOST = "localhost";
    private static final int PORT = 8888;
    private static final int MAX_PORT_AVAILABLE = 65000;
    private static final int SNAPSHOT_CYCLE = 10;
    private static final int INITIAL_DELAY = 2;
    private static List<Integer> freePorts = new ArrayList<>();
    private static SpotifyServer instance = null;
    private final int port;
    private boolean isServerWorking;
    private ByteBuffer buffer;
    private Selector selector;

    private SpotifyServer(int port) {
        this.port = port;
        for (int i = MAX_PORT_AVAILABLE; i >= 0; i--) {
            freePorts.add(i);
        }
    }

    public static SpotifyServer getInstance() {
        if (instance == null) {
            instance = new SpotifyServer(PORT);
        }
        return instance;
    }

    public void start() throws IOException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;
            DataBase dbConnection = DataBase.getInstance();
            service.scheduleAtFixedRate(new DataBaseSnapShotter(dbConnection), INITIAL_DELAY, SNAPSHOT_CYCLE,
                TimeUnit.MINUTES);

            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    processKey(keyIterator, dbConnection);
                } catch (IOException e) {
                    Logger.of().log("Error occurred while processing client request: ", e);
                }
            }
        } catch (IOException e) {
            Logger.of().log("failed to start server", e);
        } catch (FailedDataBaseConnectionException e) {
            Logger.of().log("Failed to start db connection", e);
        }
    }

    public void stop() throws FailedDataBaseConnectionException, IOException {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
        DataBase.getInstance().saveSnapshot();
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    private void processKey(Iterator<SelectionKey> keyIterator, DataBase dbConnection) throws IOException {
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            if (key.isReadable()) {
                SocketChannel clientChannel = (SocketChannel) key.channel();
                String clientInput = getClientInput(clientChannel);
                System.out.println(clientInput);
                if (clientInput == null) {
                    continue;
                }
                String output = executeCommand(key, dbConnection, clientInput);
                writeClientOutput(clientChannel, output);

            } else if (key.isAcceptable()) {
                accept(selector, key);
            }
            keyIterator.remove();
        }
    }

    private String executeCommand(SelectionKey key, DataBase dbConnection, String clientInput) throws IOException {
        String output;
        try {
            var attachedUser = (User) key.attachment();

            Command command = CommandFactory.createCommandFromString(attachedUser, clientInput);
            output = command.execute(dbConnection);
            attachToken(command, key);
            Logger.of().log(clientInput, key);

        } catch (UnknownCommandException e) {
            output =
                "Enter valid command, the last entered command: " + clientInput + "was invalid" +
                    System.lineSeparator();
            Logger.of().log(clientInput, e, key);
        } catch (InvalidArgumentsCommandException e) {
            output =
                "Enter valid arguments for the last entered command: " + clientInput +
                    System.lineSeparator();
            Logger.of().log(clientInput, e, key);
        }
        return output;
    }

    private void attachToken(Command command, SelectionKey key) {
        if (command instanceof LoginCommand login) {
            User user = login.getUserToken();
            key.attach(user);
        }
        if (command instanceof RegisterCommand register) {
            User user = register.getUserToken();
            key.attach(user);
        }
    }

    public synchronized int getFreePort() {
        int freePort = freePorts.size() - 1;
        freePorts.remove(freePorts.size() - 1);
        return freePort;
    }

    public synchronized void addFreePort(int port) {
        freePorts.add(port);
    }
}
package bg.sofia.uni.fmi.mjt.command.factory;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.command.commands.AddSongCommand;
import bg.sofia.uni.fmi.mjt.command.commands.Command;
import bg.sofia.uni.fmi.mjt.command.commands.CreatePlaylistCommand;
import bg.sofia.uni.fmi.mjt.command.commands.LoginCommand;
import bg.sofia.uni.fmi.mjt.command.commands.PLaySongCommand;
import bg.sofia.uni.fmi.mjt.command.commands.RegisterCommand;
import bg.sofia.uni.fmi.mjt.command.commands.SearchCommand;
import bg.sofia.uni.fmi.mjt.command.commands.ShowPlaylistCommand;
import bg.sofia.uni.fmi.mjt.command.commands.TopSongsCommand;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.exceptions.UnknownCommandException;

import java.util.List;

public class CommandFactory {

    private static final int COMMAND_INDEX = 0;
    private static final int BEGIN_ARGUMENT_INDEX = 1;

    public static Command createCommandFromString(User user, String commandLine)
        throws UnknownCommandException, InvalidArgumentsCommandException {
        List<String> tokens = convertCommandToTokens(commandLine);
        CommandType command = getCommandFromString(tokens.get(COMMAND_INDEX));
        List<String> arguments = tokens.subList(BEGIN_ARGUMENT_INDEX, tokens.size());
        return createCommand(user, command, arguments);
    }

    private static List<String> convertCommandToTokens(String commandLine) {
        String toProcess = commandLine.replaceAll("\\s{2,}", " ");
        return List.of(toProcess.split(" "));
    }

    private static CommandType getCommandFromString(String command) throws UnknownCommandException {
        CommandType commandType = CommandType.valueOfRepresentation(command);
        if (commandType == null) {
            throw new UnknownCommandException("Wrong type of command, this command is not supported");
        }
        return commandType;
    }

    private static Command createCommand(User user, CommandType commandType, List<String> arguments) throws
        InvalidArgumentsCommandException {
        return switch (commandType) {
            case ADD_SONG -> new AddSongCommand(user, arguments);
            case REGISTER -> new RegisterCommand(user, arguments);
            case LOGIN -> new LoginCommand(user, arguments);
            case SEARCH -> new SearchCommand(user, arguments);
            case TOP -> new TopSongsCommand(user, arguments);
            case CREATE_PLAYLIST -> new CreatePlaylistCommand(user, arguments);
            case SHOW_PLAYLIST -> new ShowPlaylistCommand(user, arguments);
            case PLAY -> new PLaySongCommand(user, arguments);
        };
    }
}

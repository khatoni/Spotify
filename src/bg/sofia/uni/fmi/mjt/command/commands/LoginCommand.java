package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.Loginable;
import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.FailedLoginException;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;

import java.util.List;

public class LoginCommand implements Command {
    private final String emailAddress;
    private final String password;
    private User attachedUser;
    private static final int EMAIL_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;

    public LoginCommand(User user, List<String> arguments) throws InvalidArgumentsCommandException {
        if (arguments.isEmpty()) {
            throw new InvalidArgumentsCommandException("Attempt to create Login command without arguments");
        }
        if (arguments.get(EMAIL_INDEX) == null || arguments.get(PASSWORD_INDEX) == null) {
            throw new InvalidArgumentsCommandException("Attempt to create Login command with null email or password");
        }

        this.emailAddress = arguments.get(EMAIL_INDEX);
        this.password = arguments.get(PASSWORD_INDEX);
        this.attachedUser = user;
    }

    @Override
    public String execute(DataBase dbConnection) {
        if (attachedUser != null) {
            return ServerResponse.SECOND_AUTHORIZATION;
        }
        Loginable accessController = dbConnection.getAuthenticator();
        try {
            boolean isSuccessful = accessController.authenticate(new User(emailAddress, password));
            if (isSuccessful) {
                attachedUser = new User(emailAddress, password);
                return ServerResponse.SUCCESSFUL_LOGIN;
            } else {
                return ServerResponse.WRONG_AUTHENTICATION;
            }

        } catch (FailedLoginException e) {
            return ServerResponse.FAILED_OPERATION;
        }
    }

    public User getUserToken() {
        return attachedUser;
    }
}

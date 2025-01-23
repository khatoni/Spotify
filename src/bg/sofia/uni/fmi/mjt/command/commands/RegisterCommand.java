package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.Registrable;
import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.FailedRegistrationException;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.exceptions.UserAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;

import java.util.List;

public class RegisterCommand implements Command {

    private final String emailAddress;
    private final String password;
    private User attachedUser;
    private static final int EMAIL_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;

    public RegisterCommand(User user, List<String> arguments) throws InvalidArgumentsCommandException {
        if (arguments.isEmpty()) {
            throw new InvalidArgumentsCommandException("Attempt to create Register command without arguments");
        }
        if (arguments.get(EMAIL_INDEX) == null || arguments.get(PASSWORD_INDEX) == null) {
            throw new InvalidArgumentsCommandException(
                "Attempt to create Register command with null email or password");
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

        Registrable accessController = dbConnection.getRegistrator();
        try {
            User user = new User(emailAddress, password);
            accessController.registerAccount(user);
            attachedUser = user;
            return ServerResponse.SUCCESSFUL_REGISTRATION;

        } catch (UserAlreadyExistsException e) {
            return ServerResponse.DUPLICATE_USER;
        } catch (FailedRegistrationException e) {
            return ServerResponse.FAILED_OPERATION;
        }
    }

    public User getUserToken() {
        return attachedUser;
    }

}

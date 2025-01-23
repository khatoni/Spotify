package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.Registrable;
import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.command.commands.Command;
import bg.sofia.uni.fmi.mjt.command.commands.RegisterCommand;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.FailedRegistrationException;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.exceptions.UserAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterCommandTest {

    @Test
    void testCreateCommandInvalidArguments() {
        assertThrows(InvalidArgumentsCommandException.class, () -> new RegisterCommand(null, new ArrayList<>()),
            "InvalidArgumentsCommandException was expected, but was not thrown");
    }

    @Test
    void testExecuteRegisterUserIsAlreadyAuthenticated() throws InvalidArgumentsCommandException {
        Command command = new RegisterCommand(new User("test"), List.of("test1", "test2"));
        DataBase dataBase = mock();
        String response = command.execute(dataBase);
        assertEquals(ServerResponse.SECOND_AUTHORIZATION, response,
            "The expected response is that user is not authenticated");
    }

    @Test
    void testExecuteRegistrationUserAlreadyExists()
        throws InvalidArgumentsCommandException, FailedRegistrationException, UserAlreadyExistsException {
        DataBase dataBase = mock();
        Registrable registator = mock();
        when(registator.registerAccount(any())).thenThrow(new UserAlreadyExistsException("user already exists"));
        when(dataBase.getRegistrator()).thenReturn(registator);
        Command command = new RegisterCommand(null, List.of("antonio", "antonio"));

        assertEquals(ServerResponse.DUPLICATE_USER,
            command.execute(dataBase),
            "The actual response is different the expected");
    }

    @Test
    void testExecuteRegistrationFailedOperation()
        throws InvalidArgumentsCommandException, FailedRegistrationException, UserAlreadyExistsException {
        DataBase dataBase = mock();
        Registrable registator = mock();
        when(registator.registerAccount(any())).thenThrow(new FailedRegistrationException("internal problem"));
        when(dataBase.getRegistrator()).thenReturn(registator);
        Command command = new RegisterCommand(null, List.of("antonio", "antonio"));

        assertEquals(ServerResponse.FAILED_OPERATION,
            command.execute(dataBase),
            "The actual response is different the expected");
    }

    @Test
    void testExecuteRegistrationSuccessfully()
        throws FailedRegistrationException, UserAlreadyExistsException, InvalidArgumentsCommandException {
        DataBase dataBase = mock();
        Registrable registator = mock();
        when(registator.registerAccount(any())).thenReturn(new User("antonio"));
        when(dataBase.getRegistrator()).thenReturn(registator);
        Command command = new RegisterCommand(null, List.of("antonio", "antonio"));

        assertEquals(ServerResponse.SUCCESSFUL_REGISTRATION,
            command.execute(dataBase),
            "The actual response is different the expected");
    }

}

package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.Loginable;
import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.command.commands.Command;
import bg.sofia.uni.fmi.mjt.command.commands.LoginCommand;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.FailedLoginException;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginCommandTest {

    @Test
    void testCreateCommandInvalidArguments() {
        assertThrows(InvalidArgumentsCommandException.class, () -> new LoginCommand(null, new ArrayList<>()),
            "InvalidArgumentsCommandException was expected, but was not thrown");
    }

    @Test
    void testExecuteLoginUserIsAlreadyAuthenticated() throws InvalidArgumentsCommandException {
        Command command = new LoginCommand(new User("test"), List.of("test1", "test2"));
        DataBase dataBase = mock();
        String response = command.execute(dataBase);
        assertEquals(ServerResponse.SECOND_AUTHORIZATION, response,
            "The expected response is that user is not authenticated");
    }

    @Test
    void testExecuteLoginFailedOperation()
        throws InvalidArgumentsCommandException,
        FailedLoginException {
        DataBase dataBase = mock();
        Loginable authenticator = mock();
        when(authenticator.authenticate(any())).thenThrow(new FailedLoginException("internal problem"));
        when(dataBase.getAuthenticator()).thenReturn(authenticator);
        Command command = new LoginCommand(null, List.of("antonio", "antonio"));

        assertEquals(ServerResponse.FAILED_OPERATION,
            command.execute(dataBase),
            "The actual response is different the expected");
    }

    @Test
    void testExecuteLoginWrongAuthentication() throws FailedLoginException, InvalidArgumentsCommandException {
        DataBase dataBase = mock();
        Loginable authenticator = mock();
        when(authenticator.authenticate(any())).thenReturn(false);
        when(dataBase.getAuthenticator()).thenReturn(authenticator);
        Command command = new LoginCommand(null, List.of("antonio", "antonio"));

        assertEquals(ServerResponse.WRONG_AUTHENTICATION,
            command.execute(dataBase),
            "The actual response is different the expected");
    }

    @Test
    void testExecuteLoginSuccessful() throws FailedLoginException, InvalidArgumentsCommandException {
        DataBase dataBase = mock();
        Loginable authenticator = mock();
        when(authenticator.authenticate(any())).thenReturn(true);
        when(dataBase.getAuthenticator()).thenReturn(authenticator);
        Command command = new LoginCommand(null, List.of("antonio", "antonio"));

        assertEquals(ServerResponse.SUCCESSFUL_LOGIN,
            command.execute(dataBase),
            "The actual response is different the expected");
    }

}



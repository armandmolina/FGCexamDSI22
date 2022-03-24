package cat.tecnocampus.usersservice.application.appUseCases.exception;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String username) {
        super("User " + username + " doesn't exist");
    }
}

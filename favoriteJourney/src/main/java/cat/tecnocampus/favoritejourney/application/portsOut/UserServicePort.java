package cat.tecnocampus.favoritejourney.application.portsOut;

public interface UserServicePort {
    String userExists(String username, int delay, int faultRatio);
}

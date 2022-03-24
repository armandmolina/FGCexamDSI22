package cat.tecnocampus.usersservice.application.portsIn;

import cat.tecnocampus.usersservice.domain.Friends;
import cat.tecnocampus.usersservice.domain.User;

import java.util.List;

public interface UserPortsIn {
    List<User> getUsers();

    User getUser(String username);

    Friends getFriends(String username);

    void addFriends(String username, String friend);

    boolean userExists(String username, int delay, int faultRatio);

    void deleteUser(String username);
}

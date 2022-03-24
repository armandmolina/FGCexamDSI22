package cat.tecnocampus.usersservice.application.portsOut;

import cat.tecnocampus.usersservice.domain.Friends;

import java.util.List;

public interface FriendPortOut {
    Friends getFriends(String username);

    List<Friends> getFriends();

    void addFriends(String username, String friend);

    void deleteFriends(String username);
}

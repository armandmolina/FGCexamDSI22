package cat.tecnocampus.usersservice.domain;

import cat.tecnocampus.usersservice.domain.exceptions.RepeatedFriend;

import java.util.ArrayList;
import java.util.List;

public class Friends {
    private List<String> friends;
    private String username;

    public Friends() {
        friends = new ArrayList<>();
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addFriend(String friend) {
        if (friends.contains(friend)) {
            throw new RepeatedFriend();
        }
        friends.add(friend);
    }
}

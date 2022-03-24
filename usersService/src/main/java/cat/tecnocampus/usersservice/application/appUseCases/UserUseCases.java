package cat.tecnocampus.usersservice.application.appUseCases;

import cat.tecnocampus.usersservice.application.appUseCases.exception.UserServiceBadLuchkException;
import cat.tecnocampus.usersservice.application.portsIn.UserPortsIn;
import cat.tecnocampus.usersservice.application.portsOut.FriendPortOut;
import cat.tecnocampus.usersservice.application.portsOut.UserPortOut;
import cat.tecnocampus.usersservice.domain.Friends;
import cat.tecnocampus.usersservice.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class UserUseCases implements UserPortsIn {
    private UserPortOut userPortOut;
    private FriendPortOut friendPortOut;

    public UserUseCases(UserPortOut userPortOut, FriendPortOut friendPortOut) {
        this.userPortOut = userPortOut;
        this.friendPortOut = friendPortOut;
    }

    @Override
    public List<User> getUsers() {
        return userPortOut.getUsers();
    }

    @Override
    public User getUser(String username) {
        return  userPortOut.findByUsername(username);
    }

    @Override
    public Friends getFriends(String username) {
        return  friendPortOut.getFriends(username);
    }

    @Override
    public void addFriends(String username, String friend) {
        User user = userPortOut.findByUsername(username);
        Friends friends = friendPortOut.getFriends(username);
        friends.addFriend(friend);

        friendPortOut.addFriends(username, friend);
    }

    @Override
    public boolean userExists(String username, int delay, int faultRatio) {
        if(delay > 0) simulateDelay(delay);
        if(faultRatio > 0) throwErrorIfBadLuck(faultRatio);
        return userPortOut.existsUser(username);
    }

    // TODO 3.1 Quan un usuari s'esborra s'ha d'enviar un missatge amb el ussername per indicar-ho. Recorda de seguir
    //  l'arquitectura hexagonal
    @Override
    @Transactional
    public void deleteUser(String username) {
        friendPortOut.deleteFriends(username);
        userPortOut.deleteUser(username);
    }

    private void simulateDelay(int delay) {
        System.out.println("Sleeping for " + delay + " seconds...");
        try {Thread.sleep(delay * 1000);} catch (InterruptedException e) {}
        System.out.println("Moving on...");
    }

    private void throwErrorIfBadLuck(int faultRatio) {
        int randomThreshold = getRandomNumber(1, 100);
        if (faultRatio < randomThreshold) {
            System.out.println("We got lucky, no error occurred ...");
        } else {
            System.out.println("Bad luck, an error occurred... " + faultRatio);
            throw new UserServiceBadLuchkException("Bad luck: User service not going to respond...");
        }
    }

    private final Random randomNumberGenerator = new Random();
    private int getRandomNumber(int min, int max) {
        if (max < min) {
            throw new RuntimeException("Max must be greater than min");
        }
        return randomNumberGenerator.nextInt((max - min) + 1) + min;
    }
}

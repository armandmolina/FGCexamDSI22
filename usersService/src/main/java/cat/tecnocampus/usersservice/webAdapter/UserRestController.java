package cat.tecnocampus.usersservice.webAdapter;


import cat.tecnocampus.usersservice.application.appUseCases.UserUseCases;
import cat.tecnocampus.usersservice.domain.Friends;
import cat.tecnocampus.usersservice.domain.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserRestController  {
    private UserUseCases userUseCases;

    public UserRestController(UserUseCases userUseCases) {
        this.userUseCases = userUseCases;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userUseCases.getUsers();
    }

    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) {
        return userUseCases.getUser(username);
    }

    @GetMapping("/users/{username}/friends")
    public Friends getAllFriends(@PathVariable String username) {
        return userUseCases.getFriends(username);
    }

    @GetMapping(value = "/users/exists/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean existsUser(@PathVariable String username,
                              @RequestParam(value = "delay", required = false, defaultValue = "0") int delay,
                              @RequestParam(value = "faultRatio", required = false, defaultValue = "0") int faultRatio) {
        return userUseCases.userExists(username, delay, faultRatio);
    }

    @PostMapping("/users/{username}/friends/{friend}")
    public void saveFriends(@PathVariable String username, @PathVariable String friend) {
        userUseCases.addFriends(username, friend);
    }

    @DeleteMapping("users/{username}")
    public void deleteUser(@PathVariable String username) {
        userUseCases.deleteUser(username);
    }
}

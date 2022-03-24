package cat.tecnocampus.usersservice.webAdapter;


import cat.tecnocampus.usersservice.application.appController.UserController;
import cat.tecnocampus.usersservice.domain.Friends;
import cat.tecnocampus.usersservice.domain.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserRestController  {
    private UserController userController;

    public UserRestController(UserController userController) {
        this.userController = userController;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userController.getUsers();
    }

    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) {
        return userController.getUser(username);
    }

    @GetMapping("/users/{username}/friends")
    public Friends getAllFriends(@PathVariable String username) {
        return userController.getFriends(username);
    }

    @GetMapping(value = "/users/exists/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean existsUser(@PathVariable String username,
                              @RequestParam(value = "delay", required = false, defaultValue = "0") int delay,
                              @RequestParam(value = "faultRatio", required = false, defaultValue = "0") int faultRatio) {
        return userController.userExists(username, delay, faultRatio);
    }

    @PostMapping("/users/{username}/friends/{friend}")
    public void saveFriends(@PathVariable String username, @PathVariable String friend) {
        userController.addFriends(username, friend);
    }

    @DeleteMapping("users/{username}")
    public void deleteUser(@PathVariable String username) {
        userController.deleteUser(username);
    }
}

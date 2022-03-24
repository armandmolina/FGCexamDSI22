package cat.tecnocampus.usersservice.application.portsOut;

import cat.tecnocampus.usersservice.domain.User;

import java.util.List;

public interface UserPortOut {
    User findByUsername(String username);

    List<User> getUsers();

    boolean existsUser(String username);

    void deleteUser(String username);
}

package cat.tecnocampus.usersservice.persintenceAdapter;


import cat.tecnocampus.usersservice.application.appUseCases.exception.UserDoesNotExistException;
import cat.tecnocampus.usersservice.application.portsOut.UserPortOut;
import cat.tecnocampus.usersservice.domain.User;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.simpleflatmapper.jdbc.spring.RowMapperImpl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO implements UserPortOut {

    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapperImpl<User> userRowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("username")
                    .newRowMapper(User.class);

    ResultSetExtractorImpl<User> usersRowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("username")
                    .newResultSetExtractor(User.class);

    @Override
    public User findByUsername(String username) {
        final String query = "select username, name, second_name, email from user where username = ?";

        try {
            return jdbcTemplate.queryForObject(query, userRowMapper, username);
        }
        catch (EmptyResultDataAccessException e) {
            throw new UserDoesNotExistException(username);
        }
    }

    @Override
    public List<User> getUsers() {
        final String query = "SELECT username, name, second_name, email FROM USER";

        return jdbcTemplate.query(query, usersRowMapper);
    }

    @Override
    public boolean existsUser(String username) {
        final String query = "SELECT count(*) FROM user WHERE username = ?";

        try {
            Integer cnt = jdbcTemplate.queryForObject(query, Integer.class, username);
            return cnt > 0;
        }
        catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public void deleteUser(String username) {
        final String query = "delete from user where username = ?";
        jdbcTemplate.update(query, username);
    }
}

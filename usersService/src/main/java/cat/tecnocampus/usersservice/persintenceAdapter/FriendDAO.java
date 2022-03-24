package cat.tecnocampus.usersservice.persintenceAdapter;

import cat.tecnocampus.usersservice.application.appController.exception.UserDoesNotExistException;
import cat.tecnocampus.usersservice.domain.Friends;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FriendDAO implements cat.tecnocampus.usersservice.application.portsOut.FriendPortOut {
    private JdbcTemplate jdbcTemplate;

    ResultSetExtractorImpl<Friends> friendsRowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("username", "friend")
                    .newResultSetExtractor(Friends.class);

    public FriendDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Friends getFriends(String username) {
        List<Friends> result;
        final String query = "select * from friend where username = ?";

        result = jdbcTemplate.query(query, friendsRowMapper, username);
        try {
            return result.get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new UserDoesNotExistException(username);
        }
    }

    @Override
    public List<Friends> getFriends() {
        final String query = "select * from friend";
        return jdbcTemplate.query(query, friendsRowMapper);
    }

    @Override
    public void addFriends(String username, String friend) {
        final String query = "insert into friend(username, friend) values(?, ?)";

        jdbcTemplate.update(query, username, friend);
    }

    @Override
    public void deleteFriends(String username) {
        final String query = "delete from friend where username = ?";

        jdbcTemplate.update(query, username);
    }
}

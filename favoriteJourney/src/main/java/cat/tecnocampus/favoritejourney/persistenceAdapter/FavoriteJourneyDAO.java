package cat.tecnocampus.favoritejourney.persistenceAdapter;

import cat.tecnocampus.favoritejourney.application.portsOut.FavoriteJourneyPort;
import cat.tecnocampus.favoritejourney.domain.DayTimeStart;
import cat.tecnocampus.favoritejourney.domain.FavoriteJourney;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FavoriteJourneyDAO implements FavoriteJourneyPort {
    JdbcTemplate jdbcTemplate;
    JourneyDAO journeyDAO;

    public FavoriteJourneyDAO(JdbcTemplate jdbcTemplate, JourneyDAO journeyDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.journeyDAO = journeyDAO;
    }

    @Override
    public void saveFavoriteJourney (FavoriteJourney favoriteJourney, String username, boolean checked) {
        jdbcTemplate.update("INSERT INTO favorite_journey(favorite_journey_id, journey, user_id, checked) VALUES (?, ?, ?, ?)",
                favoriteJourney.getId(), favoriteJourney.getJourney().getId(), username, checked);

        saveDayTimeStart(favoriteJourney.getStartList(), favoriteJourney.getId());
    }

    @Override
    public List<FavoriteJourney> findFavoriteJourneys (String username) {
        return jdbcTemplate.query("select * from favorite_journey where user_id = ?", new FavoriteJourneyMapper(), username);
    }

    @Override
    public void deleteFavoriteJourney(String username) {
        List<FavoriteJourney> favoriteJourneys = findFavoriteJourneys(username);
        deleteAllDayTimeStart(favoriteJourneys);
        final String query = "delete from favorite_journey where user_id = ?";
        jdbcTemplate.update(query, username);
        deleteJourneys(favoriteJourneys);
    }

    private void deleteJourneys(List<FavoriteJourney> favoriteJourneys) {
        List<String> journeyIds = favoriteJourneys.stream()
                .map(fj -> fj.getJourney().getId())
                .collect(Collectors.toList());
        journeyDAO.deleteJourneys(journeyIds);
    }

    private void deleteAllDayTimeStart(List<FavoriteJourney> favoriteJourneys) {
        final String query = "delete from day_time_start where daytime_id = ?";
        List<String> startIds = favoriteJourneys.stream()
                .flatMap(fj -> fj.getStartList().stream())
                .map(DayTimeStart::getId)
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(query,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1,startIds.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return startIds.size();
                    }
                });
    }

    private int[] saveDayTimeStart(List<DayTimeStart> start, String favoriteJourneyId) {
        return jdbcTemplate.batchUpdate("INSERT INTO day_time_start(daytime_id, timeStart, day_of_week, favorite_journey_id) values(?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        DayTimeStart dayTimeStart = start.get(i);
                        preparedStatement.setString(1, dayTimeStart.getId());
                        preparedStatement.setString(2, dayTimeStart.getTimeStart());
                        preparedStatement.setString(3, dayTimeStart.getDayOfWeek());
                        preparedStatement.setString(4, favoriteJourneyId);
                    }

                    @Override
                    public int getBatchSize() {
                        return start.size();
                    }
        });
    }

    private List<DayTimeStart> findDayTimeStart(String favoriteJourneyId) {
        return jdbcTemplate.query("select * from day_time_start where favorite_journey_id = ?", new DayTimeStartMapper(), favoriteJourneyId);
    }

    public final class FavoriteJourneyMapper implements RowMapper<FavoriteJourney> {
        @Override
        public FavoriteJourney mapRow(ResultSet resultSet, int i) throws SQLException {
            FavoriteJourney favoriteJourney = new FavoriteJourney();

            favoriteJourney.setId(resultSet.getString("favorite_journey_id"));
            favoriteJourney.setJourney(journeyDAO.findJourney(resultSet.getString("journey")));
            favoriteJourney.setDateTimeStarts(findDayTimeStart(favoriteJourney.getId()));
            favoriteJourney.setChecked(resultSet.getBoolean("checked"));
            favoriteJourney.setUsername(resultSet.getString("user_id"));
            return favoriteJourney;
        }
    }

    public final class DayTimeStartMapper implements RowMapper<DayTimeStart> {
        @Override
        public DayTimeStart mapRow(ResultSet resultSet, int i) throws SQLException {
            DayTimeStart dayTimeStart = new DayTimeStart();

            dayTimeStart.setId(resultSet.getString("daytime_id"));
            dayTimeStart.setTimeStart(resultSet.getString("timeStart"));
            dayTimeStart.setDayOfWeek(resultSet.getString("day_of_week"));

            return dayTimeStart;
        }
    }
}

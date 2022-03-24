package cat.tecnocampus.favoritejourney.persistenceAdapter;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cat.tecnocampus.favoritejourney.application.portsOut.*;
import cat.tecnocampus.favoritejourney.domain.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JourneyDAO implements JourneyPort {
    JdbcTemplate jdbcTemplate;
    StationPort stationPort;

    public JourneyDAO(JdbcTemplate jdbcTemplate, StationPort stationPort) {
        this.jdbcTemplate = jdbcTemplate;
        this.stationPort = stationPort;
    }

    @Override
    public int saveJourney(Journey journey) {
        final String query = "INSERT INTO journey(journey_id, origin, destination) VALUES (?, ?, ?)";

        return jdbcTemplate.update(query, journey.getId(), journey.getOrigin().getNom(), journey.getDestination().getNom());
    }

    @Override
    public String getJourneyId(Journey journey) {
        final String query = "SELECT journey_id FROM journey WHERE origin = ? AND destination = ?";

        try {
            String id = jdbcTemplate.queryForObject(query, String.class, journey.getOrigin().getNom(),
                    journey.getDestination().getNom());
            return id;
        }
        catch (EmptyResultDataAccessException e) {
            return "-1";
        }
    }

    @Override
    public Journey findJourney(String journeyId) {
        final String query = "select * from journey where journey_id = ?";
        return jdbcTemplate.queryForObject(query, new JourneyMapper(), journeyId);
    }

    @Override
    public void deleteJourneys(List<String> journeyIds) {
        final String query = "delete from journey where journey_id = ?";
        jdbcTemplate.batchUpdate(query,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, journeyIds.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return journeyIds.size();
                    }
                });

    }

    public final class JourneyMapper implements RowMapper<Journey> {
        @Override
        public Journey mapRow(ResultSet resultSet, int i) throws SQLException {
            Journey journey = new Journey(stationPort.findByName(resultSet.getString("origin")),
                    stationPort.findByName(resultSet.getString("destination")),
                    resultSet.getString("journey_id"));

            return journey;
        }
    }
}

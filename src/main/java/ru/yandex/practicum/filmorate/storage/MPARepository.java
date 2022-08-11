package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MPARepository implements IMPARepository {

    private final JdbcTemplate jdbcTemplate;

    public MPARepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<MPA> read(int id) {
        try {
            String sqlQuery = "SELECT * FROM MPA WHERE MPA_ID = ?";
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMPA, id));
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MPA> readAll() {

        String sqlQuery = "SELECT * FROM MPA";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPA);
    }

    private MPA mapRowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("MPA_NAME"))
                .build();
    }
}

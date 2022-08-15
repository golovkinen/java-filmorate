package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements UserStorage{

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {

        String sqlQuery = "insert into USERS (USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }


        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public List<User> readAll() {

        String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, UserRepository::mapRowToUser);
    }

    @Override
    public Optional<User> read(int id) {

        try {
            String sqlQuery = "select * from USERS WHERE USER_ID = ?";
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, UserRepository::mapRowToUser, id));
        }
        catch (EmptyResultDataAccessException e) {

            return Optional.empty();
        }
    }

    @Override
    public boolean update(User user) {

        if (user.getId() != null || read(user.getId()).isEmpty()) {

            String sqlQuery = "update USERS set " +
                    "USER_NAME = ?, USER_EMAIL = ?, USER_LOGIN = ?, " +
                    "BIRTHDAY = ? where USER_ID = ?";

            return jdbcTemplate.update(sqlQuery
                    , user.getName()
                    , user.getEmail()
                    , user.getLogin()
                    , Date.valueOf(user.getBirthday())
                    , user.getId()) > 0;
        }

        return false;
    }

    @Override
    public boolean delete(int id) {

        String sqlQuery = "DELETE FROM USERS where USER_ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.execute("DROP TABLE USERS CASCADE");
    }

    public static User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("USER_ID"))
                .name(resultSet.getString("USER_NAME"))
                .email(resultSet.getString("USER_EMAIL"))
                .login(resultSet.getString("USER_LOGIN"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}

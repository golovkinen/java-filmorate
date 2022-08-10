package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserRepository;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class DBUserService implements IUserService{

    private final UserStorage userStorage;

    private final JdbcTemplate jdbcTemplate;

    public DBUserService(UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public boolean addFriend(int userId, int friendId) {

        if (userStorage.read(userId) == null || userStorage.read(friendId) == null) {
            return false;
        }

            String sqlQuery = "MERGE INTO USER_FRIENDS (USER_ID, FRIEND_ID) VALUES (?,?)";

            return jdbcTemplate.update(sqlQuery
                    , userId, friendId) > 0;
    }

    @Override
    public List<User> readAllFriends(int userId) {

        if (userStorage.read(userId) == null) {
            return null;
        }

       String sqlQuery = "SELECT u.* FROM USERS u, USER_FRIENDS uf WHERE u.USER_ID = uf.FRIEND_ID AND uf.USER_ID = ?";


        return jdbcTemplate.query(sqlQuery, UserRepository::mapRowToUser, userId);
    }

    @Override
    public boolean deleteFriend(int userId, int friendId) {

        if (userStorage.read(userId) == null || userStorage.read(friendId) == null) {
            return false;
        }

        String sqlQuery = "DELETE FROM USER_FRIENDS where USER_ID = ? AND FRIEND_ID = ?";

        return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
    }

    @Override
    public List<User> findCommonFriends(int userId, int otherId) {

        if (userStorage.read(userId) == null || userStorage.read(otherId) == null) {
            return null;
        }

        String sqlQuery = "SELECT U.* FROM USERS U, USER_FRIENDS UF1, USER_FRIENDS UF2 " +
                "WHERE UF1.USER_ID = ? AND UF2.USER_ID = ? " +
                "AND U.USER_ID=UF1.FRIEND_ID AND U.USER_ID=UF2.FRIEND_ID";

        return jdbcTemplate.query(sqlQuery, UserRepository::mapRowToUser, userId, otherId);

    }
}

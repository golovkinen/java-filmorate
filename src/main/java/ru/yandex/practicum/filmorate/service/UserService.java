package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService implements UserServiceInterface {

    @Autowired
    UserStorage userStorage;

    @Override
    public boolean addFriend (int userId, int friendId) {
        if (userStorage.read(userId) == null || userStorage.read(friendId) == null) {
            return false;
        }
        return userStorage.read(userId).getFriends().add(friendId) &&
        userStorage.read(friendId).getFriends().add(userId);

    }

    @Override
    public List<User> readAllFriends (int userId) {

        if (userStorage.read(userId) == null) {
            return null;
        }

      return userStorage.read(userId).getFriends().stream()
                .map(id -> userStorage.read(id))
                .collect(Collectors.toList());

    }

    @Override
    public boolean deleteFriend (int userId, int friendId) {

        if (userStorage.read(userId) == null || userStorage.read(friendId) == null) {
            return false;
        }

        return userStorage.read(userId).getFriends().remove(friendId) &&
                userStorage.read(friendId).getFriends().remove(userId);
    }

    @Override
    public List<User> findCommonFriends(int userId, int otherId) {

        List<Integer> commonFriends = userStorage.read(userId).getFriends().stream()
                .filter(element -> userStorage.read(otherId).getFriends().contains(element))
                .collect(Collectors.toList());

        return commonFriends.stream()
                .map(id -> userStorage.read(id))
                .collect(Collectors.toList());

    }


}

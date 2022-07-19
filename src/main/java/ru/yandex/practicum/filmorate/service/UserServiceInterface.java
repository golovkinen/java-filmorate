package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserServiceInterface {
    boolean addFriend (int userId, int friendId);
    List<User> readAllFriends (int userId);
    boolean deleteFriend (int userId, int friendId);
    List<User> findCommonFriends(int userId, int friendId);
}

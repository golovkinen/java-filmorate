package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create (User user);
    List<User> readAll ();
    User read (int id);

    boolean update(User user);

    boolean delete(int id);

    void deleteAll();
}

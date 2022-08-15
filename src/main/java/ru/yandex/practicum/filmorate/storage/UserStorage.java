package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create (User user);
    List<User> readAll ();
    Optional<User> read (int id);

    boolean update(User user);

    boolean delete(int id);

    void deleteAll();
}

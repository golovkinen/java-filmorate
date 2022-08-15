package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film create (Film film);
    List<Film> readAll ();
    Optional<Film> read (int id);
    boolean update (Film film);
    boolean delete(int id);
    void deleteAll();
}

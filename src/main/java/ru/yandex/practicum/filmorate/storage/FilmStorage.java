package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create (Film film);
    List<Film> readAll ();
    Film read (int id);
    boolean update (Film film);
    boolean delete(int id);
    void deleteAll();
}

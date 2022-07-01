package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceInterface {

    Film create (Film film);
    List<Film> readAll ();
    Film read (int id);
    boolean update (Film film);
    boolean delete(int id);
}

package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface IFilmService {

    boolean addLike(int userId, int filmId);
    List<Film> readTenBestFilms (int count);
    boolean deleteLike (int userId, int filmId);
}

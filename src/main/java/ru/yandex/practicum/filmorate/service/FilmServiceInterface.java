package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmServiceInterface {

    boolean addLike(int userId, int filmId);
    List<Film> readTenBestFilms (int count);
    boolean deleteLike (int userId, int filmId);
}

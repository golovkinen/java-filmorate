package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FilmService implements FilmServiceInterface {

    @Autowired
    public FilmStorage filmStorage;

    @Autowired
    public UserStorage userStorage;


    @Override
    public boolean addLike(int userId, int filmId) {

        if (userStorage.read(userId) == null || filmStorage.read(filmId) == null) {
            return false;
        }

        return filmStorage.read(filmId).getLikes().add(userId);
    }

    @Override
    public List<Film> readTenBestFilms(int count) {

       return filmStorage.readAll().stream()
               .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
               .limit(count)
               .collect(Collectors.toList());
    }

    @Override
    public boolean deleteLike(int userId, int filmId) {

        if (userStorage.read(userId) == null || filmStorage.read(filmId) == null) {
            return false;
        }

        return filmStorage.read(filmId).getLikes().remove(userId);
    }

}

package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IGenreRepository {

        List<Genre> readAll();
        Optional<Genre> read (int id);
        void setFilmGenres(Film film);

        List<Genre> loadFilmGenres (int id);

        boolean deleteFilmGenres(int filmId);

        void updateFilmGenres(Film film);
}

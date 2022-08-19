package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IDirectorRepository {

    Director create(Director director);

    boolean update(Director director);

    List<Director> readAll();
    Optional<Director> read (int id);

    boolean delete(int id);

    boolean setFilmDirectors(Film film);

    Set<Director> loadFilmDirectors (int id);

    boolean deleteFilmDirectors(int filmId);

}

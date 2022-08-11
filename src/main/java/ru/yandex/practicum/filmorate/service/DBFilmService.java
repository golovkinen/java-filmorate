package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmRepository;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class DBFilmService implements IFilmService{

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final JdbcTemplate jdbcTemplate;

    public DBFilmService(FilmStorage filmStorage, UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addLike(int filmId, int userId) {

        if (userStorage.read(userId).isEmpty() || filmStorage.read(filmId).isEmpty()) {
            return false;
        }

       String sqlQuery = "MERGE INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?,?)";


        return jdbcTemplate.update(sqlQuery
                , filmId, userId) > 0;
    }

    @Override
    public List<Film> readTenBestFilms(int count) {

        String sqlQuery = "SELECT f.* FROM FILMS f LEFT JOIN FILM_LIKES fl ON f.FILM_ID = fl.FILM_ID GROUP BY f.FILM_ID ORDER BY COUNT(fl.FILM_ID) DESC LIMIT ?";

        List<Film> popularFilms = jdbcTemplate.query(sqlQuery, FilmRepository::mapRowToFilm, count);

        if (popularFilms.isEmpty()) {
            String sqlQuery2 = "SELECT * FROM FILMS LIMIT ?";

            popularFilms = jdbcTemplate.query(sqlQuery2, FilmRepository::mapRowToFilm, count);
        }
        return popularFilms;
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {

        if (userStorage.read(userId).isEmpty() || filmStorage.read(filmId).isEmpty()) {
            return false;
        }

        String sqlQuery = "DELETE FROM FILM_LIKES where FILM_ID = ? AND USER_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }


}

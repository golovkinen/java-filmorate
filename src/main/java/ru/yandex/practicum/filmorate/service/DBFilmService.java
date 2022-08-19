package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;

@Service
public class DBFilmService implements IFilmService{

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final IDirectorRepository directorRepository;
    private final JdbcTemplate jdbcTemplate;

    public DBFilmService(FilmStorage filmStorage, UserStorage userStorage, IDirectorRepository directorRepository, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.directorRepository = directorRepository;
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

        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.FILM_DURATION, m.MPA_ID, m.MPA_NAME from FILMS f JOIN MPA M on f.FILM_MPA = M.MPA_ID LEFT JOIN FILM_LIKES fl ON f.FILM_ID = fl.FILM_ID GROUP BY f.FILM_ID ORDER BY COUNT(fl.FILM_ID) DESC LIMIT ?";

        return jdbcTemplate.query(sqlQuery, FilmRepository::mapRowToFilm, count);
    }

    @Override
    public List<Film> readBestDirectorFilms(int directorId, String param) {

        if(directorRepository.read(directorId).isEmpty()) {
            return null;
        }

        String sqlQuery;

        if (param.equals("likes")) {
            sqlQuery ="select f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.FILM_DURATION, m.MPA_ID, m.MPA_NAME from FILMS f JOIN MPA M on f.FILM_MPA = M.MPA_ID RIGHT JOIN FILM_DIRECTORS fd ON f.FILM_ID = fd.FILM_ID AND fd.DIRECTOR_ID=? LEFT JOIN FILM_LIKES fl ON f.FILM_ID = fl.FILM_ID GROUP BY f.FILM_ID ORDER BY COUNT(fl.FILM_ID) DESC";
        } else if (param.equals("year")) {
            sqlQuery ="select f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.FILM_DURATION, m.MPA_ID, m.MPA_NAME from FILMS f JOIN MPA M on f.FILM_MPA = M.MPA_ID RIGHT JOIN FILM_DIRECTORS fd ON f.FILM_ID = fd.FILM_ID AND fd.DIRECTOR_ID=? GROUP BY YEAR(f.RELEASE_DATE), MONTH(f.RELEASE_DATE), DAY_OF_MONTH(f.RELEASE_DATE)";
        } else {
            return null;
        }

        return jdbcTemplate.query(sqlQuery, FilmRepository::mapRowToFilm, directorId);
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

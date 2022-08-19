package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GenreRepository implements IGenreRepository {

    private final JdbcTemplate jdbcTemplate;

    public GenreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> readAll() {
        return jdbcTemplate.query("SELECT * FROM GENRES", this::mapRowToGenre);

    }

    @Override
    public Optional<Genre> read(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM GENRES WHERE GENRE_ID = ?", this::mapRowToGenre, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean setFilmGenres(Film film) {

        boolean updated = false;

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            deleteFilmGenres(film.getId());
            return false;
        }

        deleteFilmGenres(film.getId());

        String sqlQuery = "INSERT into FILM_GENRES (FILM_ID, GENRE_ID) " +
                "values (?, ?)";

        for (Integer genreId : film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet())) {
            updated = jdbcTemplate.update(sqlQuery
                    , film.getId()
                    , genreId) > 0;
        }
        return updated;
    }

    @Override
    public Set<Genre> loadFilmGenres(int filmId) {

        Set<Genre> filmGenres = new HashSet<>();

        try {
            for(Genre genre : jdbcTemplate.query("SELECT * FROM GENRES WHERE GENRE_ID IN (SELECT GENRE_ID " +
                    "FROM FILM_GENRES WHERE FILM_ID=?)", this::mapRowToGenre, filmId)) {
                filmGenres.add(genre);
            }

            return filmGenres;

        } catch (EmptyResultDataAccessException e) {
            return new HashSet<>();
        }
    }

    @Override
    public boolean deleteFilmGenres(int filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRES where FILM_ID = ?";
        try {
            return jdbcTemplate.update(sqlQuery, filmId) > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public void updateFilmGenres(Film film) {

        deleteFilmGenres(film.getId());
        setFilmGenres(film);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }
}

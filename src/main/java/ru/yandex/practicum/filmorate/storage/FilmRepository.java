package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FilmRepository implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private static IGenreRepository iGenreRepository;

    private static IMPARepository impaRepository;

    public FilmRepository(JdbcTemplate jdbcTemplate, IGenreRepository iGenreRepository, IMPARepository impaRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.iGenreRepository = iGenreRepository;
        this.impaRepository = impaRepository;
    }

    @Override
    public Film create(Film film) {

        if (film.getMpa().getName() == null) {
            film.setMpa(impaRepository.read(film.getMpa().getId()));
        }

        String sqlQuery = "insert into FILMS (FILM_NAME, FILM_DESCRIPTION," +
                "RELEASE_DATE, FILM_DURATION, FILM_MPA) values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());

        List<Genre> filmGenres = new ArrayList<>();

        if (film.getGenres() == null) {
            film.setGenres(filmGenres);
        } else {
            filmGenres = film.getGenres().stream()
                    .map(e -> iGenreRepository.read(e.getId()))
                    .collect(Collectors.toList());
            film.setGenres(filmGenres);
        }

        iGenreRepository.setFilmGenres(film);

        return film;

    }

    @Override
    public List<Film> readAll() {

        String sqlQuery = "select * from FILMS";
        return jdbcTemplate.query(sqlQuery, FilmRepository::mapRowToFilm);

    }

    @Override
    public Film read(int id) {
        String sqlQuery = "select * from FILMS WHERE FILM_ID = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, FilmRepository::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean update(Film film) {
        if (film.getId() != null && read(film.getId()) != null) {

            String sqlQuery = "update FILMS set " +
                    "FILM_NAME = ?, FILM_DESCRIPTION = ?, RELEASE_DATE = ?, " +
                    "FILM_DURATION = ?, FILM_MPA = ? where FILM_ID = ?";

            if (film.getMpa().getName() == null) {
                film.setMpa(impaRepository.read(film.getMpa().getId()));
            }

            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<>());
            } else {
                film.setGenres(film.getGenres().stream()
                        .map(e -> iGenreRepository.read(e.getId()))
                        .collect(Collectors.toList()));
            }

            iGenreRepository.updateFilmGenres(film);

            return jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , Date.valueOf(film.getReleaseDate())
                    , film.getDuration()
                    , film.getMpa().getId()
                    , film.getId()) > 0
                    ;
        }
        return false;
    }

    @Override
    public boolean delete(int filmId) {
        String sqlQuery = "DELETE FROM FILMS where FILM_ID = ?";

        try {
            iGenreRepository.deleteFilmGenres(filmId);
            return jdbcTemplate.update(sqlQuery, filmId) > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.execute("DROP TABLE FILMS CASCADE");
    }

    public static Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("FILM_DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("FILM_DURATION"))
                .genres(iGenreRepository.loadFilmGenres(resultSet.getInt("FILM_ID")))
                .mpa(impaRepository.read(resultSet.getInt("FILM_MPA")))
                .build();
    }
}

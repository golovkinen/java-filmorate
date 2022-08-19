package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmRepository implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private static IGenreRepository iGenreRepository;

    private static IDirectorRepository iDirectorRepository;

    private static IMPARepository impaRepository;

    public FilmRepository(JdbcTemplate jdbcTemplate, IGenreRepository iGenreRepository, IDirectorRepository iDirectorRepository, IMPARepository impaRepository) {
        this.jdbcTemplate = jdbcTemplate;
        FilmRepository.iGenreRepository = iGenreRepository;
        this.iDirectorRepository = iDirectorRepository;
        FilmRepository.impaRepository = impaRepository;
    }

    @Override
    public Film create(Film film) {

        if (film.getMpa().getName() == null) {
            film.setMpa(impaRepository.read(film.getMpa().getId()).get());
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

        iGenreRepository.setFilmGenres(film);
        film.setGenres(iGenreRepository.loadFilmGenres(film.getId()));
        iDirectorRepository.setFilmDirectors(film);
        film.setDirectors(iDirectorRepository.loadFilmDirectors(film.getId()));

        return film;

    }

    @Override
    public List<Film> readAll() {

        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.FILM_DURATION, m.MPA_ID, m.MPA_NAME from FILMS f JOIN MPA M on f.FILM_MPA = M.MPA_ID";
        return jdbcTemplate.query(sqlQuery, FilmRepository::mapRowToFilm);

    }

    @Override
    public Optional<Film> read(int id) {
        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.FILM_DURATION, m.MPA_ID, m.MPA_NAME from FILMS f JOIN MPA M on f.FILM_MPA = M.MPA_ID WHERE f.FILM_ID = ?";

        try {
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, FilmRepository::mapRowToFilm, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Film film) {

        if (film.getId() != null && read(film.getId()).isPresent()) {

            String sqlQuery = "update FILMS set " +
                    "FILM_NAME = ?, FILM_DESCRIPTION = ?, RELEASE_DATE = ?, " +
                    "FILM_DURATION = ?, FILM_MPA = ? where FILM_ID = ?";

            boolean genreUpdated = iGenreRepository.setFilmGenres(film);
            film.setGenres(iGenreRepository.loadFilmGenres(film.getId()));
            boolean directorUpdated = iDirectorRepository.setFilmDirectors(film);
            film.setDirectors(iDirectorRepository.loadFilmDirectors(film.getId()));
            boolean filmUpdated = jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , Date.valueOf(film.getReleaseDate())
                    , film.getDuration()
                    , film.getMpa().getId()
                    , film.getId()) > 0
                    ;

            return filmUpdated || genreUpdated || directorUpdated;

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
                .directors(iDirectorRepository.loadFilmDirectors(resultSet.getInt("FILM_ID")))
                .genres(iGenreRepository.loadFilmGenres(resultSet.getInt("FILM_ID")))
                .mpa(MPA.builder()
                        .id(resultSet.getInt("mpa_id"))
                        .name(resultSet.getString("mpa_name"))
                        .build())
                .build();
    }
}

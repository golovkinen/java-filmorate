package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.IFilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.IGenreRepository;
import ru.yandex.practicum.filmorate.storage.IMPARepository;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@Slf4j
public class FilmController {

    private final FilmStorage filmStorage;
    private final IFilmService iFilmService;
    private final IGenreRepository iGenreRepository;
    private final IMPARepository impaRepository;
    @Autowired
    public FilmController(@Qualifier("filmRepository") FilmStorage filmStorage,
                          @Qualifier("DBFilmService") IFilmService iFilmService,
                          IGenreRepository iGenreRepository,
                          IMPARepository impaRepository) {
        this.filmStorage = filmStorage;
        this.iFilmService = iFilmService;
        this.iGenreRepository = iGenreRepository;
        this.impaRepository = impaRepository;
    }

    @GetMapping(value = "/films")
    public ResponseEntity<Collection<Film>> readAllFilms() {
        final Collection<Film> films = filmStorage.readAll();
        log.debug("Текущее количество пользователей: {}", films.size());
        return films != null
                ? new ResponseEntity<>(films, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/films/{id}")
    public ResponseEntity<Film> read(@PathVariable(name = "id") int id) {
        final Optional<Film> film = filmStorage.read(id);

        return !film.isEmpty()
                ? new ResponseEntity<>(film.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/films")
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {

        final Film newFilm = filmStorage.create(film);
        log.debug(String.valueOf(newFilm));
        return newFilm != null
                ? new ResponseEntity<>(newFilm, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/films")
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {

        final boolean updated = filmStorage.update(film);
        log.debug(String.valueOf(filmStorage.read(film.getId())));
        return updated
                ? new ResponseEntity<>(filmStorage.read(film.getId()).get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/films/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        final boolean deleted = filmStorage.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping(value = "/films/{filmId}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable int filmId,
                                     @PathVariable int userId) {

        final boolean liked = iFilmService.addLike(filmId, userId);

        return liked
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/films/popular")
    public ResponseEntity<Collection<Film>> readTenBestFilms(@RequestParam(defaultValue = "10") int count) {

        if (count <= 0) {
            count = 10;
        }

        final Collection<Film> topTenFilms = iFilmService.readTenBestFilms(count);

        return topTenFilms != null
                ? new ResponseEntity<>(topTenFilms, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/films/{filmId}/like/{userId}")
    public ResponseEntity<?> deleteLike(@PathVariable int filmId,
                                        @PathVariable int userId) {

        final boolean deleted = iFilmService.deleteLike(filmId, userId);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/genres")
    public ResponseEntity<Collection<Genre>> readAllGenres() {

        final Collection<Genre> allGenres = iGenreRepository.readAll();

        return allGenres != null
                ? new ResponseEntity<>(allGenres, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/genres/{genreId}")
    public ResponseEntity<?> readGenre(@PathVariable int genreId) {

        final Optional<Genre> genre = iGenreRepository.read(genreId);

        return !genre.isEmpty()
                ? new ResponseEntity<>(genre.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/mpa")
    public ResponseEntity<Collection<MPA>> readAllMPAs() {

        final Collection<MPA> allMPAs = impaRepository.readAll();

        return allMPAs != null
                ? new ResponseEntity<>(allMPAs, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/mpa/{mpaId}")
    public ResponseEntity<?> readMPA(@PathVariable int mpaId) {

        final Optional<MPA> mpa = impaRepository.read(mpaId);

        return !mpa.isEmpty()
                ? new ResponseEntity<>(mpa.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

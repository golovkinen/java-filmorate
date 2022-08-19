package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.IFilmService;
import ru.yandex.practicum.filmorate.storage.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class FilmController {

    private final FilmStorage filmStorage;
    private final IFilmService iFilmService;
    private final IGenreRepository iGenreRepository;
    private final IMPARepository impaRepository;
    private final IDirectorRepository iDirectorRepository;
    @Autowired
    public FilmController(FilmStorage filmStorage,
                          IFilmService iFilmService,
                          IGenreRepository iGenreRepository,
                          IMPARepository impaRepository, IDirectorRepository iDirectorRepository) {
        this.filmStorage = filmStorage;
        this.iFilmService = iFilmService;
        this.iGenreRepository = iGenreRepository;
        this.impaRepository = impaRepository;
        this.iDirectorRepository = iDirectorRepository;
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

    @GetMapping(value = "/directors")
    public ResponseEntity<Collection<Director>> readAllDirectors() {
        final Collection<Director> directors = iDirectorRepository.readAll();
        log.debug("Текущее количество режиссеров: {}", directors.size());
        return directors != null
                ? new ResponseEntity<>(directors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/directors/{id}")
    public ResponseEntity<Director> readDirector (@PathVariable(name = "id") int id) {
        final Optional<Director> director = iDirectorRepository.read(id);

        return !director.isEmpty()
                ? new ResponseEntity<>(director.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/directors")
    public ResponseEntity<Director> createDirector(@Valid @RequestBody Director director) {

        final Director newDirector = iDirectorRepository.create(director);
        log.debug(String.valueOf(newDirector));
        return newDirector != null
                ? new ResponseEntity<>(newDirector, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/directors")
    public ResponseEntity<Director> updateDirector (@Valid @RequestBody Director director) {

        final boolean updated = iDirectorRepository.update(director);
        log.debug(String.valueOf(iDirectorRepository.read(director.getId())));

        return updated
                ? new ResponseEntity<>(iDirectorRepository.read(director.getId()).get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/directors/{id}")
    public ResponseEntity<?> deleteDirector(@PathVariable(name = "id") int id) {
        final boolean deleted = iDirectorRepository.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/films/director/{directorId}")
    public ResponseEntity <List<Film>> readBestDirectorFilms (@PathVariable(name = "directorId") int directorId,
                                                              @RequestParam(value = "sortBy", required = true) String condition) {
        final List<Film> directorFilms = iFilmService.readBestDirectorFilms(directorId, condition);

        return directorFilms != null
                ? new ResponseEntity<>(directorFilms, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

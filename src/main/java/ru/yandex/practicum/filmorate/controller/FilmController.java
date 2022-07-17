package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    @Autowired
    FilmStorage filmStorage;

    @Autowired
    FilmService filmService;

    @GetMapping
    public ResponseEntity<Collection<Film>> readAllFilms() {
        final Collection <Film> films = filmStorage.readAll();
        log.debug("Текущее количество пользователей: {}", films.size());
        return films != null && !films.isEmpty()
                ? new ResponseEntity<>(films, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Film> read (@PathVariable(name = "id") int id) {
        final Film film = filmStorage.read(id);

        return film != null
                ? new ResponseEntity<>(film, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {

        final Film newFilm = filmStorage.create(film);
        log.debug(String.valueOf(newFilm));
        return newFilm != null
                ? new ResponseEntity<>(newFilm, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {

        final boolean updated = filmStorage.update(film);
        log.debug(String.valueOf(filmStorage.read(film.getId())));
        return updated
                ? new ResponseEntity<>(filmStorage.read(film.getId()), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        final boolean deleted = filmStorage.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PutMapping(value = "/{filmId}/likes/{userId}")
    public ResponseEntity<?> addFriend (@PathVariable int filmId,
                                        @PathVariable int userId) {

            final boolean liked = filmService.addLike(filmId, userId);

        return liked
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<Collection<Film>> readTenBestFilms(@RequestParam(defaultValue = "10") int count) {

        if (count <= 0) {
            count = 10;
        }

        final Collection <Film> topTenFilms = filmService.readTenBestFilms(count);

        return topTenFilms != null && !topTenFilms.isEmpty()
                ? new ResponseEntity<>(topTenFilms, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{filmId}/likes/{userId}")
    public ResponseEntity<?> deleteFriend (@PathVariable int filmId,
                                           @PathVariable int userId) {

        final boolean deleted = filmService.deleteLike(filmId, userId);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }
}

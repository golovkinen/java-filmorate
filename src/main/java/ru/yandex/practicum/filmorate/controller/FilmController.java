package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceInterface;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmServiceInterface filmServiceInterface;

    @Autowired
    public FilmController(FilmServiceInterface filmServiceInterface) {
        this.filmServiceInterface = filmServiceInterface;
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> readAllFilms() {
        final Collection <Film> films = filmServiceInterface.readAll();
        log.debug("Текущее количество пользователей: {}", films.size());
        return films != null && !films.isEmpty()
                ? new ResponseEntity<>(films, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Film> read (@PathVariable(name = "id") int id) {
        final Film film = filmServiceInterface.read(id);

        return film != null
                ? new ResponseEntity<>(film, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Film newFilm = filmServiceInterface.create(film);
        log.debug(String.valueOf(newFilm));
        return newFilm != null
                ? new ResponseEntity<>(newFilm, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {

        final boolean updated = filmServiceInterface.update(film);
        log.debug(String.valueOf(filmServiceInterface.read(film.getId())));
        return updated
                ? new ResponseEntity<>(filmServiceInterface.read(film.getId()), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        final boolean deleted = filmServiceInterface.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

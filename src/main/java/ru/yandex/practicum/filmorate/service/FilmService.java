package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FilmService implements FilmServiceInterface {

    private static final Map<Integer, Film> FILMS_MAP = new HashMap<Integer, Film>();
    private static final AtomicInteger FILM_ID_HOLDER = new AtomicInteger();

    @Override
    public Film create(Film film) {
        final int filmId = FILM_ID_HOLDER.incrementAndGet();
        film.setId(filmId);
        FILMS_MAP.put(filmId, film);
        return film;
    }

    @Override
    public List<Film> readAll() {
        return new ArrayList<>(FILMS_MAP.values());
    }

    @Override
    public Film read(int id) {
        return FILMS_MAP.get(id);
    }

    @Override
    public boolean update(Film film, int id) {
        if (film.getId() == id && FILMS_MAP.containsKey(id)) {
            FILMS_MAP.put(id, film);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        return FILMS_MAP.remove(id) != null;
    }
}

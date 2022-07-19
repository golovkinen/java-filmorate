package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryFilmStorage implements FilmStorage{


        private static final Map<Integer, Film> FILMS_MAP = new HashMap<>();
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
        public boolean update(Film film) {
            if (film.getId() != null && FILMS_MAP.containsKey(film.getId())) {
                FILMS_MAP.put(film.getId(), film);
                return true;
            }
            return false;
        }

        @Override
        public boolean delete(int id) {
            return FILMS_MAP.remove(id) != null;
        }

        @Override
        public void deleteAll() {
            FILMS_MAP.clear();
            FILM_ID_HOLDER.lazySet(0);
        }

}

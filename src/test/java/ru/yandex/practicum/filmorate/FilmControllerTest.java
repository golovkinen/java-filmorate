package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FilmStorage filmStorage;

    @Autowired
    UserStorage userStorage;

    @Autowired
    FilmService filmService;

    @AfterEach
    public void resetDB () {
        filmStorage.deleteAll();
    }

    @Test
    @DisplayName("POST /films Создаю film ")
    void testCreateFilm() throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Matrix\",\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.\",\"releaseDate\":\"1999-01-01\",\"duration\":136}"))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Matrix")))
                .andExpect(jsonPath("$.description", is("Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.")))
                .andExpect(jsonPath("$.releaseDate", is("1999-01-01")))
                .andExpect(jsonPath("$.duration", is(136)));
    }

    @Test
    @DisplayName("POST /films Создаю film имя null")
    void testCreateFilmNullName() throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":null,\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.\",\"releaseDate\":\"1999-01-01\",\"duration\":136}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films Создаю film название пустое")
    void testCreateFilmNoName() throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.\",\"releaseDate\":\"1999-01-01\",\"duration\":136}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films Создаю film описание больше 200 знаков")
    void testCreateFilmDescriptionMore200Chars() throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Matrix\",\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео, и нет места в сети, куда он бы не смог проникнуть. Но однажды всё меняется. Томас узнаёт ужасающую правду о реальности.\",\"releaseDate\":\"1999-01-01\",\"duration\":136}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films Создаю film дата релиза в будущем")
    void testCreateFilmReleaseInFuture() throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Matrix\",\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.\",\"releaseDate\":\"2099-01-01\",\"duration\":136}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films Создаю film дата релиза до 1895-12-28")
    void testCreateFilmReleaseBeforeFilmInvetion() throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Matrix\",\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.\",\"releaseDate\":\"1799-01-01\",\"duration\":136}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films дата релиза null")
    void testCreateFilmReleaseNull() throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Matrix\",\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.\",\"releaseDate\":null,\"duration\":136}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films Если продолжительность - null")
    void filmDurationNullError () throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Matrix\",\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.\",\"releaseDate\":\"1999-01-01\",\"duration\":null}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films Если продолжительность - отрицательная")
    void filmDurationBelowZeroError () throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Matrix\",\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.\",\"releaseDate\":\"1999-01-01\",\"duration\":-1}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /films/{filmId}/likes/{userId} Ставим Лайк фильму")
    void filmLikedByUserSuccess () throws Exception {

        User user1 = new User("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 07, 11));
        userStorage.create(user1);

        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        mockMvc.perform(put("/films/{filmId}/likes/{userId}", 1,1))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /films/{filmId}/likes/{userId} Ставим Лайк фильму Юзером которого нет")
    void filmLikedByUserNotInMapError () throws Exception {

        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        mockMvc.perform(put("/films/{filmId}/likes/{userId}", 1,1))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /films/{filmId}/likes/{userId} Ставим Лайк фильму которого нет")
    void filmNotInMapLikedByUserError () throws Exception {

        User user1 = new User("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 07, 11));
        userStorage.create(user1);

        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        mockMvc.perform(put("/films/{filmId}/likes/{userId}", 2,1))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /films/popular?count={count} Получаем TOP 3 фильмов")
    void getTopTenSuccess () throws Exception {

        User user1 = new User("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 07, 11));
        userStorage.create(user1);

        User user2 = new User("Email2@mail.com", "Login2", "Name2", LocalDate.of(1985, 10, 15));
        userStorage.create(user2);

        User user3 = new User("Email3@mail.com", "Login3", "Name3", LocalDate.of(1980, 11, 10));
        userStorage.create(user3);

        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        Film film2 = new Film("name2", "description2", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film2);

        Film film3 = new Film("name3", "description3", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film3);

        Film film4= new Film("name4", "description4", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film4);

        Film film5 = new Film("name5", "description5", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film5);

        filmService.addLike(1,1);
        filmService.addLike(2,1);
        filmService.addLike(3,5);
        filmService.addLike(1,4);
        filmService.addLike(2,4);
        filmService.addLike(3,4);

        mockMvc.perform(get("/films/popular?count={count}", 3))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(4)))
                .andExpect(jsonPath("$[1].id", is(1)))
                .andExpect(jsonPath("$[2].id", is(5)));

    }

    @Test
    @DisplayName("GET /films/popular?count={count} Получаем TOP 10 фильм один нет лайков")
    void getTopTenOneFilmNoLikeSuccess () throws Exception {

        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        mockMvc.perform(get("/films/popular?count={count}", 3))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));

    }

    @Test
    @DisplayName("GET /films/popular Получаем TOP фильмов без параметров")
    void getTopTenNoParamsSuccess () throws Exception {

        User user1 = new User("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 07, 11));
        userStorage.create(user1);

        User user2 = new User("Email2@mail.com", "Login2", "Name2", LocalDate.of(1985, 10, 15));
        userStorage.create(user2);

        User user3 = new User("Email3@mail.com", "Login3", "Name3", LocalDate.of(1980, 11, 10));
        userStorage.create(user3);

        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        Film film2 = new Film("name2", "description2", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film2);

        Film film3 = new Film("name3", "description3", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film3);

        Film film4= new Film("name4", "description4", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film4);

        Film film5 = new Film("name5", "description5", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film5);

        Film film6 = new Film("name5", "description6", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film6);

        Film film7 = new Film("name7", "description7", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film7);

        Film film8 = new Film("name8", "description8", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film8);

        Film film9 = new Film("name9", "description9", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film9);

        Film film10 = new Film("name10", "description10", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film10);

        filmService.addLike(1,1);
        filmService.addLike(2,1);
        filmService.addLike(3,5);
        filmService.addLike(1,4);
        filmService.addLike(2,4);
        filmService.addLike(3,4);

        mockMvc.perform(get("/films/popular"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(4)))
                .andExpect(jsonPath("$[1].id", is(1)))
                .andExpect(jsonPath("$[2].id", is(5)))
                .andExpect(jsonPath("$[3].id", is(2)))
                .andExpect(jsonPath("$[4].id", is(3)))
                .andExpect(jsonPath("$[5].id", is(6)))
                .andExpect(jsonPath("$[6].id", is(7)))
                .andExpect(jsonPath("$[7].id", is(8)))
                .andExpect(jsonPath("$[8].id", is(9)))
                .andExpect(jsonPath("$[9].id", is(10)));

    }

    @Test
    @DisplayName("GET /films/popular?count={count} Получаем TOP 10 - нет фильмов")
    void getTopTenNoFilmsError () throws Exception {

        mockMvc.perform(get("/films/popular?count={count}", 10))

                // Validate the response code and content type
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("GET /films/popular?count={count} Получаем TOP фильмов параметр 0")
    void getTopTenParamsZeroSuccess () throws Exception {

        User user1 = new User("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 07, 11));
        userStorage.create(user1);

        User user2 = new User("Email2@mail.com", "Login2", "Name2", LocalDate.of(1985, 10, 15));
        userStorage.create(user2);

        User user3 = new User("Email3@mail.com", "Login3", "Name3", LocalDate.of(1980, 11, 10));
        userStorage.create(user3);

        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        Film film2 = new Film("name2", "description2", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film2);

        Film film3 = new Film("name3", "description3", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film3);

        Film film4= new Film("name4", "description4", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film4);

        Film film5 = new Film("name5", "description5", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film5);

        filmService.addLike(1,1);
        filmService.addLike(2,1);
        filmService.addLike(3,5);
        filmService.addLike(1,4);
        filmService.addLike(2,4);
        filmService.addLike(3,4);

        mockMvc.perform(get("/films/popular?count={count}", 0))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(4)))
                .andExpect(jsonPath("$[1].id", is(1)))
                .andExpect(jsonPath("$[2].id", is(5)))
                .andExpect(jsonPath("$[3].id", is(2)))
                .andExpect(jsonPath("$[4].id", is(3)));

    }

    @Test
    @DisplayName("GET /films/popular?count={count} Получаем TOP фильмов параметр отрицательный")
    void getTopTenParamsMinusSuccess () throws Exception {

        User user1 = new User("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 07, 11));
        userStorage.create(user1);

        User user2 = new User("Email2@mail.com", "Login2", "Name2", LocalDate.of(1985, 10, 15));
        userStorage.create(user2);

        User user3 = new User("Email3@mail.com", "Login3", "Name3", LocalDate.of(1980, 11, 10));
        userStorage.create(user3);

        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        Film film2 = new Film("name2", "description2", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film2);

        Film film3 = new Film("name3", "description3", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film3);

        Film film4= new Film("name4", "description4", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film4);

        Film film5 = new Film("name5", "description5", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film5);

        filmService.addLike(1,1);
        filmService.addLike(2,1);
        filmService.addLike(3,5);
        filmService.addLike(1,4);
        filmService.addLike(2,4);
        filmService.addLike(3,4);

        mockMvc.perform(get("/films/popular?count={count}", -5))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(4)))
                .andExpect(jsonPath("$[1].id", is(1)))
                .andExpect(jsonPath("$[2].id", is(5)))
                .andExpect(jsonPath("$[3].id", is(2)))
                .andExpect(jsonPath("$[4].id", is(3)));

    }

    @Test
    @DisplayName("DELETE /films/{filmId}/likes/{userId} Удаляем Лайк")
    void deleteLikeSuccess () throws Exception {

        User user1 = new User("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 07, 11));
        userStorage.create(user1);

        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        filmService.addLike(1,1);

        mockMvc.perform(delete("/films/{filmId}/likes/{userId}", 1,1))

                // Validate the response code and content type
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("DELETE /films/{filmId}/likes/{userId} Удаляем Лайк")
    void deleteLikeNoLikeError () throws Exception {

        User user1 = new User("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 07, 11));
        userStorage.create(user1);

        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        mockMvc.perform(delete("/films/{filmId}/likes/{userId}", 1,1))

                // Validate the response code and content type
                .andExpect(status().isInternalServerError());

    }

    @Test
    @DisplayName("DELETE /films/{filmId}/likes/{userId} Удаляем Лайк")
    void deleteLikeNoUserLikeError () throws Exception {

        User user1 = new User("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 07, 11));
        userStorage.create(user1);

        User user2 = new User("Email2@mail.com", "Login2", "Name2", LocalDate.of(1985, 10, 15));
        userStorage.create(user2);

        User user3 = new User("Email3@mail.com", "Login3", "Name3", LocalDate.of(1980, 11, 10));
        userStorage.create(user3);

        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        filmService.addLike(1,1);
        filmService.addLike(3,1);

        mockMvc.perform(delete("/films/{filmId}/likes/{userId}", 1,2))

                // Validate the response code and content type
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("DELETE /films/{filmId}/likes/{userId} Удаляем Лайк")
    void deleteLikeNoFilmError () throws Exception {

        User user1 = new User("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 07, 11));
        userStorage.create(user1);


        Film film1 = new Film("name1", "description1", LocalDate.of(1999, 10, 11), 123);
        filmStorage.create(film1);

        filmService.addLike(1,1);

        mockMvc.perform(delete("/films/{filmId}/likes/{userId}", 2,1))

                // Validate the response code and content type
                .andExpect(status().isInternalServerError());
    }
}

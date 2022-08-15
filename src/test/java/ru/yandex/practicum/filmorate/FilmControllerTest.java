package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.service.DBFilmService;
import ru.yandex.practicum.filmorate.storage.FilmRepository;
import ru.yandex.practicum.filmorate.storage.GenreRepository;
import ru.yandex.practicum.filmorate.storage.MPARepository;
import ru.yandex.practicum.filmorate.storage.UserRepository;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@AutoConfigureTestDatabase
@Sql(scripts = "/test_schema.sql")
@Sql(scripts = "/test_data.sql")
@AutoConfigureMockMvc

public class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    FilmRepository filmRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DBFilmService dbFilmService;

    @Autowired
    MPARepository mpaRepository;

    @Autowired
    GenreRepository genreRepository;

    @AfterEach
    public void resetDB() {
        filmRepository.deleteAll();
        userRepository.deleteAll();
    }
/*
    @Test
    @DisplayName("POST /films Создаю film ")
    void testCreateFilm() throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Matrix\",\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.\",\"releaseDate\":\"1999-01-01\",\"duration\":136, \"mpa\": { \"id\": 5}}"))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(11)))
                .andExpect(jsonPath("$.name", is("Matrix")))
                .andExpect(jsonPath("$.description", is("Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.")))
                .andExpect(jsonPath("$.releaseDate", is("1999-01-01")))
                .andExpect(jsonPath("$.duration", is(136)));
    }
*/
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
    void filmDurationNullError() throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Matrix\",\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.\",\"releaseDate\":\"1999-01-01\",\"duration\":null}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films Если продолжительность - отрицательная")
    void filmDurationBelowZeroError() throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Matrix\",\"description\":\"Жизнь Томаса Андерсона разделена на две части: днём он — самый обычный офисный работник, получающий нагоняи от начальства, а ночью превращается в хакера по имени Нео.\",\"releaseDate\":\"1999-01-01\",\"duration\":-1}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /films/{filmId}/like/{userId} Ставим Лайк фильму")
    void filmLikedByUserSuccess() throws Exception {

        mockMvc.perform(put("/films/{filmId}/like/{userId}", 1, 1))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /films/{filmId}/like/{userId} Ставим Лайк фильму Юзером которого нет")
    void filmLikedByUserNotInMapError() throws Exception {

        mockMvc.perform(put("/films/{filmId}/like/{userId}", 1, 5))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /films/{filmId}/like/{userId} Ставим Лайк фильму которого нет")
    void filmNotInMapLikedByUserError() throws Exception {


        mockMvc.perform(put("/films/{filmId}/like/{userId}", 12, 1))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /films/popular?count={count} Получаем TOP 3 фильмов")
    void getTopTenSuccess() throws Exception {

        dbFilmService.addLike(1, 1);
        dbFilmService.addLike(1, 2);
        dbFilmService.addLike(5, 3);
        dbFilmService.addLike(4, 1);
        dbFilmService.addLike(4, 2);
        dbFilmService.addLike(4, 3);

        mockMvc.perform(get("/films/popular?count={count}", 3))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(4)))
                .andExpect(jsonPath("$[1].id", is(1)))
                .andExpect(jsonPath("$[2].id", is(5)));

    }

    @Test
    @DisplayName("GET /films/popular?count=1 Получаем TOP 1, фильм один, нет лайков")
    void getTopTenOneFilmNoLikeSuccess() throws Exception {


        mockMvc.perform(get("/films/popular?count={count}", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));

    }

    @Test
    @DisplayName("GET /films/popular Получаем TOP фильмов без параметров")
    void getTopTenNoParamsSuccess() throws Exception {

        dbFilmService.addLike(1, 1);
        dbFilmService.addLike(1, 2);
        dbFilmService.addLike(5, 3);
        dbFilmService.addLike(4, 1);
        dbFilmService.addLike(4, 2);
        dbFilmService.addLike(4, 3);

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
    void getTopTenNoFilmsError() throws Exception {

        mockMvc.perform(get("/films/popular?count={count}", 10))

                // Validate the response code and content type
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("GET /films/popular?count={count} Получаем TOP фильмов параметр 0")
    void getTopTenParamsZeroSuccess() throws Exception {

        dbFilmService.addLike(1, 1);
        dbFilmService.addLike(1, 2);
        dbFilmService.addLike(5, 3);
        dbFilmService.addLike(4, 1);
        dbFilmService.addLike(4, 2);
        dbFilmService.addLike(4, 3);

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
    void getTopTenParamsMinusSuccess() throws Exception {

        dbFilmService.addLike(1, 1);
        dbFilmService.addLike(1, 2);
        dbFilmService.addLike(5, 3);
        dbFilmService.addLike(4, 1);
        dbFilmService.addLike(4, 2);
        dbFilmService.addLike(4, 3);

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
    @DisplayName("DELETE /films/{filmId}/like/{userId} Удаляем Лайк")
    void deleteLikeSuccess() throws Exception {

        dbFilmService.addLike(1, 1);

        mockMvc.perform(delete("/films/{filmId}/like/{userId}", 1, 1))

                // Validate the response code and content type
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} Удаляем Лайк")
    void deleteLikeNoLikeError() throws Exception {

        mockMvc.perform(delete("/films/{filmId}/like/{userId}", 1, 1))

                // Validate the response code and content type
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} Удаляем Лайк")
    void deleteLikeNoUserLikeError() throws Exception {

        dbFilmService.addLike(1, 1);
        dbFilmService.addLike(3, 1);

        mockMvc.perform(delete("/films/{filmId}/like/{userId}", 1, 2))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} Удаляем Лайк")
    void deleteLikeNoFilmError() throws Exception {

        dbFilmService.addLike(1, 1);

        mockMvc.perform(delete("/films/{filmId}/like/{userId}", 2, 1))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} Удаляем Лайк")
    void deleteLikeUserIdMinusError() throws Exception {

        dbFilmService.addLike(1, 1);

        mockMvc.perform(delete("/films/{filmId}/like/{userId}", 1, -2))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }
}

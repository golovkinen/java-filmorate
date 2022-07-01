package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("POST /films Создаю film ")
    void testCreateUser() throws Exception {

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

}

package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("POST /users")
    void CreateUser() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email1@mail.com\",\"login\":\"Login1\",\"name\":\"Name1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("Email1@mail.com")))
                .andExpect(jsonPath("$.login", is("Login1")))
                .andExpect(jsonPath("$.name", is("Name1")))
                .andExpect(jsonPath("$.birthday", is("1981-07-11")));
    }

    @Test
    @DisplayName("POST /users Создаю пользователя без имени")
    void testCreateUserNoName() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email2@mail.com\",\"login\":\"Login2\",\"name\":\"\",\"birthday\":\"1985-10-15\"}"))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("Email2@mail.com")))
                .andExpect(jsonPath("$.login", is("Login2")))
                .andExpect(jsonPath("$.name", is("Login2")))
                .andExpect(jsonPath("$.birthday", is("1985-10-15")));
    }

    @Test
    @DisplayName("POST /users Создаю пользователя имя null")
    void testCreateUserNullName() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email1@mail.com\",\"login\":\"Login1\",\"name\":null,\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Создаю пользователя логин с пробелом")
    void testCreateUserLoginWithSpaces() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email1@mail.com\",\"login\":\"Login 1\",\"name\":\"Name1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Создаю пользователя логин пустой")
    void testCreateUserLoginWithNoName() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email1@mail.com\",\"login\":\"\",\"name\":\"Name1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Создаю пользователя рождение в будущем")
    void testCreateUserBirthdayInFuture() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email1@mail.com\",\"login\":\"Login1\",\"name\":\"Name1\",\"birthday\":\"2081-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Если email - null")
    void emailNullError () throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":null,\"login\":\"Login1\",\"name\":\"Name1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Если email - поле не заполнено")
    void emailBlankError () throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\",\"login\":\"Login1\",\"name\":\"Name1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Если email - введен неверно")
    void emailWrongError () throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email.comyandex@\",\"login\":\"Login1\",\"name\":\"Name1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /users success")
    void testGetUsersSuccess() throws Exception {

        mockMvc.perform(get("/users"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", is("Email1@mail.com")))
                .andExpect(jsonPath("$[0].login", is("Login1")))
                .andExpect(jsonPath("$[0].name", is("Name1")))
                .andExpect(jsonPath("$[0].birthday", is("1981-07-11")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].email", is("Email2@mail.com")))
                .andExpect(jsonPath("$[1].login", is("Login2")))
                .andExpect(jsonPath("$[1].name", is("Login2")))
                .andExpect(jsonPath("$[1].birthday", is("1985-10-15")));
    }

    @Test
    @DisplayName("GET /users/1")
    void testGetUserById() throws Exception {

        mockMvc.perform(get("/users/{id}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("Email1@mail.com")))
                .andExpect(jsonPath("$.login", is("Login1")))
                .andExpect(jsonPath("$.name", is("Name1")))
                .andExpect(jsonPath("$.birthday", is("1981-07-11")));
    }

    @Test
    @DisplayName("GET /users/3 - Not Found")
    void testGetUserByIdNotFound() throws Exception {

        // doReturn(empty()).when(service).read(1);

        // Execute the GET request
        mockMvc.perform(get("/users/{id}", 3))
                // Validate the response code
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /users/1")
    void testUpdateUserSuccess() throws Exception {

        // Execute the PUT request
        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"email\":\"UpdatedEmail1@mail.com\",\"login\":\"UpdatedLogin1\",\"name\":\"UpdatedName1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("UpdatedEmail1@mail.com")))
                .andExpect(jsonPath("$.login", is("UpdatedLogin1")))
                .andExpect(jsonPath("$.name", is("UpdatedName1")))
                .andExpect(jsonPath("$.birthday", is("1981-07-11")));
    }

    @Test
    @DisplayName("PUT /users/3 - ошибка когда Мар пустая")
    void testUpdateUserNoUserInMap() throws Exception {

        // Execute the POST request
        mockMvc.perform(put("/users/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":3,\"email\":\"Email1@mail.com\",\"login\":\"Login1\",\"name\":\"Name1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isInternalServerError());
    }
}

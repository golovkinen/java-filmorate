/*
package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.util.Lists;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest2 {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService service;


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
                .andExpect(jsonPath("$[1].name", is("Name2")))
                .andExpect(jsonPath("$[1].birthday", is("1985-10-15")));
    }

    @Test
    @DisplayName("GET /users/1")
    void testGetUserById() throws Exception {
        User user1 = new User (1, "Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 7, 11));
        doReturn(user1).when(service).read(1);

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
    @DisplayName("GET /users/1 - Not Found")
    void testGetUserByIdNotFound() throws Exception {

        // doReturn(empty()).when(service).read(1);

        // Execute the GET request
        mockMvc.perform(get("/users/{id}", 1))
                // Validate the response code
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /users")
    void testCreateUser() throws Exception {

        User userToPost = new User ("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 7, 11));
        User userToReturn = new User (1, "Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 7, 11));

        doReturn(userToReturn).when(service).create(userToPost);

        // Execute the POST request
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userToPost)))

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

        User userToPost = new User ("Email1@mail.com", "Login1", "", LocalDate.of(1981, 7, 11));
        // Execute the POST request
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userToPost)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("Email1@mail.com")))
                .andExpect(jsonPath("$.login", is("Login1")))
                .andExpect(jsonPath("$.name", is("Login1")))
                .andExpect(jsonPath("$.birthday", is("1981-07-11")));
    }

    @Test
    @DisplayName("POST /users Создаю пользователя имя null")
    void testCreateUserNullName() throws Exception {

        User userToPost = new User ("Email1@mail.com", "Login1", null, LocalDate.of(1981, 7, 11));
        // Execute the POST request
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userToPost)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Создаю пользователя логин с пробелом")
    void testCreateUserLoginWithSpaces() throws Exception {

        User userToPost = new User ("Email1@mail.com", "Login 1", "Name1" , LocalDate.of(1981, 7, 11));
        // Execute the POST request
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userToPost)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Создаю пользователя логин пустой")
    void testCreateUserLoginWithNoName() throws Exception {

        User userToPost = new User ("Email1@mail.com", "", "Name1" , LocalDate.of(1981, 7, 11));
        // Execute the POST request
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userToPost)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Создаю пользователя рождение в будущем")
    void testCreateUserBirthdayInFuture() throws Exception {

        User userToPost = new User ("Email1@mail.com", "Login 1", "Name1" , LocalDate.of(2081, 7, 11));
        // Execute the POST request
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userToPost)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /users/1")
    void testUpdateUserSuccess() throws Exception {

        User userToPost = new User ("Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 7, 11));
        User userToReturn = new User (1, "Email1@mail.com", "Login1", "Name1", LocalDate.of(1981, 7, 11));
        User userToUpdate = new User (1, "UpdatedEmail1@mail.com", "UpdatedLogin1", "UpdatedName1", LocalDate.of(1981, 7, 11));
        User userToReturnUpdated = new User (1, "UpdatedEmail1@mail.com", "UpdatedLogin1", "UpdatedName1", LocalDate.of(1981, 7, 11));

        doReturn(userToReturn).when(service).read(1);
        doReturn(userToReturnUpdated).when(service).update(userToUpdate, 1);

        // Execute the PUT request
        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userToUpdate)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/users/1"))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("UpdatedEmail1@mail.com")))
                .andExpect(jsonPath("$.login", is("UpdatedLogin1")))
                .andExpect(jsonPath("$.name", is("UpdatedName1")))
                .andExpect(jsonPath("$.birthday", is("1981-07-11")));
    }

    @Test
    @DisplayName("PUT /users/1 - ошибка когда Мар пустая")
    void testUpdateUserNoUserInMap() throws Exception {

        User userToUpdate = new User (1, "UpdatedEmail1@mail.com", "UpdatedLogin1", "UpdatedName1", LocalDate.of(1981, 7, 11));

       // doReturn(empty()).when(service).update(userToUpdate, 1);

        // Execute the POST request
        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userToUpdate)))

                // Validate the response code and content type
                        .andExpect(status().isNotModified());
    }

    @Test
    @DisplayName("POST /users Если email - null")
    void emailNullError () throws Exception {
        User userToPost = new User ("null", "Login1", "Name1", LocalDate.of(1981, 7, 11));

        // Execute the POST request
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userToPost)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Если email - поле не заполнено")
    void emailBlankError () throws Exception {
        User userToPost = new User ("", "Login1", "Name1", LocalDate.of(1981, 7, 11));

        // Execute the POST request
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userToPost)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Если email - введен неверно")
    void emailWrongError () throws Exception {
        User userToPost = new User ("email.comyandex@", "Login1", "Name1", LocalDate.of(1981, 7, 11));

        // Execute the POST request
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userToPost)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }


     static String asJsonString(User user) throws JsonProcessingException {

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);;

            return mapper.writeValueAsString(user);
    }

   /* public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

  /*  public static String asJsonString(final Object obj) {
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            return gson.toJson(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    } */

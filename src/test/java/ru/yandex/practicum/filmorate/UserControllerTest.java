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
import ru.yandex.practicum.filmorate.service.DBUserService;
import ru.yandex.practicum.filmorate.storage.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@Sql(scripts = "/test_schema.sql")
@Sql(scripts = "/test_data.sql")
@AutoConfigureMockMvc

public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DBUserService dbUserService;

    @AfterEach
    public void resetDB() {
        userRepository.deleteAll();
    }

  /* @Test
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
*/
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
    void emailNullError() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":null,\"login\":\"Login1\",\"name\":\"Name1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Если email - поле не заполнено")
    void emailBlankError() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\",\"login\":\"Login1\",\"name\":\"Name1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users Если email - введен неверно")
    void emailWrongError() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email.comyandex@\",\"login\":\"Login1\",\"name\":\"Name1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("GET /users/3 - Not Found")
    void testGetUserByIdNotFound() throws Exception {

        // Execute the GET request
        mockMvc.perform(get("/users/{id}", 5))
                // Validate the response code
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /users")
    void testUpdateUserSuccess() throws Exception {

        // Execute the PUT request
        mockMvc.perform(put("/users")
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
    @DisplayName("PUT /users - ошибка когда Мар пустая")
    void testUpdateUserNoUserInMap() throws Exception {

        // Execute the POST request
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":5,\"email\":\"UpdatedEmail1@mail.com\",\"login\":\"UpdatedLogin1\",\"name\":\"Name1\",\"birthday\":\"1981-07-11\"}"))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /users/3 - Not Found")
    void testDeleteUserByIdNotFound() throws Exception {

        // Execute the GET request
        mockMvc.perform(delete("/users/{id}", 5))
                // Validate the response code
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("DELETE /users/1 - OK")
    void testDeleteUserById() throws Exception {

        // Execute the GET request
        mockMvc.perform(delete("/users/{id}", 1))
                // Validate the response code
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /users/{userId}/friends/{friendId} Добавляю друзей")
    void addFriend() throws Exception {

        mockMvc.perform(put("/users/{userId}/friends/{friendId}", 1, 2))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /users/{userId}/friends/{friendId} Пользователя нет")
    void addFriendNoUserFriendInMapError() throws Exception {


        mockMvc.perform(put("/users/{userId}/friends/{friendId}", 5, 2))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /users/{userId}/friends/{friendId} Друга пользователя нет")
    void addFriendNoFriendInMapError() throws Exception {


        mockMvc.perform(put("/users/{userId}/friends/{friendId}", 1, 5))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /users/{userId}/friends/{friendId} Если ID отрицательный или ноль")
    void addFriendZeroAndMinusError() throws Exception {


        mockMvc.perform(put("/users/{userId}/friends/{friendId}", -1, 0))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /users/{userId}/friends Получаю список друзей")
    void getUserFriendsList() throws Exception {

        dbUserService.addFriend(1, 2);
        dbUserService.addFriend(1, 3);

        mockMvc.perform(get("/users/{userId}/friends", 1))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].email", is("Email2@mail.com")))
                .andExpect(jsonPath("$[0].login", is("Login2")))
                .andExpect(jsonPath("$[0].name", is("Name2")))
                .andExpect(jsonPath("$[0].birthday", is("1985-10-15")))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[1].email", is("Email3@mail.com")))
                .andExpect(jsonPath("$[1].login", is("Login3")))
                .andExpect(jsonPath("$[1].name", is("Name3")))
                .andExpect(jsonPath("$[1].birthday", is("1980-11-10")));
    }

    @Test
    @DisplayName("GET /users/{userId}/friends Получаю список друзей нет User")
    void getUserFriendsListNoUser() throws Exception {

        mockMvc.perform(get("/users/{userId}/friends", 4))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /users/{userId}/friends/common/{otherId} Получаю список общих друзей")
    void getCommonFriendsList() throws Exception {

        dbUserService.addFriend(1, 2);
        dbUserService.addFriend(3, 2);

        mockMvc.perform(get("/users/{userId}/friends/common/{otherId}", 1, 3))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].email", is("Email2@mail.com")))
                .andExpect(jsonPath("$[0].login", is("Login2")))
                .andExpect(jsonPath("$[0].name", is("Name2")))
                .andExpect(jsonPath("$[0].birthday", is("1985-10-15")));
    }

    @Test
    @DisplayName("GET /users/{userId}/friends/common/{otherId} Получаю список общих друзей но их нет")
    void getCommonFriendsListNoCommons() throws Exception {

        dbUserService.addFriend(1, 3);

        mockMvc.perform(get("/users/{userId}/friends/common/{otherId}", 1, 2))

                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /users/{userId}/friends/{friendId} удаляяю друга")
    void deleteFriendSuccess() throws Exception {

        dbUserService.addFriend(1, 2);
        dbUserService.addFriend(1, 3);

        mockMvc.perform(delete("/users/{userId}/friends/{friendId}", 1, 3))
                // Validate the response code
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /users/{userId}/friends/{friendId} удаляяю несуществующего друга")
    void deleteFriendNotFound() throws Exception {

        dbUserService.addFriend(1, 3);

        mockMvc.perform(delete("/users/{userId}/friends/{friendId}", 1, 2))
                // Validate the response code
                .andExpect(status().isInternalServerError());
    }
}

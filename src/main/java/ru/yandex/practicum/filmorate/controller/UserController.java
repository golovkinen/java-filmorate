package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserServiceInterface;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
  @Autowired
    UserService userService;

  @Autowired
    UserStorage userStorage;

    @GetMapping
    public ResponseEntity<Collection<User>> readAll() {
        final Collection <User> users = userStorage.readAll();
        log.debug("Текущее количество пользователей: {}", users.size());
        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> read(@PathVariable(name = "id") int id) {
        final User user = userStorage.read(id);

        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {

        User newUser = userStorage.create(user);
        log.debug(String.valueOf(newUser));
        return newUser != null
                ? new ResponseEntity<>(newUser, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {

        final boolean updated = userStorage.update(user);

        return updated
                ? new ResponseEntity<>(userStorage.read(user.getId()), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        final boolean deleted = userStorage.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(value = "/{userId}/friends/{friendId}")
    public ResponseEntity<?> addFriend (@PathVariable int userId,
                                        @PathVariable int friendId) {

        final boolean updated = userService.addFriend(userId, friendId);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "/{userId}/friends")
    public ResponseEntity<Collection<User>> readAllFriends(@PathVariable int userId) {

        final Collection<User> friends = userService.readAllFriends(userId);

        return friends != null && !friends.isEmpty()
                ? new ResponseEntity<>(friends, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{userId}/friends/common/{otherId}")
    public ResponseEntity<Collection<User>> findCommonFriends(@PathVariable int userId,
                                                              @PathVariable int otherId) {

        final Collection<User> commonFriends = userService.findCommonFriends(userId, otherId);
        log.debug("Текущее количество друзей: {}", commonFriends.size());

        return commonFriends != null && !commonFriends.isEmpty()
                ? new ResponseEntity<>(commonFriends, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{userId}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend (@PathVariable int userId,
                                           @PathVariable int friendId) {

        final boolean deleted = userService.deleteFriend(userId, friendId);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }
}

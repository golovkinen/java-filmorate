package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService implements UserServiceInterface {

    private static final Map<Integer, User> USERS_MAP = new HashMap<>();
    private static final AtomicInteger CLIENT_ID_HOLDER = new AtomicInteger();

    @Override
    public User create(User user) {
        final int userId = CLIENT_ID_HOLDER.incrementAndGet();
        user.setId(userId);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        USERS_MAP.put(userId, user);
        return user;
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(USERS_MAP.values());
    }

    @Override
    public User read(int id) {
        return USERS_MAP.get(id);
    }

    @Override
    public boolean update(User user, int id) {

        if (user.getId() == id && USERS_MAP.containsKey(id)) {
            USERS_MAP.put(id, user);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        return USERS_MAP.remove(id) != null;
    }
}

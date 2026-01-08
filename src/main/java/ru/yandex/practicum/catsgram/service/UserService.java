package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        if (isDuplicateEmail(user.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);

        return user;
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (!users.containsKey(newUser.getId())) {
            throw new ConditionsNotMetException("Пользователь с id = " + newUser.getId() + " не найден");
        }

        User oldUser = users.get(newUser.getId());

        if (!oldUser.getEmail().equals(newUser.getEmail()) && isDuplicateEmail(newUser.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        updateUserFields(oldUser, newUser);

        return oldUser;
    }

    //Метод обновления полей пользователя
    private void updateUserFields(User oldUser, User newUser) {
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }

        if (newUser.getUsername() != null) {
            oldUser.setUsername(newUser.getUsername());
        }

        if (newUser.getPassword() != null) {
            oldUser.setPassword(newUser.getPassword());
        }
    }

    //Метод для проверки дубликатов имейла
    private boolean isDuplicateEmail(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public Optional<User> findUserById(Long id) {
        User user = users.get(id);
        return Optional.ofNullable(user);
    }

    private long getNextId() {
        long currentMaxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }
}

package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers(){
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail().isBlank()) {
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

    @PutMapping
    public User update(@RequestBody User updUser) {
        if (updUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (!users.containsKey(updUser.getId())) {
            throw new ConditionsNotMetException("Пользователь с id = " + updUser.getId() + " не найден");
        }

        User oldUser = users.get(updUser.getId());

        if (!oldUser.getEmail().equals(updUser.getEmail())
                    && isDuplicateEmail(updUser.getEmail())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
        }

        updateUserFields(oldUser, updUser);

        return oldUser;
    }

    private void updateUserFields(User target, User source) {
        if (source.getEmail() != null) {
            target.setEmail(source.getEmail());
        }

        if (source.getUsername() != null) {
            target.setUsername(source.getUsername());
        }

        if (source.getPassword() != null) {
            target.setPassword(source.getPassword());
        }
    }

    private boolean isDuplicateEmail(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}

package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public User getUserById(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new ConditionsNotMetException("пользователь с id = " + id + " не найден"));
    }

    public Collection<User> findAllUsers() {
        return users.values();
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (isEmailTaken(user.getEmail())) {
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
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        checkEmailDuplicate(newUser, oldUser);
        updateUserFields(oldUser, newUser);
        return oldUser;
    }

    private void checkEmailDuplicate(User newUser, User oldUser) {
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()){
            if(!oldUser.getEmail().equalsIgnoreCase(newUser.getEmail())){
                if(isEmailTaken(newUser.getEmail())){
                    throw new DuplicatedDataException("Имейл " + newUser.getEmail() + " уже занят");
                }
            }
        }
    }

    private boolean isEmailTaken(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    //Метод обновления полей пользователя
    private void updateUserFields(User oldUser, User newUser) {
        if(newUser.getEmail() != null && !newUser.getEmail().isBlank()){
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getUsername() != null && !newUser.getUsername().isBlank()) {
            oldUser.setUsername(newUser.getUsername());
        }
        if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
            oldUser.setPassword(newUser.getPassword());
        }
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

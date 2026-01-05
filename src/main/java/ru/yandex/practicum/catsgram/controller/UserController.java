package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    public User create(@RequestBody User user){
        if(user.getEmail().isBlank()){
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        return user;
    }


}

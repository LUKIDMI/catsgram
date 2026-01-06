package ru.yandex.practicum.catsgram.model;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "email")
public class User {
    Long id;
    String username;
    @NotNull(message = "Имейл должен быть указан")
    String email;
    String password;
    Instant registrationDate;
}


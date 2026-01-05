package ru.yandex.practicum.catsgram.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "email")
public class User {
    Long id;
    String userName;
    @NonNull
    String email;
    String password;
    Instant registrationDate;
}

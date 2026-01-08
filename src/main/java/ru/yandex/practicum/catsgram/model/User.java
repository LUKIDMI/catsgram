package ru.yandex.practicum.catsgram.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "email")
@Component
public class User {
    Long id;
    String username;
    String email;
    String password;
    Instant registrationDate;
}


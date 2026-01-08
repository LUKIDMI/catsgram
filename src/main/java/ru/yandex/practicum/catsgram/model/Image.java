package ru.yandex.practicum.catsgram.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Component
public class Image {
    Long id;
    long postId;
    String originalFileName;
    String filePath;
}

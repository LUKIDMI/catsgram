package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.enums.SortOrder;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Post getPostById(Long id) {
        return Optional.ofNullable(posts.get(id))
                .orElseThrow(() -> new ConditionsNotMetException("Пост с id " + id + " не найден"));
    }

    public Collection<Post> findAll(String sort, int from, int size) {
        if(size < 0){
            throw new ConditionsNotMetException("Количество постов не может быть меньше нуля");
        }

        List<Post> sortedPosts = posts.values()
                .stream()
                .sorted(Comparator.comparing(Post::getPostDate))
                .collect(Collectors.toList());

        SortOrder sortOrder = SortOrder.from(sort);
        if(sortOrder == null){
            sortOrder = SortOrder.ASCENDING;
        }

        switch(sortOrder){
            case ASCENDING -> {
                return sortedPosts
                        .stream()
                        .skip(from)
                        .limit(size)
                        .collect(Collectors.toList());
            }
            case DESCENDING -> {
                Collections.reverse(sortedPosts);
                return sortedPosts
                        .stream()
                        .skip(from)
                        .limit(size)
                        .collect(Collectors.toList());
            }
        }

        return posts.values();
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        long authorId = post.getAuthorId();
        userService.findUserById(authorId).orElseThrow(()
                -> new ConditionsNotMetException("Автора с id " + authorId + " не найден."));

        post.setId(idGenerator.incrementAndGet());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;

    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }
}
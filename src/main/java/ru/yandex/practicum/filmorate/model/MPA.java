package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
public class MPA {
    @NonNull
    private Integer id;
    private String name;
}

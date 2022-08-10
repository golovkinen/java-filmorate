package ru.yandex.practicum.filmorate.model;


import lombok.*;

@Data
@Getter
@Setter
@Builder
public class Genre {
    private Integer id;
    private String name;

}

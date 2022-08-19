package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.annotation.AfterDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {

    private Integer id;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @Size (max = 200)
    private String description;
    @NonNull
    @AfterDate("1895-12-28")
    private LocalDate releaseDate;
    @NonNull
    @PositiveOrZero
    private Integer duration;

    private Set<Director> directors;
    private Set<Genre> genres;
    @NonNull
    private MPA mpa;
}

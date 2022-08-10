package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.PresentOrAfter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
    @PresentOrAfter("1895-12-28")
    private LocalDate releaseDate;
    @NonNull
    @PositiveOrZero
    private Integer duration;

    private List<Genre> genres;
    @NonNull
    private MPA mpa;
}

package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.annotation.PresentOrAfter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
//@AllArgsConstructor
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
    @JsonIgnore
    private Set<Integer> likes = new HashSet<>();

}

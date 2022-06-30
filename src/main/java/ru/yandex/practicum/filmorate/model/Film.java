package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
//@AllArgsConstructor
public class Film {

    private Integer id;
    @NotBlank
    @NonNull
    private String name;
    @Size (max = 200)
    @NotNull
    @NonNull
    private String description;
    @PastOrPresent
    @NonNull
    private LocalDate releaseDate;
    @PositiveOrZero
    @NonNull
    private int duration;


}

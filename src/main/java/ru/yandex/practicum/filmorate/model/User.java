package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data

public class User {

    private Integer id;
    @NotBlank
    @Email (message = "Please enter a valid e-mail address")
    @NonNull
    private String email;
    @NotBlank
    @NonNull
    @Pattern(regexp = "\\w+\\S", message = "Поле не должно содержать пробелов")
    private String login;
    @NonNull
    private String name;
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @NonNull
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friends = new HashSet<>();
}

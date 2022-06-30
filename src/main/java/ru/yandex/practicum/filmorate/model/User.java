package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
//@AllArgsConstructor
//@RequiredArgsConstructor


public class User {

    private Integer id;
    @NotBlank
    @Email (message = "Please enter a valid e-mail address")
    @NonNull
    private String email;
    @NotBlank
    @NonNull
    @Pattern(regexp = ".*\\S.*", message = "Поле не должно содержать пробелов")
    private String login;
    @NotNull
    @NonNull
    private String name;
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NonNull
    private LocalDate birthday;



}

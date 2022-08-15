package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;


public interface IMPARepository {

    Optional<MPA> read (int id);

    List<MPA> readAll();
}

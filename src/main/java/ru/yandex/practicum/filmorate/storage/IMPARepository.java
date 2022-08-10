package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;


public interface IMPARepository {

    MPA read (int id);

    List<MPA> readAll();
}

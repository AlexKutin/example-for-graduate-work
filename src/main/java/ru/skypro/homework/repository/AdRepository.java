package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.model.Ad;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Integer> {

    List<Ad> getAllByAuthor_Username(String username);
}

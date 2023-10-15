package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.AdUser;

public interface AdUserRepository extends JpaRepository<AdUser, Integer> {

    AdUser findByUsername(String username);
}

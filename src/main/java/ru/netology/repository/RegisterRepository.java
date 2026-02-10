package ru.netology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.model.Person;

import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByLogin(String login);
}

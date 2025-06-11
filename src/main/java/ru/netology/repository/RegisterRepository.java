package ru.netology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.model.Person;

@Repository
public interface RegisterRepository extends JpaRepository<Person, Integer> {
    Person findByUsername(String username);
}

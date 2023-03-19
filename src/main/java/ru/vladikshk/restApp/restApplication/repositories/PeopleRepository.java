package ru.vladikshk.restApp.restApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vladikshk.restApp.restApplication.models.Person;


@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
}

package ru.vladikshk.restApp.restApplication.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vladikshk.restApp.restApplication.models.Person;
import ru.vladikshk.restApp.restApplication.repositories.PeopleRepository;
import ru.vladikshk.restApp.restApplication.util.PersonNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElseThrow(PersonNotFoundException::new);
    }


    @Transactional
    public void save(Person person) {
        enrichPerson(person);
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPeron) {
        updatedPeron.setId(id);
        peopleRepository.save(updatedPeron);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public void enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setCreatedWho("ADMIN");
    }
}

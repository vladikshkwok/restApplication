package ru.vladikshk.restApp.restApplication.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.vladikshk.restApp.restApplication.dto.PersonDTO;
import ru.vladikshk.restApp.restApplication.models.Person;
import ru.vladikshk.restApp.restApplication.services.PeopleService;
import ru.vladikshk.restApp.restApplication.util.PersonErrorResponse;
import ru.vladikshk.restApp.restApplication.util.PersonNotCreatedException;
import ru.vladikshk.restApp.restApplication.util.PersonNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PeopleService peopleService,
                            ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    private List<PersonDTO> getPeople() {
        return peopleService.findAll().stream()
                .map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    private PersonDTO getPerson(@PathVariable("id") int id) {
        return convertToPersonDTO(peopleService.findOne(id));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createPerson(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(error -> errorMsg.append(error.getField())
                    .append("-").append(error.getDefaultMessage()).append(";"));

            throw new PersonNotCreatedException(errorMsg.toString());
        }



        peopleService.save(convertToPerson(personDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }



    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Человек с таким id не найден",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }



}

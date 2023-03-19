package ru.vladikshk.restApp.restApplication.util;

public class PersonNotCreatedException extends RuntimeException {
    public PersonNotCreatedException(String msg) {
        super(msg);
    }
}

package org.springframework.data.examples.boot.neo4j.service;

import org.springframework.data.examples.boot.neo4j.domain.Person;

import java.util.Optional;

/**
 * @author Thomas Woehlke
 */
public interface PersonService {

    Person findByName(String name);

    Iterable<Person> findAll();

    Optional<Person> findById(Long id);

    Person save(Person entity);

    void deleteAll();
}

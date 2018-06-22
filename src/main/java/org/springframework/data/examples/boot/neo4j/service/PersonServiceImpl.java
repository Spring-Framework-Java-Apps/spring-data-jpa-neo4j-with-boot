package org.springframework.data.examples.boot.neo4j.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.boot.neo4j.domain.Person;
import org.springframework.data.examples.boot.neo4j.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@Transactional("neo4jTransactionManager")
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person findByName(String name) {
        return personRepository.findByName(name);
    }

    @Override
    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }

    @Override
    /*
    @Transactional(
        propagation=REQUIRED,
        readOnly=false,
        transactionManager="neo4jTransactionManager"
    )
    */
    public Person save(Person entity) {
        return personRepository.save(entity);
    }

    @Override
    public void deleteAll() {
        personRepository.deleteAll();
    }
}

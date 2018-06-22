package org.springframework.data.examples.boot.neo4j.repository;

import org.springframework.data.examples.boot.neo4j.domain.Person;
//import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * @author Mark Angrish
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    Person findByName(@NonNull String name);
}

package org.neo4j.examples.sdjpasdn.neo4j.repository;

import org.neo4j.examples.sdjpasdn.neo4j.domain.Person;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Created by markangrish on 27/10/2016.
 */
public interface PersonRepository extends GraphRepository<Person> {

    Person findByName(String name);
}

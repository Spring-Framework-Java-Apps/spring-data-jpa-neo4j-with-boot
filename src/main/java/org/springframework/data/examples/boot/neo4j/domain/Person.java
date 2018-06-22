package org.springframework.data.examples.boot.neo4j.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.neo4j.ogm.annotation.*;

/**
 * @author Mark Angrish
 */
@NodeEntity
public class Person {

    @Id
    @GeneratedValue
	private Long id;

    @Property
	private String name;

	@Relationship
	private Set<Person> teamMate;

	private Person() {
	}

	public Person(String name) {
		this.name = name;
	}


	public void worksWith(Person person) {
		if (teamMate == null) {
            teamMate = new HashSet<>();
		}
        teamMate.add(person);
	}

	@Override
	public String toString() {

		return  "Person: " +  this.name + "'s teammates => "
				+ Optional.ofNullable(this.teamMate).orElse(
				Collections.emptySet()).stream().map(
				Person::getName).collect(Collectors.toList());
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Person> getTeamMate() {
        return teamMate;
    }

    public void setTeamMate(Set<Person> teamMate) {
        this.teamMate = teamMate;
    }
}

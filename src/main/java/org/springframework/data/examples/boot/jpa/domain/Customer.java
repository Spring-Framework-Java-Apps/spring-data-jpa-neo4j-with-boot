package org.springframework.data.examples.boot.jpa.domain;

import org.springframework.data.examples.boot.neo4j.domain.Person;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

/**
 * @author Mark Angrish
 */
@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String firstName;

	private String lastName;

    @OneToMany(cascade=ALL,fetch=EAGER)
    @JoinColumn(name="teamMate_id", nullable=true,referencedColumnName="id")
    private Set<Customer> teamMate;

    public void worksWith(Customer customer) {
        if (teamMate == null) {
            teamMate = new HashSet<>();
        }
        teamMate.add(customer);
    }

	protected Customer() {
	}

	public Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

    @Override
    public String toString() {

        return "Customer: " + getName() + "'s teammates => "
            + Optional.ofNullable(this.teamMate).orElse(
            Collections.emptySet()).stream().map(
            Customer::getName).collect(Collectors.toList());
    }

    @Transient
    public String getName(){
        return this.firstName + " " + this.lastName;
    }
}

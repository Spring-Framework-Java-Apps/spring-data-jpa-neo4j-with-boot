package org.springframework.data.examples.boot.jpa.repository;

import java.util.List;

import org.springframework.data.examples.boot.jpa.domain.Customer;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Mark Angrish
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	List<Customer> findByLastName(String lastName);
}

package org.springframework.data.examples.boot.jpa.service;

import org.springframework.data.examples.boot.jpa.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> findByLastName(String lastName);

    Iterable<Customer> findAll();

    Optional<Customer> findById(Long id);

    Customer save(Customer entity);
}

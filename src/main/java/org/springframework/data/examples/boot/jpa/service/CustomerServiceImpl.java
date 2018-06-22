package org.springframework.data.examples.boot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.boot.jpa.domain.Customer;
import org.springframework.data.examples.boot.jpa.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;


/**
 * @author Thomas Woehlke
 */
@Service
@Transactional("jpaTransactionManager")
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findByLastName(String lastName) {
        return this.customerRepository.findByLastName(lastName);
    }

    @Override
    public Iterable<Customer> findAll() {
        return this.customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return this.customerRepository.findById(id);
    }

    @Override
    public Customer save(Customer entity) {
        return this.customerRepository.save(entity);
    }

    @Override
    public void deleteAll() {
        this.customerRepository.deleteAll();
    }
}

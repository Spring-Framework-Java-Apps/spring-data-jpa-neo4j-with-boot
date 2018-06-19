package org.springframework.data.examples.boot;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.examples.boot.jpa.domain.Customer;
import org.springframework.data.examples.boot.jpa.service.CustomerService;
import org.springframework.data.examples.boot.neo4j.domain.Person;
import org.springframework.data.examples.boot.neo4j.service.PersonService;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;


/**
 * @author Mark Angrish
 */
@SpringBootApplication(exclude = Neo4jDataAutoConfiguration.class)
public class Application {

	private static final Log LOGGER = LogFactory.getLog(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner demo(

	    CustomerService customerService,
        @Qualifier("jpaTransactionManager") JpaTransactionManager jpaTransactionManager,
        PersonService personService,
        @Qualifier("neo4jTransactionManager") Neo4jTransactionManager neo4jTransactionManager,
        @Qualifier("jpaTransactionManager") PlatformTransactionManager transactionManager
    ) {
		return (args) -> {

			LOGGER.info(transactionManager.getClass().getName());

			// save a couple of customers
			TransactionTemplate jpaTransactionTemplate = new TransactionTemplate(jpaTransactionManager);
			jpaTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
                    customerService.save(new Customer("Jack", "Bauer"));
                    customerService.save(new Customer("Chloe", "O'Brian"));
                    customerService.save(new Customer("Kim", "Bauer"));
                    customerService.save(new Customer("David", "Palmer"));
                    customerService.save(new Customer("Michelle", "Dessler"));
				}
			});

			TransactionTemplate neo4jTransactionTemplate = new TransactionTemplate(neo4jTransactionManager);
			neo4jTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					// also save them as people
                    personService.save(new Person("Jack Bauer"));
                    personService.save(new Person("Chloe O'Brian"));
                    personService.save(new Person("Kim Bauer"));
                    personService.save(new Person("David Palmer"));
                    personService.save(new Person("Michelle Dessler"));
				}
			});

			// fetch all customers
			LOGGER.info("Customers found with findAll():");
			LOGGER.info("-------------------------------");
			Iterable<Customer> customers = jpaTransactionTemplate.execute(status -> customerService.findAll());
			for (Customer customer : customers) {
				LOGGER.info(customer.toString());
			}
			LOGGER.info("");

			// fetch all people
			LOGGER.info("People found with findAll():");
			LOGGER.info("-------------------------------");
			Iterable<Person> people = neo4jTransactionTemplate.execute(status -> personService.findAll());

			for (Person person : people) {
				LOGGER.info(person.toString());
			}
			LOGGER.info("");

			// fetch an individual customer by ID
			Optional<Customer> customer = customerService.findById(1L);
			LOGGER.info("Customer found with findOne(1L):");
			LOGGER.info("--------------------------------");
			LOGGER.info(customer.toString());
			LOGGER.info("");

			// fetch an individual person by ID
			Optional<Person> person = personService.findById(1L);
			LOGGER.info("Person found with findOne(1L):");
			LOGGER.info("--------------------------------");
			LOGGER.info(customer.toString());
			LOGGER.info("");

			// fetch customers by last name
			LOGGER.info("Customer found with findByLastName('Bauer'):");
			LOGGER.info("--------------------------------------------");
			for (Customer bauer : customerService.findByLastName("Bauer")) {
				LOGGER.info(bauer.toString());
			}
			LOGGER.info("");

			// fetch person by their name
			LOGGER.info("Customer found with findByLastName('Bauer'):");
			LOGGER.info("--------------------------------------------");
			Person jackBauer = personService.findByName("Jack Bauer");
			LOGGER.info(jackBauer.toString());
			LOGGER.info("");

		};

	}
}

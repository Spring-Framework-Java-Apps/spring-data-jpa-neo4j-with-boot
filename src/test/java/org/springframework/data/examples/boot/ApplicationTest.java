package org.springframework.data.examples.boot;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.examples.boot.config.ConfigurationProduction;
import org.springframework.data.examples.boot.jpa.domain.Customer;
import org.springframework.data.examples.boot.jpa.service.CustomerService;
import org.springframework.data.examples.boot.neo4j.domain.Person;
import org.springframework.data.examples.boot.neo4j.service.PersonService;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = {
        Application.class,
        ConfigurationProduction.class
    },
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest {

    private static final Log LOGGER = LogFactory.getLog(ApplicationTest.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    @Qualifier("jpaTransactionManager")
    private JpaTransactionManager jpaTransactionManager;

    @Autowired
    @Qualifier("neo4jTransactionManager")
    private  Neo4jTransactionManager neo4jTransactionManager;

    @Autowired
    private PersonService personService;

    @Autowired
    @Qualifier("transactionManager")
    private  PlatformTransactionManager transactionManager;

    //@Commit
    @Test
    public void runApplicationTest() throws Exception {
        LOGGER.info("----------------------------------------------------------");
        LOGGER.info(" JpaTransactionManager jpaTransactionManager = ");
        LOGGER.info(jpaTransactionManager.getClass().getName());
        LOGGER.info(" Neo4jTransactionManager neo4jTransactionManager  = ");
        LOGGER.info(neo4jTransactionManager.getClass().getName());
        LOGGER.info(" PlatformTransactionManager transactionManager = ");
        LOGGER.info(transactionManager.getClass().getName());
        LOGGER.info("----------------------------------------------------------");
        TransactionTemplate jpaTransactionTemplate = new TransactionTemplate(jpaTransactionManager);
        jpaTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
            LOGGER.info(" save a couple of customers: ");
            LOGGER.info("----------------------------------------------------------");
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
            LOGGER.info(" also save them as people: ");
            LOGGER.info("----------------------------------------------------------");
            personService.save(new Person("Jack Bauer"));
            personService.save(new Person("Chloe O'Brian"));
            personService.save(new Person("Kim Bauer"));
            personService.save(new Person("David Palmer"));
            personService.save(new Person("Michelle Dessler"));
            }
        });
        LOGGER.info(" fetch all customers ");
        LOGGER.info("");
       LOGGER.info(" Customers found with findAll():");
        LOGGER.info("----------------------------------------------------------");
        Iterable<Customer> customers = jpaTransactionTemplate.execute(status -> customerService.findAll());
        for (Customer customer : customers) {
            LOGGER.info(customer.toString());
        }
        LOGGER.info("");
        LOGGER.info(" fetch all people ");
        LOGGER.info("");
        LOGGER.info(" People found with findAll():");
        LOGGER.info("----------------------------------------------------------");
        Iterable<Person> people = neo4jTransactionTemplate.execute(status -> personService.findAll());
        for (Person person : people) {
            LOGGER.info(person.toString());
        }
        LOGGER.info("");
        LOGGER.info(" fetch an individual customer by ID");
        LOGGER.info("");
        Optional<Customer> customer = customerService.findById(1L);
        LOGGER.info(" Customer found with findOne(1L):");
        LOGGER.info("----------------------------------------------------------");
        LOGGER.info(customer.toString());
        LOGGER.info("");
        LOGGER.info(" fetch an individual person by ID");
        LOGGER.info("");
        Optional<Person> person = personService.findById(1L);
        LOGGER.info(" Person found with findOne(1L):");
        LOGGER.info("----------------------------------------------------------");
        LOGGER.info(person.toString());
        LOGGER.info("");
        LOGGER.info(" fetch customers by last name");
        LOGGER.info("");
        LOGGER.info(" Customer found with findByLastName('Bauer'):");
        LOGGER.info("----------------------------------------------------------");
        for (Customer bauer : customerService.findByLastName("Bauer")) {
            LOGGER.info(bauer.toString());
        }
        LOGGER.info("");
        LOGGER.info(" fetch person by their name");
        LOGGER.info("");
        LOGGER.info(" Customer found with findByLastName('Bauer'):");
        LOGGER.info("----------------------------------------------------------");
        Person jackBauer = personService.findByName("Jack Bauer");
        LOGGER.info(jackBauer.toString());
        LOGGER.info("");
    }

}

package org.springframework.data.examples.boot.config;

import javax.persistence.EntityManagerFactory;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.driver.Driver;
import org.neo4j.ogm.drivers.bolt.driver.BoltDriver;
import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;

/**
 * @author Mark Angrish
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "org.springframework.data.examples.boot.neo4j.repository", transactionManagerRef = "neo4jTransactionManager")
@EnableJpaRepositories(basePackages = "org.springframework.data.examples.boot.jpa.repository", transactionManagerRef = "jpaTransactionManager")
@EnableTransactionManagement
public class Neo4jConfiguration {

	@Value("spring.data.neo4j.driverClassName")
	private String neo4jDriverClassname;

	@Bean
	public Driver driver(){
		Driver driver;
		switch (neo4jDriverClassname){
			case "org.neo4j.ogm.drivers.bolt.driver.BoltDriver":
				driver = new BoltDriver();
				break;
			default:
				File db = new File("target/var/graphDb" );
				GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( db );
				driver = new EmbeddedDriver(graphDb);
				break;
		}
		if(driver != null){
			if(driver.getConfiguration() != null){
				LOGGER.info("---------------------------------------------------------------------------");
				LOGGER.info("getDriverClassName:    "+driver.getConfiguration().getDriverClassName());
				LOGGER.info("getURI:                "+driver.getConfiguration().getURI());
				LOGGER.info("getTrustStrategy:      "+driver.getConfiguration().getTrustStrategy());
				LOGGER.info("getAutoIndex:          "+driver.getConfiguration().getAutoIndex().getName());
				LOGGER.info("getDumpDir:            "+driver.getConfiguration().getDumpDir());
				LOGGER.info("getDumpFilename:       "+driver.getConfiguration().getDumpFilename());
				//LOGGER.info("getCredentials:        "+driver.getConfiguration().getCredentials().credentials().toString());
				LOGGER.info("getEncryptionLevel:    "+driver.getConfiguration().getEncryptionLevel());
				LOGGER.info("getConnectionPoolSize: "+driver.getConfiguration().getConnectionPoolSize());
				LOGGER.info("getVerifyConnection:   "+driver.getConfiguration().getVerifyConnection());
				LOGGER.info("---------------------------------------------------------------------------");
			}
		}
		return driver;
	}

	@Bean
	public SessionFactory sessionFactory() {
		return new SessionFactory(driver(), "org.springframework.data.examples.boot.neo4j.domain");
	}

	@Bean
	public Neo4jTransactionManager neo4jTransactionManager(SessionFactory sessionFactory) {
		return new Neo4jTransactionManager(sessionFactory);
	}

	@Bean
	public JpaTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}



	private static final Logger LOGGER = LoggerFactory.getLogger(Neo4jConfiguration.class);
}

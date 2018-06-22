package org.springframework.data.examples.boot.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.ogm.driver.Driver;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.examples.boot.config.helper.ConfigurationLogger;
import org.springframework.data.examples.boot.config.helper.MyApplicationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.io.File;


/**
 * @author Mark Angrish, Thomas Woehlke
 */
@Configuration
@Profile("development")
@EnableNeo4jRepositories(
	basePackages = "org.springframework.data.examples.boot.neo4j.repository",
	transactionManagerRef = "neo4jTransactionManager"
)
@EnableJpaRepositories(
	basePackages = "org.springframework.data.examples.boot.jpa.repository",
	transactionManagerRef = "jpaTransactionManager"
)
@EnableTransactionManagement
public class ConfigurationDevelopment {

    private final String packages[] = { "org.springframework.data.examples.boot.neo4j.domain" };

	private final String graphDbFileName  = "target/var/graphDb";

	@Autowired
	private ConfigurationLogger configurationLogger;

    @Autowired
	private MyApplicationProperties myApplicationProperties;

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {
        myApplicationProperties.log();
        LOGGER.debug("   Neo4J Driver Configuration = Embedded : " + this.graphDbFileName + " ");
        LOGGER.debug("-------------------------------------------------------------");
        File db = new File( graphDbFileName );
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( db );
        Driver driver = new EmbeddedDriver(graphDb);
        if (driver == null) {
            LOGGER.error("");
            LOGGER.error("-------------------------------------------------------------");
            LOGGER.error("   driver == null                                            ");
            LOGGER.error("-------------------------------------------------------------");
            LOGGER.error("");
            return null;
        } else {
            org.neo4j.ogm.config.Configuration configuration = driver.getConfiguration();
            configurationLogger.configurationLogger(configuration);
            return configuration;
        }
    }

    @Required
    @Bean
	public SessionFactory sessionFactory(org.neo4j.ogm.config.Configuration configuration ) {
        return new SessionFactory(configuration,packages);
	}

    @Bean("jpaTransactionManager")
    public JpaTransactionManager jpaTransactionManager(
        EntityManagerFactory emf
    ){
        LOGGER.info("Initializing JpaTransactionManager from EntityManagerFactory");
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(emf);
        return jpaTransactionManager;
    }

    @Bean("neo4jTransactionManager")
    public Neo4jTransactionManager neo4jTransactionManager(
        @Qualifier("sessionFactory")  SessionFactory sessionFactory
    ){
        LOGGER.info("Initializing Neo4jTransactionManager fromm SessionFactory");
        Neo4jTransactionManager neo4jTransactionManager = new Neo4jTransactionManager(sessionFactory);
        return neo4jTransactionManager;
    }

	private static final Log LOGGER = LogFactory.getLog(ConfigurationDevelopment.class);
}

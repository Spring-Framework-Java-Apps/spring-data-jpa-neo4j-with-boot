package org.springframework.data.examples.boot.config;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.driver.Driver;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;


/**
 * @author Mark Angrish
 */
@Configuration
@Profile("production")
@EnableNeo4jRepositories(
	basePackages = "org.springframework.data.examples.boot.neo4j.repository",
	transactionManagerRef = "neo4jTransactionManager"
)
@EnableJpaRepositories(
	basePackages = "org.springframework.data.examples.boot.jpa.repository",
	transactionManagerRef = "jpaTransactionManager"
)
@EnableTransactionManagement
public class ConfigurationProduction {

    private final String packages[] = {
        "org.springframework.data.examples.boot.neo4j.domain"
    };

	@Nullable
	@Value("${spring.data.neo4j.URI}")
	private String neo4jUri;

	@NonNull
	@Value("${spring.profiles}")
	private String springProfile;

	/*
    @NonNull
    @Value("${spring.profiles.active}")
    private String springProfileActive;
    */

    @NonNull
    @Value("${spring.data.neo4j.username}")
    private String username="neo4j";

    @NonNull
    @Value("${spring.data.neo4j.password}")
    private String password="secret";

    //@Nullable
    //@Value("${spring.data.neo4j.indexes.auto")
    //private String autoIndex = "dump";

    @NonNull
    @Value("${spring.data.neo4j.indexes.auto.dump.dir}")
    private String generatedIndexesOutputDir="target";

    @NonNull
    @Value("${spring.data.neo4j.indexes.auto.dump.filename}")
    private String generatedIndexesOutputFilename="neo4j_indexes.cypher";

    @NonNull
    @Value("${spring.data.neo4j.verify.connection}")
    private Boolean verifyConnection = false;

    @Nullable
    @Value("${spring.data.neo4j.encryption.level}")
    private String encryptionLevel = "NONE";

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {
        LOGGER.debug("-------------------------------------------------------------");
        LOGGER.debug("   Neo4J Driver Configuration                                ");
        LOGGER.debug("-------------------------------------------------------------");
        LOGGER.debug("   spring.data.neo4j.URI =      " + this.neo4jUri + "        ");
        LOGGER.debug("   spring.profiles =            " + this.springProfile + "   ");
        LOGGER.debug("   spring.data.neo4j.username = " + this.username + "        ");
        LOGGER.debug("   spring.data.neo4j.password = " + this.password + "        ");
        LOGGER.debug("-------------------------------------------------------------");
        LOGGER.debug("   Neo4J Driver Configuration =  bolt                        ");
        LOGGER.debug("-------------------------------------------------------------");
        org.neo4j.ogm.config.Configuration configuration =
            new org.neo4j.ogm.config.Configuration.Builder()
                .uri(this.neo4jUri)
                .credentials(this.username,this.password)
                .encryptionLevel(this.encryptionLevel)
                //.autoIndex(this.autoIndex)
                .generatedIndexesOutputDir(this.generatedIndexesOutputDir)
                .generatedIndexesOutputFilename(this.generatedIndexesOutputFilename)
                .verifyConnection(this.verifyConnection)
                .build();
        configurationLogger.configurationLogger(configuration);
        return configuration;
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

    /*
    @Bean("platformTransactionManager")
    public PlatformTransactionManager platformTransactionManager(
        JpaTransactionManager jpaTransactionManager,
        Neo4jTransactionManager neo4jTransactionManager
    ){

        LOGGER.info("Initializing Neo4jTransactionManager fromm SessionFactory");
        Neo4jTransactionManager neo4jTransactionManager = new Neo4jTransactionManager(sessionFactory);
        LOGGER.info("Initializing JpaTransactionManager from EntityManagerFactory");
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(emf);
        LOGGER.info("Initializing platform transaction manager as ChainedTransactionManager");

        return new ChainedTransactionManager(jpaTransactionManager, neo4jTransactionManager);
    }
    */



    @Autowired
    private  ConfigurationLogger configurationLogger;

	private static final Log LOGGER = LogFactory.getLog(ConfigurationProduction.class);
}

package org.springframework.data.examples.boot.config;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.examples.boot.config.helper.ConfigurationLogger;
import org.springframework.data.examples.boot.config.helper.MyApplicationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author Mark Angrish, Thomas Woehlke
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
@EnableConfigurationProperties(MyApplicationProperties.class)
public class ConfigurationProduction {

    @Autowired
    private MyApplicationProperties myApplicationProperties;


    private final String packages[] = {
        "org.springframework.data.examples.boot.neo4j.domain"
    };

    @NonNull
    @Value("${spring.data.neo4j.username}")
    private String username="neo4j";

    @NonNull
    @Value("${spring.data.neo4j.password}")
    private String password="secret";

    @NonNull
    @Value("${spring.data.neo4j.verify.connection}")
    private Boolean verifyConnection = false;

    @Nullable
    @Value("${spring.data.neo4j.encryption.level}")
    private String encryptionLevel = "NONE";

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {
        myApplicationProperties.log();
        LOGGER.debug("-------------------------------------------------------------");
        LOGGER.debug("   spring.data.neo4j.username = " + this.username + "        ");
        LOGGER.debug("   spring.data.neo4j.password = " + this.password + "        ");
        LOGGER.debug("-------------------------------------------------------------");
        LOGGER.debug("   Neo4J Driver Configuration =  bolt                        ");
        LOGGER.debug("-------------------------------------------------------------");
        org.neo4j.ogm.config.Configuration configuration =
            new org.neo4j.ogm.config.Configuration.Builder()
                .uri(this.myApplicationProperties.getNeo4jUri())
                .credentials(this.username,this.password)
                .encryptionLevel(this.encryptionLevel)
                .generatedIndexesOutputDir(myApplicationProperties.getGeneratedIndexesOutputDir())
                .generatedIndexesOutputFilename(myApplicationProperties.getGeneratedIndexesOutputFilename())
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

    @Autowired
    private ConfigurationLogger configurationLogger;

	private static final Log LOGGER = LogFactory.getLog(ConfigurationProduction.class);
}

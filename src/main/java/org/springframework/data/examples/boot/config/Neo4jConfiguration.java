package org.springframework.data.examples.boot.config;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.driver.v1.Config;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.ogm.config.AutoIndexMode;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.driver.Driver;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;


/**
 * @author Mark Angrish
 */
@Configuration
@EnableNeo4jRepositories(
	basePackages = "org.springframework.data.examples.boot.neo4j.repository",
	transactionManagerRef = "neo4jTransactionManager"
)
@EnableJpaRepositories(
	basePackages = "org.springframework.data.examples.boot.jpa.repository",
	transactionManagerRef = "jpaTransactionManager"
)
@EnableTransactionManagement
public class Neo4jConfiguration {

	private final String packages[] = { "org.springframework.data.examples.boot.neo4j.domain" };

	private final String graphDbFileName  = "target/var/graphDb";

	@Nullable
	@Value("${spring.data.neo4j.URI}")
	private String neo4jUri;

	@NonNull
	@Value("${spring.profiles.active}")
	private String springProfile;

    @Nullable
    @Value("${spring.data.neo4j.username}")
    private String username;

    @Nullable
    @Value("${spring.data.neo4j.password}")
    private String password;

    @Nullable
    @Value("${spring.data.neo4j.indexes.auto")
    private String autoIndex;

    @Nullable
    @Value("${spring.data.neo4j.indexes.auto.dump.dir}")
    private String generatedIndexesOutputDir;

    @Nullable
    @Value("${spring.data.neo4j.indexes.auto.dump.filename}")
    private String generatedIndexesOutputFilename;

    @Nullable
    @Value("${spring.data.neo4j.verify.connection}")
    private Boolean verifyConnection;

    @Nullable
    @Value("${spring.data.neo4j.encryption.level}")
    private String encryptionLevel;

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {
        LOGGER.debug("-------------------------------------------------------------");
        LOGGER.debug("   Neo4J Driver Configuration                                ");
        LOGGER.debug("-------------------------------------------------------------");
        LOGGER.debug("   spring.data.neo4j.URI = " + this.neo4jUri + "             ");
        LOGGER.debug("   spring.profiles.active = " + this.springProfile + "       ");
        LOGGER.debug("   spring.data.neo4j.username = " + this.username + "        ");
        LOGGER.debug("   spring.data.neo4j.password = " + this.password + "        ");
        LOGGER.debug("-------------------------------------------------------------");
        if (this.neo4jUri != null && this.neo4jUri.startsWith("bolt:")) {
            org.neo4j.ogm.config.Configuration configuration =
                new org.neo4j.ogm.config.Configuration.Builder()
                    .uri(this.neo4jUri)
                    .credentials(this.username,this.password)
                    .autoIndex(this.autoIndex)
                    .encryptionLevel(this.encryptionLevel)
                    //.generatedIndexesOutputDir(this.generatedIndexesOutputDir)
                    //.generatedIndexesOutputFilename(this.generatedIndexesOutputFilename)
                    .verifyConnection(this.verifyConnection)
                    .build();
            return configuration;
        } else {
            File db = new File( graphDbFileName );
            GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( db );
            Driver driver = new EmbeddedDriver(graphDb);
            driverLogger(driver);
            return driver.getConfiguration();
        }
    }

	@Bean
	public SessionFactory sessionFactory(org.neo4j.ogm.config.Configuration configuration) {
        return new SessionFactory(configuration,packages);
	}

    @Bean
    public PlatformTransactionManager transactionManager(
        SessionFactory sessionFactory,EntityManagerFactory emf){
        LOGGER.info("Initializing Neo4jTransactionManager fromm SessionFactory");
        Neo4jTransactionManager neo4jTransactionManager = new Neo4jTransactionManager(sessionFactory);
        LOGGER.info("Initializing JpaTransactionManager from EntityManagerFactory");
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(emf);
        LOGGER.info("Initializing platform transaction manager as ChainedTransactionManager");
        return new ChainedTransactionManager(jpaTransactionManager, neo4jTransactionManager);
    }

	private void driverLogger(Driver driver){
		if (driver == null) {
			LOGGER.debug("");
			LOGGER.debug("**************************************************************");
			LOGGER.debug("   driver == null                                             ");
			LOGGER.debug("**************************************************************");
			LOGGER.debug("");
		} else {
			if (driver.getConfiguration() == null) {
				LOGGER.debug("");
				LOGGER.debug("**************************************************************");
				LOGGER.debug("   driver.getConfiguration() == null                          ");
				LOGGER.debug("**************************************************************");
				LOGGER.debug("");
			} else {
				try {
					LOGGER.debug("spring.data.neo4j.DriverClassName: " + driver.getConfiguration().getDriverClassName());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.DriverClassName:  " + npe.getMessage());
				}
				try {
					LOGGER.debug("spring.data.neo4j.URI = " + driver.getConfiguration().getURI());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.URI = " + npe.getMessage());
				}
				try {
					int i = 0;
					for(String uri: driver.getConfiguration().getURIS()){
						LOGGER.debug("spring.data.neo4j.URIS ["+ ++i +"] = " +uri);
					}
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.URIS = " + npe.getMessage());
				}
				try {
					LOGGER.debug("spring.data.neo4j.trust.strategy = " + driver.getConfiguration().getTrustStrategy());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.trust.strategy = " + npe.getMessage());
				}
				try {
					LOGGER.debug("spring.data.neo4j.trust.certificate.file = " + driver.getConfiguration().getTrustCertFile());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.trust.certificate.file = " + npe.getMessage());
				}
				try {
					LOGGER.debug("spring.data.neo4j.connection.pool.size = " + driver.getConfiguration().getConnectionPoolSize());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.connection.pool.size = " + npe.getMessage());
				}
				try {
					LOGGER.debug("spring.data.neo4j.connection.liveness.check.timeout = " + driver.getConfiguration().getConnectionLivenessCheckTimeout());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.connection.liveness.check.timeout = " + npe.getMessage());
				}
				try {
					if (driver.getConfiguration().getAutoIndex() != null) {
						LOGGER.debug("spring.data.neo4j.indexes.auto.dump.dir = " + driver.getConfiguration().getAutoIndex().getName());
					} else {
						LOGGER.error("spring.data.neo4j.indexes.auto.dump.dir =  driver.getConfiguration().getAutoIndex() == null ");
					}
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.indexes.auto.dump.dir = " + npe.getMessage());
				}
				try {
					LOGGER.debug("spring.data.neo4j.indexes.auto.dump.dir = " + driver.getConfiguration().getDumpDir());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.indexes.auto.dump.dir = " + npe.getMessage());
				}
				try {
					LOGGER.debug("spring.data.neo4j.indexes.auto.dump.filename = " + driver.getConfiguration().getDumpFilename());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.indexes.auto.dump.filename = " + npe.getMessage());
				}
				try {
					if (driver.getConfiguration().getCredentials() != null) {
						if (driver.getConfiguration().getCredentials().credentials() != null) {
							try {
								LOGGER.error("spring.data.neo4j.username = " + driver.getConfiguration().getCredentials().credentials().toString());
							} catch (NullPointerException npe) {
								LOGGER.error("spring.data.neo4j.username = " + npe.getMessage());
							}
						} else {
							LOGGER.error("spring.data.neo4j.username =       driver.getConfiguration().getCredentials().credentials() == null");
						}
					} else {
						LOGGER.error("spring.data.neo4j.username =       driver.getConfiguration().getCredentials() == null");
					}
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.username = " + npe.getMessage());
				}
				try {
					LOGGER.debug("spring.data.neo4j.encryption.level = " + driver.getConfiguration().getEncryptionLevel());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.encryption.level = " + npe.getMessage());
				}
				try {
					LOGGER.debug("spring.data.neo4j.neo4j.ha.properties.file = " + driver.getConfiguration().getNeo4jHaPropertiesFile());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.neo4j.ha.properties.file = " + npe.getMessage());
				}
				try {
					LOGGER.debug("spring.data.neo4j.connection.pool.size = " + driver.getConfiguration().getConnectionPoolSize());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.connection.pool.size = " + npe.getMessage());
				}
				try {
					LOGGER.debug("spring.data.neo4j.verify.connection = " + driver.getConfiguration().getVerifyConnection());
				} catch (NullPointerException npe) {
					LOGGER.error("spring.data.neo4j.verify.connection = " + npe.getMessage());
				}
				LOGGER.debug("-------------------------------------------------------------");
			}
		}
		LOGGER.debug("-------------------------------------------------------------");
	}

	private static final Log LOGGER = LogFactory.getLog(Neo4jConfiguration.class);
}

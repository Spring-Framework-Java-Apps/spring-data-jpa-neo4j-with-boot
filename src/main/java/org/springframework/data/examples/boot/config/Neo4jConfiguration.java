package org.springframework.data.examples.boot.config;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.driver.Driver;
import org.neo4j.ogm.drivers.bolt.driver.BoltDriver;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.JpaTransactionManager;
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

	@Nullable
	@Value("spring.data.neo4j.URI")
	private String neo4jUri;


	private SessionFactory sessionFactoryFromBoltDriver(String... packages){
		Driver driver = new BoltDriver();
		//driverLogger(driver);
		SessionFactory sessionFactory = new SessionFactory(driver, packages);
		return sessionFactory;
	}

	private SessionFactory sessionFactoryFromEmbeddedDriver(String... packages) {
		File db = new File("target/var/graphDb" );
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( db );
		Driver driver = new EmbeddedDriver(graphDb);
		//driverLogger(driver);
		SessionFactory sessionFactory = new SessionFactory(driver, packages);
		return sessionFactory;
	}

	@Bean
	@ConfigurationProperties(prefix="spring.data.neo4j")
	public SessionFactory sessionFactory() {

/*
		private static final String CONNECTION_POOL_SIZE = "connection.pool.size";
		private static final String ENCRYPTION_LEVEL = "encryption.level";
		private static final String TRUST_STRATEGY = "trust.strategy";
		private static final String TRUST_CERT_FILE = "trust.certificate.file";
		private static final String CONNECTION_LIVENESS_CHECK_TIMEOUT = "connection.liveness.check.timeout";
		private static final String VERIFY_CONNECTION = "verify.connection";
		private static final String AUTO_INDEX = "indexes.auto";
		private static final String GENERATED_INDEXES_OUTPUT_DIR = "indexes.auto.dump.dir";
		private static final String GENERATED_INDEXES_OUTPUT_FILENAME = "indexes.auto.dump.filename";
		private static final String NEO4J_HA_PROPERTIES_FILE = "neo4j.ha.properties.file";

*/

		if(this.neo4jUri != null){
			if(this.neo4jUri.startsWith("bolt:")){
				return sessionFactoryFromBoltDriver(packages);
			} else {
				return sessionFactoryFromEmbeddedDriver(packages);
			}
		} else {
			return sessionFactoryFromEmbeddedDriver(packages);
		}
	}

	@Bean
	public Neo4jTransactionManager neo4jTransactionManager(SessionFactory sessionFactory) {
		return new Neo4jTransactionManager(sessionFactory);
	}

	@Bean
	public JpaTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	private void driverLogger(Driver driver){
		LOGGER.debug("---------------------------------------------------------------------------");
		LOGGER.debug("                       Neo4J Driver Configuration                          ");
		LOGGER.debug("---------------------------------------------------------------------------");
			if (driver == null) {
				LOGGER.error("                             driver == null");
			} else {
				if (driver.getConfiguration() != null) {
					LOGGER.warn("                    driver.getConfiguration() == null");
				} else {
					try {
						LOGGER.debug("getDriverClassName:    " + driver.getConfiguration().getDriverClassName());
					} catch (NullPointerException npe) {
						LOGGER.warn(npe.getMessage());
					}
					LOGGER.debug("getURI:                " + driver.getConfiguration().getURI());
					LOGGER.debug("getTrustStrategy:      " + driver.getConfiguration().getTrustStrategy());
					if (driver.getConfiguration().getAutoIndex() != null) {
						LOGGER.debug("getAutoIndex:          " + driver.getConfiguration().getAutoIndex().getName());
					} else {
						LOGGER.warn("getAutoIndex:          driver.getConfiguration().getAutoIndex() == null ");
					}
					LOGGER.debug("getDumpDir:            " + driver.getConfiguration().getDumpDir());
					LOGGER.debug("getDumpFilename:       " + driver.getConfiguration().getDumpFilename());
					if (driver.getConfiguration().getCredentials() != null) {
						if (driver.getConfiguration().getCredentials().credentials() != null) {
							LOGGER.error("getCredentials:        " + driver.getConfiguration().getCredentials().credentials().toString());
						} else {
							LOGGER.warn("getCredentials:        driver.getConfiguration().getCredentials().credentials() == null");
						}
					} else {
						LOGGER.error("getCredentials:        driver.getConfiguration().getCredentials() == null");
					}
					LOGGER.debug("getEncryptionLevel:    " + driver.getConfiguration().getEncryptionLevel());
					LOGGER.debug("getConnectionPoolSize: " + driver.getConfiguration().getConnectionPoolSize());
					LOGGER.debug("getVerifyConnection:   " + driver.getConfiguration().getVerifyConnection());
					LOGGER.debug("---------------------------------------------------------------------------");
				}
			}
			LOGGER.debug("---------------------------------------------------------------------------");
	}

	private static final Log LOGGER = LogFactory.getLog(Neo4jConfiguration.class);
}

package org.springframework.data.examples.boot.config.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.ogm.config.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Thomas Woehlke
 */
@Component
public class ConfigurationLoggerImpl implements ConfigurationLogger {

    @Override
    public void configurationLogger(Configuration configuration) {
        if (configuration == null) {
            LOGGER.error("");
            LOGGER.error("-------------------------------------------------------------");
            LOGGER.error("   configuration == null                                     ");
            LOGGER.error("-------------------------------------------------------------");
            LOGGER.error("");
        } else {
            LOGGER.debug("");
            LOGGER.debug("-------------------------------------------------------------");
            try {
                LOGGER.debug("spring.data.neo4j.DriverClassName: " + configuration.getDriverClassName());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.DriverClassName:  " + npe.getMessage());
            }
            try {
                LOGGER.debug("spring.data.neo4j.URI = " + configuration.getURI());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.URI = " + npe.getMessage());
            }
            try {
                int i = 0;
                for(String uri: configuration.getURIS()){
                    LOGGER.debug("spring.data.neo4j.URIS ["+ ++i +"] = " +uri);
                }
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.URIS = " + npe.getMessage());
            }
            try {
                LOGGER.debug("spring.data.neo4j.trust.strategy = " + configuration.getTrustStrategy());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.trust.strategy = " + npe.getMessage());
            }
            try {
                LOGGER.debug("spring.data.neo4j.trust.certificate.file = " + configuration.getTrustCertFile());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.trust.certificate.file = " + npe.getMessage());
            }
            try {
                LOGGER.debug("spring.data.neo4j.connection.pool.size = " + configuration.getConnectionPoolSize());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.connection.pool.size = " + npe.getMessage());
            }
            try {
                LOGGER.debug("spring.data.neo4j.connection.liveness.check.timeout = " + configuration.getConnectionLivenessCheckTimeout());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.connection.liveness.check.timeout = " + npe.getMessage());
            }
            try {
                if (configuration.getAutoIndex() != null) {
                    LOGGER.debug("spring.data.neo4j.indexes.auto.dump.dir = " + configuration.getAutoIndex().getName());
                } else {
                    LOGGER.error("spring.data.neo4j.indexes.auto.dump.dir =  configuration.getAutoIndex() == null ");
                }
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.indexes.auto.dump.dir = " + npe.getMessage());
            }
            try {
                LOGGER.debug("spring.data.neo4j.indexes.auto.dump.dir = " + configuration.getDumpDir());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.indexes.auto.dump.dir = " + npe.getMessage());
            }
            try {
                LOGGER.debug("spring.data.neo4j.indexes.auto.dump.filename = " + configuration.getDumpFilename());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.indexes.auto.dump.filename = " + npe.getMessage());
            }
            try {
                if (configuration.getCredentials() != null) {
                    if (configuration.getCredentials().credentials() != null) {
                        try {
                            LOGGER.error("spring.data.neo4j.username = " + configuration.getCredentials().credentials().toString());
                        } catch (NullPointerException npe) {
                            LOGGER.error("spring.data.neo4j.username = " + npe.getMessage());
                        }
                    } else {
                        LOGGER.error("spring.data.neo4j.username =       configuration.getCredentials().credentials() == null");
                    }
                } else {
                    LOGGER.error("spring.data.neo4j.username =       configuration.getCredentials() == null");
                }
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.username = " + npe.getMessage());
            }
            try {
                LOGGER.debug("spring.data.neo4j.encryption.level = " + configuration.getEncryptionLevel());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.encryption.level = " + npe.getMessage());
            }
            try {
                LOGGER.debug("spring.data.neo4j.neo4j.ha.properties.file = " + configuration.getNeo4jHaPropertiesFile());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.neo4j.ha.properties.file = " + npe.getMessage());
            }
            try {
                LOGGER.debug("spring.data.neo4j.connection.pool.size = " + configuration.getConnectionPoolSize());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.connection.pool.size = " + npe.getMessage());
            }
            try {
                LOGGER.debug("spring.data.neo4j.verify.connection = " + configuration.getVerifyConnection());
            } catch (NullPointerException npe) {
                LOGGER.error("spring.data.neo4j.verify.connection = " + npe.getMessage());
            }
            LOGGER.debug("-------------------------------------------------------------");
            LOGGER.debug("");
        }
    }

    private static final Log LOGGER = LogFactory.getLog(ConfigurationLoggerImpl.class);
}

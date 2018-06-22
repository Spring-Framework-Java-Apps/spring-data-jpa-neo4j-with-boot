package org.springframework.data.examples.boot.config;

import org.neo4j.ogm.driver.Driver;

public interface ConfigurationLogger {

    void driverLogger(Driver driver);

    void configurationLogger(org.neo4j.ogm.config.Configuration configuration);
}

package org.springframework.data.examples.boot.config.helper;

import org.neo4j.ogm.driver.Driver;

public interface ConfigurationLogger {

    void configurationLogger(org.neo4j.ogm.config.Configuration configuration);
}

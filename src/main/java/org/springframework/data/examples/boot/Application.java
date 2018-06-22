package org.springframework.data.examples.boot;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.examples.boot.process.ProcessService;


/**
 * @author Mark Angrish, Thomas Woehlke
 */
@SpringBootApplication(exclude = Neo4jDataAutoConfiguration.class)
public class Application implements ApplicationListener<ApplicationReadyEvent> {

	public static void main(String[] args) {
		SpringApplication.run(Application.class,args);
	}

    private static final Log LOGGER = LogFactory.getLog(Application.class);

	@Autowired
    private ProcessService processService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOGGER.info(" ");
        LOGGER.info(" ================================ ");
        LOGGER.info(" ===== Application Ready   ====== ");
        LOGGER.info(" ================================ ");
        LOGGER.info(" ");
        processService.runDatabaseExample();;
        SpringApplication.exit(event.getApplicationContext());
    }
}

package in.woowa.pilot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
public class PilotBatchApplication {
    public static void main(String[] args) {
        log.info("Batch Application Runs");

        new SpringApplicationBuilder(PilotBatchApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}

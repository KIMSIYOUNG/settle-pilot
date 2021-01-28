package in.woowa.pilot.batch.job;

import in.woowa.pilot.batch.dto.SettleJobDto;
import in.woowa.pilot.batch.parameter.SettleParameter;
import in.woowa.pilot.batch.processor.SettleJobProcessor;
import in.woowa.pilot.batch.query.ItemReaderQueryString;
import in.woowa.pilot.batch.wrtier.SettleJobWriter;
import in.woowa.pilot.core.owner.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class SettleConfiguration {
    public static final String JOB_NAME = "settleJob";
    private final String JOB_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final SettleParameter jobParameter;
    private final SettleJobProcessor jobProcessor;
    private final SettleJobWriter jobWriter;

    @Value("${chunk.size}")
    public int chunkSize;

    @Bean(JOB_PREFIX + "jobParameter")
    @JobScope
    public SettleParameter jobParameter() {
        return new SettleParameter();
    }

    @Bean(JOB_NAME)
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(settleStep())
                .preventRestart()
                .build();
    }

    @Bean(JOB_PREFIX + "settleStep")
    public Step settleStep() {
        return stepBuilderFactory.get("settleStep")
                .<Owner, SettleJobDto>chunk(chunkSize)
                .reader(reader())
                .processor(jobProcessor)
                .writer(jobWriter)
                .build();
    }

    @Bean(JOB_PREFIX + "settleReader")
    @StepScope
    public JpaPagingItemReader<Owner> reader() {
        Map<String, Object> params = new HashMap<>();
        params.put("settleType", jobParameter.getSettleType());

        return new JpaPagingItemReaderBuilder<Owner>()
                .name("settleReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString(ItemReaderQueryString.findActiveOwnerBySettleType())
                .parameterValues(params)
                .build();
    }
}

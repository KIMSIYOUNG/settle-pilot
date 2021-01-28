package in.woowa.pilot.batch.job;

import in.woowa.pilot.batch.parameter.SettleSumJobParameter;
import in.woowa.pilot.batch.query.ItemReaderQueryString;
import in.woowa.pilot.core.settle.SettleSum;
import in.woowa.pilot.core.settle.SettleSumRepository;
import in.woowa.pilot.core.settle.SettleType;
import jpql.dto.SettleSumDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SettleSumConfiguration {
    public static final String JOB_NAME = "settleSumJob";
    public static final String JOB_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final SettleSumJobParameter jobParameter;
    private final SettleSumRepository settleSumRepository;

    @Value("${chunk.size}")
    public int chunkSize;

    @Bean(JOB_PREFIX + "jobParameter")
    @JobScope
    public SettleSumJobParameter jobParameter() {
        return new SettleSumJobParameter();
    }

    @Bean(JOB_NAME)
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(settleSumStep())
                .preventRestart()
                .build();
    }

    @Bean(JOB_PREFIX + "ownerStep")
    @JobScope
    public Step settleSumStep() {
        return stepBuilderFactory.get("settleSumStep")
                .<SettleSumDto, SettleSum>chunk(chunkSize)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(JOB_PREFIX + "settleReader")
    @StepScope
    public JpaPagingItemReader<SettleSumDto> reader() {
        Map<String, Object> params = new HashMap<>();

        SettleType type = jobParameter.getSettleType();
        params.put("settleType", type);
        params.put("start", type.getStartCriteriaAt(jobParameter.getCriteriaDate()));
        params.put("end", type.getEndCriteriaAt(jobParameter.getCriteriaDate()));

        return new JpaPagingItemReaderBuilder<SettleSumDto>()
                .name("settleReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString(ItemReaderQueryString.findSettleByTypeAndDateTime())
                .parameterValues(params)
                .build();
    }

    private ItemProcessor<SettleSumDto, SettleSum> processor() {
        return dto -> dto.toSettleSum(jobParameter.getSettleType(), jobParameter.getCriteriaDate());
    }

    @Bean(JOB_NAME + "settleSumWriter")
    @StepScope
    public ItemWriter<SettleSum> writer() {
        return settleSumRepository::saveAll;
    }
}

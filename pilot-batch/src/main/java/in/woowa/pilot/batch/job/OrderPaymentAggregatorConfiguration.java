package in.woowa.pilot.batch.job;

import in.woowa.pilot.batch.query.ItemReaderQueryString;
import in.woowa.pilot.core.order.OrderPaymentAggregation;
import in.woowa.pilot.core.order.OrderPaymentAggregationRepository;
import in.woowa.pilot.core.settle.DateTimeUtils;
import jpql.dto.OrderAggregationByOwnerDto;
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
public class OrderPaymentAggregatorConfiguration {
    public static final String JOB_NAME = "orderPaymentTypeAggregatorJob";
    public static final String JOB_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final OrderPaymentAggregationRepository repository;

    @Value("${chunk.size}")
    public int chunkSize;

    @Bean(JOB_NAME)
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(orderAggregationByPaymentAndOwner())
                .preventRestart()
                .build();
    }

    @Bean(JOB_PREFIX + "step")
    @JobScope
    public Step orderAggregationByPaymentAndOwner() {
        return stepBuilderFactory.get("step")
                .<OrderAggregationByOwnerDto, OrderPaymentAggregation>chunk(chunkSize)
                .reader(reader(null))
                .processor(processor(null))
                .writer(writer())
                .build();
    }

    @Bean(JOB_PREFIX + "reader")
    @StepScope
    public JpaPagingItemReader<OrderAggregationByOwnerDto> reader(@Value("#{jobParameters[criteriaDate]}") String criteriaDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", DateTimeUtils.dailyStartDateTime(DateTimeUtils.toLocalDate(criteriaDate)));
        params.put("end", DateTimeUtils.dailyEndDateTime(DateTimeUtils.toLocalDate(criteriaDate)));

        return new JpaPagingItemReaderBuilder<OrderAggregationByOwnerDto>()
                .name("reader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString(ItemReaderQueryString.findOrderAggregationByOwnerAndPayment())
                .parameterValues(params)
                .build();
    }

    @Bean(JOB_PREFIX + "processor")
    @JobScope
    public ItemProcessor<OrderAggregationByOwnerDto, OrderPaymentAggregation> processor(@Value("#{jobParameters[criteriaDate]}") String criteriaDate) {
        return dto -> dto.toOrderPaymentAggregation(DateTimeUtils.toLocalDate(criteriaDate));
    }

    @Bean(JOB_PREFIX + "writer")
    @StepScope
    public ItemWriter<OrderPaymentAggregation> writer() {
        return repository::saveAll;
    }
}

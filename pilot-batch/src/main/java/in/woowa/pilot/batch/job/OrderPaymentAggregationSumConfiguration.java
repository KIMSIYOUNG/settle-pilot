package in.woowa.pilot.batch.job;

import in.woowa.pilot.batch.parameter.OrderPaymentAggregationJobParameter;
import in.woowa.pilot.batch.query.ItemReaderQueryString;
import in.woowa.pilot.core.order.OrderPaymentAggregationSum;
import in.woowa.pilot.core.order.OrderPaymentAggregationSumRepository;
import jpql.dto.OrderAggregationSumDto;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrderPaymentAggregationSumConfiguration {
    public static final String JOB_NAME = "orderPaymentAggregationSumJob";
    public static final String JOB_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final OrderPaymentAggregationSumRepository repository;
    private final OrderPaymentAggregationJobParameter jobParameter;

    private final int chunkSize = 10;

    @Bean(JOB_PREFIX + "jobParameter")
    @StepScope
    public OrderPaymentAggregationJobParameter jobParameter() {
        return new OrderPaymentAggregationJobParameter();
    }

    @Bean(JOB_NAME)
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(paymentAggregationSumStep())
                .preventRestart()
                .build();
    }

    @Bean(JOB_PREFIX + "sumStep")
    @JobScope
    public Step paymentAggregationSumStep() {
        return stepBuilderFactory.get("sumStep")
                .<OrderAggregationSumDto, OrderPaymentAggregationSum>chunk(chunkSize)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(JOB_PREFIX + "sumReader")
    @StepScope
    public JpaPagingItemReader<OrderAggregationSumDto> reader() {
        Map<String, Object> params = new HashMap<>();
        params.put("criteriaDate", jobParameter.getCriteriaDate());

        return new JpaPagingItemReaderBuilder<OrderAggregationSumDto>()
                .name("sumReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString(ItemReaderQueryString.findOrderAggregationSumByPayment())
                .parameterValues(params)
                .build();
    }

    private ItemProcessor<OrderAggregationSumDto, OrderPaymentAggregationSum> processor() {
        return (dto) -> dto.toOrderPaymentAggregationSum(jobParameter.getCriteriaDate());
    }

    @Bean(JOB_PREFIX + "sumWriter")
    @StepScope
    public ItemWriter<OrderPaymentAggregationSum> writer() {
        return repository::saveAll;
    }

}

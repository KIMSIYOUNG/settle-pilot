package in.woowa.pilot.batch.job;

import in.woowa.pilot.batch.common.BatchIntegrationTest;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.settle.SettleRepository;
import in.woowa.pilot.core.settle.SettleSum;
import in.woowa.pilot.core.settle.SettleSumRepository;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.settle.SettleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static in.woowa.pilot.fixture.TestUtils.assertThatBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SettleSumConfigurationTest extends BatchIntegrationTest {

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    SettleRepository settleRepository;

    @Autowired
    SettleSumRepository settleSumRepository;

    @DisplayName("오늘자 지급금의 총 집계결과를 반환한다.")
    @Test
    void createSumDaily() throws Exception {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));

        settleRepository.saveAll(Arrays.asList(
                SettleFixture.createWithoutId(owner, LocalDate.now(), SettleType.DAILY),
                SettleFixture.createWithoutId(owner, LocalDate.now(), SettleType.DAILY),
                SettleFixture.createWithoutId(owner1, LocalDate.now(), SettleType.DAILY),
                SettleFixture.createWithoutId(owner2, LocalDate.now(), SettleType.DAILY)
        ));
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .addString("settleType", SettleType.DAILY.name())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher(SettleSumConfiguration.JOB_NAME).launchJob(jobParameters);
        List<SettleSum> settleSums = settleSumRepository.findAll();
        SettleSum result = settleSums.get(0);
        // then
        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(settleSums).hasSize(1),
                () -> assertThat(result.getSettleType()).isEqualTo(SettleType.DAILY),
                () -> assertThat(result.getTransactionStartAt()).isEqualTo(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)),
                () -> assertThat(result.getTransactionEndAt()).isEqualTo(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT)),
                () -> assertThatBigDecimal(result.getTotalAmount()).isEqualTo(BigDecimal.valueOf(120_000))
        );
    }

    @DisplayName("주단위 지급금의 총 집계결과를 반환한다.")
    @Test
    void createSumWeek() throws Exception {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.WEEK));
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.WEEK));
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.WEEK));

        settleRepository.saveAll(Arrays.asList(
                SettleFixture.createWithoutId(owner, LocalDate.now(), SettleType.DAILY),
                SettleFixture.createWithoutId(owner, LocalDate.now(), SettleType.WEEK),
                SettleFixture.createWithoutId(owner1, LocalDate.now(), SettleType.WEEK),
                SettleFixture.createWithoutId(owner2, LocalDate.now(), SettleType.WEEK)
        ));
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .addString("settleType", SettleType.WEEK.name())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher(SettleSumConfiguration.JOB_NAME).launchJob(jobParameters);
        List<SettleSum> settleSums = settleSumRepository.findAll();
        SettleSum result = settleSums.get(0);
        // then
        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(settleSums).hasSize(1),
                () -> assertThat(result.getSettleType()).isEqualTo(SettleType.WEEK),
                () -> assertThatBigDecimal(result.getTotalAmount()).isEqualTo(BigDecimal.valueOf(90_000))
        );
    }

    @DisplayName("월단위 집계를 정상적으로 수행한다.")
    @Test
    void createSumMonth() throws Exception {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.MONTH));
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.MONTH));
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.WEEK));

        settleRepository.saveAll(Arrays.asList(
                SettleFixture.createWithoutId(owner, LocalDate.now(), SettleType.MONTH),
                SettleFixture.createWithoutId(owner, LocalDate.now(), SettleType.MONTH),
                SettleFixture.createWithoutId(owner1, LocalDate.now(), SettleType.MONTH),
                SettleFixture.createWithoutId(owner2, LocalDate.now(), SettleType.MONTH)
        ));
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .addString("settleType", SettleType.MONTH.name())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher(SettleSumConfiguration.JOB_NAME).launchJob(jobParameters);
        List<SettleSum> settleSums = settleSumRepository.findAll();
        SettleSum result = settleSums.get(0);
        // then
        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(settleSums).hasSize(1),
                () -> assertThat(result.getSettleType()).isEqualTo(SettleType.MONTH),
                () -> assertThatBigDecimal(result.getTotalAmount()).isEqualTo(BigDecimal.valueOf(90_000))
        );
    }

    @DisplayName("지급금이 없는 경우 결과를 만들고 금액은 0으로 반환한다.")
    @Test
    void createSumWithoutSettle() throws Exception {
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .addString("settleType", SettleType.MONTH.name())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher(SettleSumConfiguration.JOB_NAME).launchJob(jobParameters);
        List<SettleSum> settleSums = settleSumRepository.findAll();
        SettleSum result = settleSums.get(0);
        // then
        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(settleSums).hasSize(1),
                () -> assertThat(result.getSettleType()).isEqualTo(SettleType.MONTH),
                () -> assertThatBigDecimal(result.getTotalAmount()).isEqualTo(BigDecimal.valueOf(0))
        );
    }
}
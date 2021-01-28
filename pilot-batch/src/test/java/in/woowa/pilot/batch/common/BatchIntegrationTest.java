package in.woowa.pilot.batch.common;

import in.woowa.pilot.fixture.DataInitializeExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestExecutionListeners;

@SpringBootTest
@TestExecutionListeners(
        listeners = {DataInitializeExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public abstract class BatchIntegrationTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    JobLauncher jobLauncher;

    protected JobLauncherTestUtils jobLauncher(String jobName) {
        Job bean = applicationContext.getBean(jobName, Job.class);
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJob(bean);
        jobLauncherTestUtils.setJobRepository(jobRepository);
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        return jobLauncherTestUtils;
    }

    protected JobParametersBuilder uniqueParameterBuilder() {
        return new JobParametersBuilder(new JobLauncherTestUtils().getUniqueJobParameters());
    }
}

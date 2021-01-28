package in.woowa.pilot.fixture;

import in.woowa.pilot.core.config.QueryDslConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

@DataJpaTest
@Import({QueryDslConfiguration.class})
@TestExecutionListeners(
        listeners = DataInitializeExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public abstract class RepositoryTest {
}

package in.woowa.pilot.fixture;

import com.google.common.base.CaseFormat;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

public class DataInitializeExecutionListener implements TestExecutionListener {

    @Override
    public void afterTestMethod(TestContext testContext) {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        EntityManager entityManager = applicationContext.getBean(EntityManager.class);
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);

        List<String> entityNames = entityManager.getMetamodel().getEntities().stream()
                .map(entity -> getEntityName(entity.getJavaType()))
                .map(name -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name))
                .collect(Collectors.toList());

        jdbcTemplate.execute("SET foreign_key_checks = 0");
        for (String name : entityNames) {
            jdbcTemplate.execute("TRUNCATE TABLE " + name);
        }
        jdbcTemplate.execute("SET foreign_key_checks = 1");
    }

    private String getEntityName(Class<?> entity) {
        if (entity.getAnnotation(Table.class) != null) {
            return entity.getAnnotation(Table.class).name();
        }
        return entity.getSimpleName();
    }
}

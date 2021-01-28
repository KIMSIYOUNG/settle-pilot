package in.woowa.pilot.batch.mock;

import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Profile(value = "truncate-mock-data")
public class MockTruncateGenerator {
    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @PostConstruct
    void truncateDummyData() {

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

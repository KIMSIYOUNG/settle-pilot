package in.woowa.pilot.fixture;

import org.assertj.core.api.AbstractBigDecimalAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.BigDecimalComparator;

import java.math.BigDecimal;

public class TestUtils {

    public static AbstractBigDecimalAssert<?> assertThatBigDecimal(BigDecimal input) {
        return Assertions.assertThat(input).usingComparator(new BigDecimalComparator());
    }
}

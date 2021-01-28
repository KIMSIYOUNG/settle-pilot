package in.woowa.pilot.core.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@EnableAspectJAutoProxy
@Slf4j
public class QueryTimeAspect {

    @Around("within(*..*CustomRepositoryImpl)")
    public Object logQueryTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long begin = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        log.info("Query perform time second: "
                + (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - begin))
        );
        return proceed;
    }
}

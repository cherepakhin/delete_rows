package ru.stm.delete_rows.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class ExecutionTimer {
    @Around("@annotation(ru.stm.delete_rows.aspect.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Запуск таймера");
        StopWatch watch = new StopWatch();
        Object proceed;
        try {
            watch.start();
            proceed = joinPoint.proceed();
        } finally {
            watch.stop();
            long executionTime = watch.getLastTaskTimeMillis();
            String pack = joinPoint.getTarget().getClass().getPackageName();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            log.info("Класс: [{}.{}], Метод: [{}] ", pack, className, methodName);
            log.info("Время выполнения: {} мс", executionTime);
        }
        return proceed;
    }
}

package io.item987.ecommerce.product;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
class Resilience4JConfig {

    @Bean
    public RetryConfig productServiceRetryConfig() {
        return RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofSeconds(3))
                .ignoreExceptions(IgnoredExceptions.getArray())
                .build();
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> productServiceCircuitBreakerCustomizer() {
        var circuitBreakerCfg = CircuitBreakerConfig.custom()
                .minimumNumberOfCalls(5)
                .failureRateThreshold(30)
                .ignoreExceptions(IgnoredExceptions.getArray())
                .build();
        var timeLimiterCfg = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(15))
                .build();
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(circuitBreakerCfg)
                .timeLimiterConfig(timeLimiterCfg)
                .build());
    }

}


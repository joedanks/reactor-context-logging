package org.joedanks.reactor.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;
import reactor.util.context.Context;

import java.util.Map;

import static org.joedanks.reactor.logging.ReactorLogger.MDC_KEY;
import static org.junit.jupiter.api.Assertions.*;

class ReactorLoggerTest {

    private ReactorLogger logger;

    @BeforeEach
    void setUp() {
        logger = new ReactorLogger();
    }

    @Test
    void shouldAddToContext() {
        TestPublisher publisher = TestPublisher.create();

        Mono testMono = publisher.mono()
                .transform(logger.addToContext("key", "value"));

        StepVerifier.create(testMono)
                .expectSubscription()
                .expectAccessibleContext()
                .hasKey(MDC_KEY)
                .matches(context -> ((Map<String, String>)((Context) context).get(MDC_KEY)).get("key").equals("value"))
                .then()
                .then(publisher::complete)
                .verifyComplete();
    }

    @Test
    void shouldAddToMDCForLog() {
        Mono<String> testMono = Mono.just("test")
                .transform(logger.logMessageInfoWithContext((s) -> assertEquals("value", MDC.get("key"))))
                .transform(logger.addToContext("key", "value"));

        StepVerifier.create(testMono)
                .expectNext("test")
                .verifyComplete();
    }

    @Test
    void shouldNotBeAvailableInMDCForLog() {
        Mono<String> testMono = Mono.just("test")
                .transform(logger.addToContext("key", "value"))
                .transform(
                        logger.logMessageInfoWithContext(
                                (s) -> assertNull(MDC.get("key"))
                        )
                );

        StepVerifier.create(testMono)
                .expectNext("test")
                .verifyComplete();
    }
}
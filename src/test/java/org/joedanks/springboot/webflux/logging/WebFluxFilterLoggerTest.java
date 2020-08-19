package org.joedanks.springboot.webflux.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.DefaultServerWebExchange;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WebFluxFilterLoggerTest {

    private WebFluxFilterLogger filterLogger;
    private Logger loggerSpy;

    @BeforeEach
    void setUp() {
        filterLogger = new WebFluxFilterLogger();

        loggerSpy = Mockito.spy(LoggerFactory.getLogger(WebFluxFilterLogger.class));
    }

    @Test
    void shouldLogStart() {
        new DefaultServerWebExchange()


    }
}
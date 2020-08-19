package org.joedanks.springboot.webflux.logging;

import org.joedanks.reactor.logging.ReactorLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class WebFluxFilterLogger implements WebFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebFluxFilterLogger.class);
    private final ReactorLogger reactorLogger;

    public WebFluxFilterLogger() {
        this.reactorLogger = new ReactorLogger();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

        return Mono.<Void>empty()
                .doOnSuccess(aVoid -> LOGGER.info("Start of request: {} {}", serverWebExchange.getRequest().getMethodValue(), serverWebExchange.getRequest().getURI().toString()))
                .then(webFilterChain.filter(serverWebExchange))
                .doOnSuccess(aVoid -> LOGGER.info("End of request: {}", serverWebExchange.getResponse().getStatusCode()))
                .doOnError(t -> LOGGER.error("Request failed", t))
                .transform(reactorLogger.addToContext("request_id", UUID.randomUUID().toString()))
                .transform(reactorLogger.addToContext("method", serverWebExchange.getRequest().getMethodValue()))
                .transform(reactorLogger.addToContext("uri", serverWebExchange.getRequest().getURI().toASCIIString()));
    }
}

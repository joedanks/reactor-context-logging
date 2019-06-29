package org.joedanks.reactor.logging;

import org.slf4j.MDC;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class ReactorLogger {

    static final String MDC_KEY = "mdc_key";

    public <T> Function<Mono<T>, Mono<T>> logMessageInfoWithContext(Consumer<T> loggerConsumer) {
        return (mono) ->
                mono.doOnEach(signal -> {
                    if (signal.isOnNext()) {
                        List<MDC.MDCCloseable> closeableList = signal.getContext()
                                .getOrDefault(MDC_KEY, new HashMap<String, String>())
                                .entrySet()
                                .stream()
                                .map(entry -> MDC.putCloseable(entry.getKey(), entry.getValue()))
                                .collect(toList());
                        try {
                            loggerConsumer.accept(signal.get());
                        } finally {
                            closeableList.forEach(MDC.MDCCloseable::close);
                        }
                    }
                });
    }

    public <T> Function<Mono<T>, Mono<T>> addToContext(String key, String value) {
        return (mono) ->
                mono.subscriberContext(ctx -> {
                    Map<String, String> mdc = ctx.getOrDefault(MDC_KEY, new HashMap<>());
                    mdc.put(key, value);
                    return ctx.put(MDC_KEY, mdc);
                });
    }
}

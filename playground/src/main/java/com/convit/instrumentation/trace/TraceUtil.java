package com.convit.instrumentation.trace;

import com.convit.instrumentation.OpenTelmetryConfig;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;

import java.util.function.Consumer;
import java.util.function.Function;

public class TraceUtil {

    private static final Tracer tracer = OpenTelmetryConfig.tracer(TraceUtil.class);

    public static void trace(String spanName, Consumer<Span> consumer) {
        trace(spanName, span -> {
            consumer.accept(span);
            return null;
        });
    }

    public static <T> T trace(String spanName, Function<Span, T> function) {
        var span = tracer.spanBuilder(spanName)
                .startSpan();
        try(var scope = span.makeCurrent()){
            var t = function.apply(span);
            span.setStatus(StatusCode.OK);
            return t;
        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR);
            return null;
        } finally {
            span.end();
        }
    }
}

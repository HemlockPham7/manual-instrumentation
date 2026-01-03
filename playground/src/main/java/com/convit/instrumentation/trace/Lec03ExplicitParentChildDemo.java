package com.convit.instrumentation.trace;


import com.convit.instrumentation.CommonUtil;
import com.convit.instrumentation.OpenTelmetryConfig;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;

public class Lec03ExplicitParentChildDemo {
    private static final Tracer tracer = OpenTelmetryConfig.tracer(Lec03ExplicitParentChildDemo.class);

    public static void main(String[] args) {

        var demo = new Lec03ExplicitParentChildDemo();
        //POST /orders
        demo.processOrder();

        CommonUtil.sleepSeconds(2);

    }

    private void processOrder() {
        var span = tracer.spanBuilder("processOrder")
                .setNoParent()
                .startSpan();
        try {
            processPayment(span);
            Thread.ofPlatform().start(() -> deductInventory(span));
            Thread.ofVirtual().start(() -> sendNotification(span));
            // deductInventory(span);
            // sendNotification(span);
            span.setAttribute("order.id", 124);
            span.setAttribute("order.amount", 1500);

            span.setStatus(StatusCode.OK);
        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR);
        } finally {
            span.end();
        }
    }

    private void processPayment(Span parent) {
        var span = tracer.spanBuilder("processPayment")
                .setParent(Context.current().with(parent))
                .startSpan();
        try {
            CommonUtil.sleepMillis(150);
            span.setAttribute("payment.method", "CREDIT_CARD");
            span.setStatus(StatusCode.OK);
        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR);
        } finally {
            span.end();
        }
    }

    private void deductInventory(Span parent) {
        var span = tracer.spanBuilder("deductInventory")
                .setParent(Context.current().with(parent))
                .startSpan();
        try {
            CommonUtil.sleepMillis(125);
            span.setStatus(StatusCode.OK);
        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR);
        } finally {
            span.end();
        }
    }

    private void sendNotification(Span parent) {
        var span = tracer.spanBuilder("sendNotification")
                .setParent(Context.current().with(parent))
                .startSpan();
        try {
            CommonUtil.sleepMillis(100);
            span.setStatus(StatusCode.OK);
        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR);
        } finally {
            span.end();
        }
    }

}

package com.convit.instrumentation.trace;

import com.convit.instrumentation.CommonUtil;
import com.convit.instrumentation.OpenTelmetryConfig;
import io.opentelemetry.api.trace.Tracer;

public class Lec01SpanBasicsDemo {

    private static final Tracer tracer = OpenTelmetryConfig.tracer(Lec01SpanBasicsDemo.class);

    public static void main(String[] args) {
        var demo = new Lec01SpanBasicsDemo();
        demo.processOrder();

        CommonUtil.sleepSeconds(2);
    }

    private void processOrder() {
        var span = tracer.spanBuilder("processOrder")
                        .startSpan();
        processPayment();
        deductInventory();
        sendNotification();

        span.end();
    }

    private void processPayment() {
        CommonUtil.sleepMillis(150);
    }

    private void deductInventory() {
        CommonUtil.sleepMillis(125);
    }

    private void sendNotification() {
        CommonUtil.sleepMillis(100);
    }
}

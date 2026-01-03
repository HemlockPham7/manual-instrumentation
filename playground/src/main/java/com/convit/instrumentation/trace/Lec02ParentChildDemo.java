package com.convit.instrumentation.trace;

import com.convit.instrumentation.CommonUtil;
import com.convit.instrumentation.OpenTelmetryConfig;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;

public class Lec02ParentChildDemo {

    private static final Tracer tracer = OpenTelmetryConfig.tracer(Lec02ParentChildDemo.class);

    public static void main(String[] args) {

        var demo = new Lec02ParentChildDemo();
        //POST /orders
        demo.processOrder();

        CommonUtil.sleepSeconds(2);

    }

    private void processOrder() {
        var span = tracer.spanBuilder("processOrder")
                .startSpan();
        try(var scope = span.makeCurrent()){
            processPayment();
            sendNotification();

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

    private void processPayment() {
        var span = tracer.spanBuilder("processPayment")
                .startSpan();
        try(var scope = span.makeCurrent()){
            CommonUtil.sleepMillis(150);
            deductInventory();
            span.setAttribute("payment.method", "CREDIT_CARD");
            span.setStatus(StatusCode.OK);
        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR);
        }finally {
            span.end();
        }
    }

    private void deductInventory() {
        var span = tracer.spanBuilder("deductInventory")
                .startSpan();
        try{
            CommonUtil.sleepMillis(125);
            span.setStatus(StatusCode.OK);
        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR);
        }finally {
            span.end();
        }
    }

    private void sendNotification() {
        var span = tracer.spanBuilder("sendNotification")
                .startSpan();
        try{
            CommonUtil.sleepMillis(100);
            span.setStatus(StatusCode.OK);
        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR);
        }finally {
            span.end();
        }
    }
}

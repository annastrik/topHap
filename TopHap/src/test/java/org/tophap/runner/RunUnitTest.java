package org.tophap.runner;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RunUnitTest {

    private static class SingleValid extends SingleApiTest {
        @Test
        void planeTest() {}
    }

    private static class SingleInvalid extends SingleApiTest {
        @Order(1)
        @Test
        void planeTest() {}
    }

    private static class MultipleValid extends MultipleApiTest {
        @Order(1)
        @Test
        void planeTest() {}
    }

    private static class MultipleInvalid extends MultipleApiTest {
        @Test
        void planeTest() {}
    }

    @Test
    void validSingleAnnotations() {
        SingleValid singleValid = new SingleValid();
    }

    @Test
    void invalidSingleAnnotations() {
        WrongAssignableClass error = null;
        try {
            SingleInvalid singleInvalid = new SingleInvalid();
        } catch (WrongAssignableClass e) {
            error = e;
        }
        assertNotNull(error);
    }

    @Test
    void validMultipleAnnotations() {
        MultipleValid multipleValid = new MultipleValid();
    }

    @Test
    void invalidMultipleAnnotations() {
        WrongAssignableClass error = null;
        try {
            MultipleInvalid multipleInvalid = new MultipleInvalid();
        } catch (WrongAssignableClass e) {
            error = e;
        }
        assertNotNull(error);
    }

}

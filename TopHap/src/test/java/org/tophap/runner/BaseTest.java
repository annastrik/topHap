package org.tophap.runner;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class BaseTest {

    BaseTest() throws WrongAssignableClass {
        for (Method method : this.getClass().getDeclaredMethods()){
            validateMethodAnnotations(method);
        }
    }

    private void validateMethodAnnotations(Method method) {
        if (method.getAnnotation(Test.class) != null) {
            if (method.getAnnotation(Order.class) == null) {
                if (MultipleApiTest.class.isAssignableFrom(this.getClass())) {

                    String errorMessage = String.format(
                            "Class %s is extended from %s, thus method %s should use @Order annotation",
                            this.getClass().getSimpleName(),
                            MultipleApiTest.class.getSimpleName(),
                            method.getName()
                    );
                    throw new WrongAssignableClass(errorMessage);
                }
            } else {
                if (SingleApiTest.class.isAssignableFrom(this.getClass()) && !MultipleApiTest.class.isAssignableFrom(this.getClass())) {

                    String errorMessage = String.format(
                            "Class %s is extended from %s, thus method %s should not use @Order annotation",
                            this.getClass().getSimpleName(),
                            SingleApiTest.class.getSimpleName(),
                            method.getName()
                    );
                    throw new WrongAssignableClass(errorMessage);
                }
            }
        }
    }
}

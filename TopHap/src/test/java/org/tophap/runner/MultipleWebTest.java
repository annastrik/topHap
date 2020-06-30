package org.tophap.runner;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

public abstract class MultipleWebTest extends MultipleApiTest {

    private final WebImpl webImpl = new WebImpl();

    @BeforeAll
    private void setUpAll() {
        webImpl.beforeTest();
    }

    @AfterAll
    private void setDownAll() {
        webImpl.afterTest();
    }

    protected WebDriver getDriver() {
        return webImpl.getDriver();
    }
}

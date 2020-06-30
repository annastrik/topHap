package org.tophap.runner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public abstract class SingleWebTest extends SingleApiTest {

    private final WebImpl webImpl = new WebImpl();

    @BeforeEach
    private void setUp() {
        webImpl.beforeTest();
    }

    @AfterEach
    private void setDown() {
        webImpl.afterTest();
    }

    protected WebDriver getDriver() {
        return webImpl.getDriver();
    }
}

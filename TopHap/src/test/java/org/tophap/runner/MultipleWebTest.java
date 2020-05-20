package org.tophap.runner;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class MultipleWebTest extends MultipleApiTest {

    private WebImpl webImpl = new WebImpl();

    @BeforeAll
    private void setUpAll() throws IOException {
        webImpl.beforeAll();
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

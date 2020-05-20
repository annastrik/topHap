package org.tophap.runner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

public class SingleWebTest extends SingleApiTest {

    private WebImpl webImpl = new WebImpl();

    @BeforeEach
    private void setUp() throws MalformedURLException {
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

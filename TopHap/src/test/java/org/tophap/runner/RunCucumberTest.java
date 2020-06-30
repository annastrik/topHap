package org.tophap.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/org/tophap/cucumber",
        glue = {"org.tophap.cucumber", "org.tophap.runner"})
public class RunCucumberTest {
}
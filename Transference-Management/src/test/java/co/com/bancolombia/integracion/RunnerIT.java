package co.com.bancolombia.integracion;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
    strict = true,
    features = { "classpath:features" }
)
public class RunnerIT {
}

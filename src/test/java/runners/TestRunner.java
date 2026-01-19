package runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
  features = "src/test/resources/features",
  glue = {"hooks", "steps"},
  plugin = {"pretty"},
  monochrome = true
)
/**
 * JUnit runner for Cucumber features.
 *
 * Use this runner to execute all features under src/test/resources/features.
 */
public class TestRunner {}

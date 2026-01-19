package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import com.microsoft.playwright.Page;

/**
 * Cucumber hooks for reporting integration.
 *
 * - Creates an Extent test before each scenario and logs basic start info.
 * - On scenario end, attaches a screenshot on failure and logs pass/fail.
 * - Flushes the extent report so results become visible in the output HTML.
 */
public class ReportingHooks {

  @Before
  public void beforeScenario(Scenario scenario) {
    ReportManager.createTest(scenario.getName());
    ReportManager.logInfo("Starting scenario: " + scenario.getName());
  }

  @After
  public void afterScenario(Scenario scenario) {
    if (scenario.isFailed()) {
      // try attach screenshot when possible
      try {
        Page page = TestHooks.page;
        if (page != null) ReportManager.attachScreenshot(page, "failure");
      } catch (Exception ignored) {}
      ReportManager.logFail("Scenario failed: " + scenario.getName());
    } else {
      ReportManager.logPass("Scenario passed: " + scenario.getName());
    }
    ReportManager.flush();
  }
}

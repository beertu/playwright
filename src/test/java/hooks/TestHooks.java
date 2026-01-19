package hooks;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Attachment;

/**
 * Test lifecycle hooks that manage Playwright browser lifecycle for each scenario.
 *
 * - Creates Playwright, launches Chromium, and opens a new Page before scenarios.
 * - Captures a screenshot on failure and ensures browser/page are closed after scenario.
 */
public class TestHooks {
  // Shared Playwright instances exposed to steps/pages
  public static Playwright playwright;
  public static Browser browser;
  public static Page page;

  @Before(order = 0)
  public void beforeScenario() {
    try {
      playwright = Playwright.create();
      BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
      // Determine headless mode: prefer JVM system property, otherwise read config.properties
      String headlessValue = System.getProperty("headless");
      if (headlessValue == null) {
        try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
          if (in != null) {
            java.util.Properties p = new java.util.Properties();
            p.load(in);
            headlessValue = p.getProperty("headless");
          }
        } catch (Exception ignored) {
          // ignore and fallback to default below
        }
      }
      if (headlessValue == null) headlessValue = "true";
      options.setHeadless(Boolean.parseBoolean(headlessValue));
      // when running headed, request Chromium to start maximized
      if (!Boolean.parseBoolean(headlessValue)) {
        options.setArgs(java.util.Arrays.asList("--start-maximized"));
      }
      browser = playwright.chromium().launch(options);
      page = browser.newPage();
      // Try to set a large viewport for headed runs so the browser appears maximized
      try {
        if (!Boolean.parseBoolean(headlessValue)) {
          page.setViewportSize(1920, 1080);
        }
      } catch (Exception ignored) {
        // non-fatal if setting viewport fails
      }
    } catch (RuntimeException e) {
      // common failure is browser download error — give a clear message
      String msg = e.getMessage() == null ? e.toString() : e.getMessage();
      System.err.println("Playwright failed to launch browser: " + msg);
      System.err.println("If this is the first run, install Playwright browsers locally by running:\n  npx playwright install\nfrom the project directory, or set a valid PLAYWRIGHT_BROWSERS_PATH.");
      throw e;
    }
  }

  @After
  public void afterScenario(Scenario scenario) {
    try {
      if (scenario.isFailed()) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        saveScreenshot(screenshot);
      }
    } catch (Exception e) {
      // ignore
    } finally {
      if (page != null) page.close();
      if (browser != null) browser.close();
      if (playwright != null) playwright.close();
    }
  }

  @Attachment(value = "Screenshot", type = "image/png")
  public byte[] saveScreenshot(byte[] data) {
    return data;
  }
}

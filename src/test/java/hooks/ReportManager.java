package hooks;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

/**
 * Centralized Extent report manager.
 *
 * Responsibilities:
 * - Initialize the Extent report and reporter output directory
 * - Provide thread-safe current test handle for logging
 * - Attach screenshots and flush the report at the end
 */
public class ReportManager {
  private static final ExtentReports extent;
  private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();

  static {
    extent = new ExtentReports();
    Path out = Paths.get("target", "extent-report");
    try {
      Files.createDirectories(out);
    } catch (IOException e) {
      // ignore
    }
    ExtentSparkReporter spark = new ExtentSparkReporter(out.resolve("index.html").toString());
    spark.config().setDocumentTitle("Test Execution Report");
    spark.config().setReportName("Playwright Cucumber Tests");
    spark.config().setTheme(Theme.STANDARD);
    extent.attachReporter(spark);
  }

  public static ExtentTest createTest(String name) {
    ExtentTest t = extent.createTest(name);
    currentTest.set(t);
    return t;
  }

  public static void logInfo(String msg) {
    ExtentTest t = currentTest.get();
    if (t != null) t.info(msg);
  }

  public static void logPass(String msg) {
    ExtentTest t = currentTest.get();
    if (t != null) t.pass(msg);
  }

  public static void logFail(String msg) {
    ExtentTest t = currentTest.get();
    if (t != null) t.fail(msg);
  }

  public static void attachScreenshot(Page page, String screenshotNamePrefix) {
    ExtentTest t = currentTest.get();
    if (t == null || page == null) return;
    try {
      String ts = String.valueOf(Instant.now().toEpochMilli());
      Path dir = Paths.get("target", "extent-report", "screenshots");
      Files.createDirectories(dir);
      Path file = dir.resolve(screenshotNamePrefix + "_" + ts + ".png");
      page.screenshot(new Page.ScreenshotOptions().setPath(file));
      t.addScreenCaptureFromPath("screenshots/" + file.getFileName().toString());
    } catch (Exception e) {
      // ignore screenshot failures
    }
  }

  public static void attachScreenshotToStep(Page page, String screenshotNamePrefix, String message, String status) {
    ExtentTest t = currentTest.get();
    if (t == null || page == null) return;
    try {
      String ts = String.valueOf(Instant.now().toEpochMilli());
      Path dir = Paths.get("target", "extent-report", "screenshots");
      Files.createDirectories(dir);
      Path file = dir.resolve(screenshotNamePrefix + "_" + ts + ".png");
      page.screenshot(new Page.ScreenshotOptions().setPath(file));
      String relative = "screenshots/" + file.getFileName().toString();
      var media = com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath(relative).build();
      if (status != null && status.equalsIgnoreCase("pass")) {
        t.pass(message, media);
      } else if (status != null && status.equalsIgnoreCase("fail")) {
        t.fail(message, media);
      } else {
        t.info(message, media);
      }
    } catch (Exception e) {
      // ignore screenshot failures
    }
  }

  public static void flush() {
    if (extent != null) extent.flush();
  }
}

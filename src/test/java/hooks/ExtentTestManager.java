package hooks;

import com.microsoft.playwright.Page;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Convenience helper used by step definitions to log step-level results and
 * attach screenshots to the Extent report.
 */
public class ExtentTestManager {
  /**
   * Capture a screenshot (if page available) and log a step with the given status.
   * If page is null, logs only the message to the report.
   */
  public static void takeScreenshot(Page page, String status, String message) {
    try {
      if (page != null) {
        // attach screenshot and log it together with the step
        ReportManager.attachScreenshotToStep(page, message.replaceAll("\\s+", "_"), message, status);
      } else {
        if (status != null && status.equalsIgnoreCase("pass")) {
          ReportManager.logPass(message);
        } else if (status != null && status.equalsIgnoreCase("fail")) {
          ReportManager.logFail(message);
        } else {
          ReportManager.logInfo(message);
        }
      }
    } catch (Exception e) {
      ReportManager.logInfo("takeScreenshot failed: " + e.getMessage());
    }
  }

  public static void logTestResult(String level, String message) {
    if (level == null) level = "INFO";
    switch (level.toUpperCase()) {
      case "PASS":
        ReportManager.logPass(message);
        break;
      case "FAIL":
        ReportManager.logFail(message);
        break;
      default:
        ReportManager.logInfo(message);
        break;
    }
  }

  public static void logException(Throwable t) {
    if (t == null) return;
    ReportManager.logFail(ExceptionUtils.getStackTrace(t instanceof Exception ? (Exception) t : new Exception(t)));
  }
}

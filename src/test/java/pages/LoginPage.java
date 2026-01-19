package pages;

import com.microsoft.playwright.Page;

/**
 * Page object representing the login page of SauceDemo.
 *
 * Encapsulates navigation and basic login operations so step definitions
 * remain concise and focused on behavior rather than UI details.
 */
public class LoginPage {
  private final Page page;
  public LoginPage(Page page) { this.page = page; }

  /** Navigate the browser to the login page URL. */
  public void open(String url) { page.navigate(url); }

  /** Fill credentials and submit the login form. */
  public void login(String username, String password) {
    page.fill("#user-name", username);
    page.fill("#password", password);
    page.click("#login-button");
  }

  /** Return the visible login error text, or null if none present. */
  public String getError() {
    return page.textContent("h3[data-test='error']");
  }
}
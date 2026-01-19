package steps;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

import hooks.TestHooks;
import hooks.ExtentTestManager;
import org.apache.commons.lang3.exception.ExceptionUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductsPage;

/**
 * UI step definitions implementing user interactions for SauceDemo flows.
 *
 * Each step uses page objects (LoginPage, ProductsPage, CartPage) and logs
 * results to the Extent report via `ExtentTestManager`.
 */
public class UiSteps {
  private LoginPage loginPage;
  private ProductsPage productsPage;
  private CartPage cartPage;

  private String readProp(String key) {
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
      Properties p = new Properties();
      p.load(in);
      return p.getProperty(key);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Open the login page using `baseUrl` from config.properties.
   */
  @Given("I open the login page")
  public void i_open_the_login_page() {
    try {
      loginPage = new LoginPage(TestHooks.page);
      loginPage.open(readProp("baseUrl"));
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Opened login page");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @When("I login as {string} with password {string}")
  public void i_login_as_with_password(String user, String pass) {
    try {
      loginPage.login(user, pass);
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "User logged in: " + user);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @When("I attempt to login as {string} with password {string}")
  public void i_attempt_to_login_as_with_password(String user, String pass) {
    try {
      loginPage.login(user, pass);
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Attempted login: " + user);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Then("login should fail with an error message")
  public void login_should_fail_with_an_error_message() {
    try {
      String err = loginPage.getError();
      assertTrue(err != null && !err.isEmpty());
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Login failed as expected");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @When("I add all products to the cart")
  public void i_add_all_products_to_the_cart() {
    try {
      productsPage = new ProductsPage(TestHooks.page);
      productsPage.addAllToCart();
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Added all products to cart");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @When("I go to the cart")
  public void i_go_to_the_cart() {
    try {
      productsPage.goToCart();
      cartPage = new CartPage(TestHooks.page);
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Opened cart page");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @When("I remove the third item from the cart")
  public void i_remove_the_third_item_from_the_cart() {
    try {
      productsPage.removeThirdItemFromCart();
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Removed third item from cart");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @When("I proceed to checkout with firstName {string} lastName {string} postalCode {string}")
  public void i_proceed_to_checkout_with_first_name_last_name_postal_code(String first, String last, String postal) {
    try {
      cartPage.checkout(first, last, postal);
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Proceeded to checkout");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Then("I should see the order confirmation")
  public void i_should_see_the_order_confirmation() {
    try {
      String conf = cartPage.getConfirmation();
      assertTrue(conf != null && !conf.isEmpty());
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Order confirmation displayed");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @When("I open product by name {string}")
  public void i_open_product_by_name(String name) {
    try {
      productsPage = new ProductsPage(TestHooks.page);
      productsPage.openProductByName(name);
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Opened product: " + name);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @When("I add the product to the cart from product page")
  public void i_add_the_product_to_the_cart_from_product_page() {
    try {
      productsPage.addToCartFromProductPage();
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Added product to cart from product page");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Then("the cart should contain product {string}")
  public void the_cart_should_contain_product(String name) {
    // ensure cart items are present and refresh cart page object
    try {
      // use locator wait to avoid unresolved Playwright inner class issues
      TestHooks.page.locator(".cart_item .inventory_item_name").waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(3000));
    } catch (Exception ignored) {}
    try {
      cartPage = new CartPage(TestHooks.page);
      var items = cartPage.getCartItems();
      System.out.println("Cart items: " + items);
      ExtentTestManager.logTestResult("INFO", "Cart items: " + items);
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "cart_contents");
      assertTrue(items.stream().anyMatch(s -> s.contains(name)));
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @When("I sort products by {string}")
  public void i_sort_products_by(String option) {
    try {
      productsPage = new ProductsPage(TestHooks.page);
      productsPage.sortBy(option);
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Sorted products by: " + option);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Then("products should be sorted by name ascending")
  public void products_should_be_sorted_by_name_ascending() {
    try {
      var list = productsPage.getProductNames();
      var sorted = list.stream().sorted().toList();
      assertTrue(list.equals(sorted));
      ExtentTestManager.takeScreenshot(TestHooks.page, "Pass", "Products sorted by name ascending");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }
}

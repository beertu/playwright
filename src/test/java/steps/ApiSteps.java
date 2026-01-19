package steps;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

import api.DummyJsonClient;
import hooks.ExtentTestManager;
import org.apache.commons.lang3.exception.ExceptionUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

/**
 * API step definitions that call the DummyJSON test API using `DummyJsonClient`.
 *
 * Steps store the last `Response` so assertions can be performed on response
 * status, time and body content.
 */
public class ApiSteps {
  private DummyJsonClient client;
  private Response response;

  private String readProp(String key) {
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
      Properties p = new Properties();
      p.load(in);
      return p.getProperty(key);
    } catch (IOException e) {
      return null;
    }
  }

  @Given("I request products with delay {int}")
  public void i_request_products_with_delay(Integer delay) {
    try {
      if (client == null) client = new DummyJsonClient(readProp("dummyJsonBase"));
      response = client.getProductsWithDelay(delay);
      ExtentTestManager.logTestResult("INFO", "Requested products with delay: " + delay);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Given("I request all products")
  public void i_request_all_products() {
    try {
      if (client == null) client = new DummyJsonClient(readProp("dummyJsonBase"));
      response = client.getAllProducts();
      ExtentTestManager.logTestResult("INFO", "Requested all products");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Given("I create a product with title {string} description {string} price {int} brand {string}")
  public void i_create_a_product_with_title_description_price_brand(String title, String desc, Integer price, String brand) {
    try {
      if (client == null) client = new DummyJsonClient(readProp("dummyJsonBase"));
      response = client.createProduct(title, desc, price, brand);
      ExtentTestManager.logTestResult("INFO", "Created product: " + title);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Given("I get product with id {int}")
  public void i_get_product_with_id(Integer id) {
    try {
      if (client == null) client = new DummyJsonClient(readProp("dummyJsonBase"));
      response = client.getProduct(id);
      ExtentTestManager.logTestResult("INFO", "Fetched product id: " + id);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @When("I update product id {int} with title {string}")
  public void i_update_product_id_with_title(Integer id, String title) {
    try {
      String body = "{\"title\":\"" + title + "\"}";
      response = client.updateProduct(id, body);
      ExtentTestManager.logTestResult("INFO", "Updated product id " + id + " with title " + title);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Then("the response status should be {int}")
  public void the_response_status_should_be(Integer status) {
    try {
      assertEquals(status.intValue(), response.getStatusCode());
      ExtentTestManager.logTestResult("INFO", "Response status verified: " + status);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Then("the response time should be less than {int} ms")
  public void the_response_time_should_be_less_than_ms(Integer ms) {
    try {
      long time = response.getTime();
      if (!(time < ms)) {
        throw new AssertionError("Response time " + time + " >= " + ms);
      }
      ExtentTestManager.logTestResult("INFO", "Response time: " + time + " ms");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Then("print titles of products with odd ids")
  public void print_titles_of_products_with_odd_ids() {
    try {
      System.out.println(response.getBody().asString());
      ExtentTestManager.logTestResult("INFO", "Response body printed");
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Then("the response should contain the created product title {string}")
  public void the_response_should_contain_the_created_product_title(String title) {
    try {
      String body = response.getBody().asString();
      if (!body.contains(title)) throw new AssertionError("Response does not contain title: " + title);
      ExtentTestManager.logTestResult("INFO", "Response contains created product title: " + title);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Then("the update response status should be {int}")
  public void the_update_response_status_should_be(Integer status) {
    try {
      assertEquals(status.intValue(), response.getStatusCode());
      ExtentTestManager.logTestResult("INFO", "Update response status verified: " + status);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  @Then("the updated product title should be {string}")
  public void the_updated_product_title_should_be(String title) {
    try {
      String body = response.getBody().asString();
      if (!body.contains(title)) throw new AssertionError("Updated product body missing title: " + title);
      ExtentTestManager.logTestResult("INFO", "Updated product title verified: " + title);
    } catch (Exception e) {
      ExtentTestManager.logTestResult("ERROR", ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }
}

package api;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

/**
 * Lightweight REST client for DummyJSON API used in API tests.
 *
 * Wraps common operations (list, create, get, update) so steps remain concise.
 */
public class DummyJsonClient {
  private final String base;

  public DummyJsonClient(String base) {
    this.base = base;
    RestAssured.baseURI = base;
  }

  /** GET /products */
  public Response getAllProducts() {
    return given().when().get("/products").then().extract().response();
  }

  /** POST /products/add - creates a product (DummyJSON supports this endpoint). */
  public Response createProduct(String title, String description, int price, String brand) {
    return given()
      .contentType("application/json")
      .body("{\"title\":\""+title+"\",\"description\":\""+description+"\",\"price\":"+price+",\"brand\":\""+brand+"\"}")
      .when().post("/products/add")
      .then().extract().response();
  }

  /** GET /products/{id} */
  public Response getProduct(int id) {
    return given().when().get("/products/" + id).then().extract().response();
  }

  /** PUT /products/{id} - update product with provided JSON body. */
  public Response updateProduct(int id, String jsonBody) {
    return given()
      .contentType("application/json")
      .body(jsonBody)
      .when().put("/products/" + id)
      .then().extract().response();
  }

  /** GET /products?delay={delay} - useful to test delayed responses. */
  public Response getProductsWithDelay(int delay) {
    return given().queryParam("delay", delay).when().get("/products").then().extract().response();
  }
}

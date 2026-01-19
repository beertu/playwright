package pages;

import java.util.List;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Page object for product listing and product-specific actions.
 *
 * Small helpers hide Playwright locators and provide readable operations
 * used by step definitions.
 */
public class ProductsPage {
  private final Page page;
  public ProductsPage(Page page) { this.page = page; }

  /** Click "Add to cart" on all visible products. */
  public void addAllToCart() {
    page.locator("button.btn_inventory").all().forEach(Locator::click);
  }

  /** Open a product details page by visible product name. */
  public void openProductByName(String name) {
    page.click("text=" + name);
    page.waitForLoadState();
  }

  /** Click the primary add-to-cart button on a product page. */
  public void addToCartFromProductPage() {
    page.click("button.btn_primary.btn_inventory");
  }

  /** Navigate to the cart page. */
  public void goToCart() {
    page.click("a.shopping_cart_link");
  }

  /** Return visible product names on the listing page. */
  public List<String> getProductNames() {
    return page.locator(".inventory_item_name").allTextContents();
  }

  /** Select a sort option by mapping human text to select value. */
  public void sortBy(String option) {
    // Map visible option text to the select element's value attribute, then select by value
    String value = "az"; // default to A to Z
    if (option.contains("A to Z")) value = "az";
    else if (option.contains("Z to A")) value = "za";
    else if (option.toLowerCase().contains("low to high")) value = "lohi";
    else if (option.toLowerCase().contains("high to low")) value = "hilo";
    page.selectOption(".product_sort_container", value);
  }

  /** Remove the third item listed in the cart (0-based index). */
  public void removeThirdItemFromCart() {
    // go to cart first
    page.click("a.shopping_cart_link");
    page.locator(".cart_item").nth(2).locator("button").click();
  }
}

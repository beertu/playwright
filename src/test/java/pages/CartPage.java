package pages;

import java.util.List;

import com.microsoft.playwright.Page;

/**
 * Page object for the shopping cart and checkout flow.
 */
public class CartPage {
  private final Page page;
  public CartPage(Page page) { this.page = page; }

  /** Return the list of item names currently present in the cart. */
  public List<String> getCartItems() {
    return page.locator(".cart_item .inventory_item_name").allTextContents();
  }

  /**
   * Perform checkout by filling the address form and finishing the order.
   * This performs page interactions for the final checkout flow.
   */
  public void checkout(String firstName, String lastName, String postalCode) {
    page.click("button[data-test='checkout']");
    page.fill("#first-name", firstName);
    page.fill("#last-name", lastName);
    page.fill("#postal-code", postalCode);
    page.click("input[data-test='continue']");
    page.click("button[data-test='finish']");
  }

  /** Return the order confirmation header shown on success. */
  public String getConfirmation() {
    return page.textContent(".complete-header");
  }
}

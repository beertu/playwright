package pages;

import java.util.List;

import com.microsoft.playwright.Page;

public class CheckoutPage {
  private final Page page;

  public CheckoutPage(Page page) {
    this.page = page;
  }

  // Navigate to checkout info page (assumes you're on cart page)
  public void startCheckout() {
    page.click("button[data-test='checkout']");
  }

  // Fill checkout information (first step)
  public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
    page.fill("#first-name", firstName);
    page.fill("#last-name", lastName);
    page.fill("#postal-code", postalCode);
    page.click("input[data-test='continue']");
  }

  // Get list of item names on the overview page
  public List<String> getOverviewItemNames() {
    return page.locator(".cart_item .inventory_item_name").allTextContents();
  }

  // Get item prices on the overview page as strings (e.g., "$29.99")
  public List<String> getOverviewItemPrices() {
    return page.locator(".cart_item .inventory_item_price").allTextContents();
  }

  // Parse and return subtotal value shown on overview (numeric)
  public double getSubtotal() {
    String text = page.textContent(".summary_subtotal_label"); // "Item total: $39.98"
    if (text == null) return 0.0;
    String num = text.replaceAll("[^0-9.]", "");
    return num.isEmpty() ? 0.0 : Double.parseDouble(num);
  }

  // Parse and return tax value shown on overview (numeric)
  public double getTax() {
    String text = page.textContent(".summary_tax_label"); // "Tax: $3.20"
    if (text == null) return 0.0;
    String num = text.replaceAll("[^0-9.]", "");
    return num.isEmpty() ? 0.0 : Double.parseDouble(num);
  }

    // Parse and return total value shown on overview (numeric)
    public double getTotal() {
      String text = page.textContent(".summary_total_label"); // "Total: $43.18"
      if (text == null) return 0.0;
      String num = text.replaceAll("[^0-9.]", "");
      return num.isEmpty() ? 0.0 : Double.parseDouble(num);
    }
  }
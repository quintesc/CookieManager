/**
 * Class that models a pack of cookies, all of the same type
 * @author Andres Quintanal
 *
 */
public class CookiePack {
  private int quantity; // quantity of cookies in pack
  private double price; // total price of package to the customer
  private double myCost; // total cost of making cookie pack
  private String type; // type of cookie in cookie pack

  /**
  * Constructs a new cookie pack
  * @param type the flavor of the cookie
  * @param quantity the number of cookies for the given type
  */
  public CookiePack(String type, int quantity) {
    this.quantity = quantity;
    this.type = type;
    setPrice();
    setMyCost();
  }

  // yes I did use switch cases to avoid having 6 different class files, sue me

  /** 
   * Sets the price of the pack
   */
  private void setPrice() {
    switch (type) {
      case "macadamia":
        price = 5;
        break;
      case "ferrero":
        price = 6;
        break;
      case "chocolate chip":
        price = 3;
        break;
      case "triple chocolate":
        price = 3.5;
        break;
      case "almond":
        price = 4.75;
        break;
      case "pecan":
        price = 4.5;
        break;
    }
    price *= quantity;
  }

  /**
   * Sets the cost to producer of the pack
   */
  private void setMyCost() {
    switch (type) {
      case "macadamia":
        myCost = 2.5;
        break;
      case "ferrero":
        myCost = 3.6;
        break;
      case "chocolate chip":
        myCost = 2.0;
        break;
      case "triple chocolate":
        myCost = 2.5;
        break;
      case "almond":
        myCost = 2.75;
        break;
      case "pecan":
        myCost = 3.1;
        break;
    }
    myCost *= quantity;
  }

  /**
   * Creates a String representation of this pack
   */
  @Override
  public String toString() {
    // these two types have longer strings so to keep consistent format only do 1 tab
    if (type.equals("chocolate chip") || type.equals("triple chocolate")) {
      return "" + quantity + " " + type + ".\t$" + String.format("%,.2f", price) + "\n";
    }
    return "" + quantity + " " + type + ".\t\t$" + String.format("%,.2f", price) + "\n";
  }

  /**
   * Returns the total price of the pack to a customer
   */
  public double getPrice() {
    return price;
  }

  /**
   * Returns total cost to producer to make the pack
   */
  public double getMyCost() {
    return myCost;
  }

  /**
   * Returns the type of the cookie in this pack
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the quantity of cookies in this pack
   */
  public int getQuantity() {
    return quantity;
  }
}

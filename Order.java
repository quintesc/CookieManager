import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This class models an Order of cookies, including all relevant data
 * @author Andres Quintanal Escandon
 */
public class Order {
  private ArrayList<CookiePack> cookies; // list with number & types of cookies
  private String datePlaced; // date of order placement
  private String dateCompleted; // date when order was completed eg delivered and paid for
  private boolean isCompleted; // has the order been completed
  private Client customer; // customer who bought the cookies
  private int orderNumber; // number of the order
  private double price; // price of the order to the customer
  private double myCost; // cost of baking all cookies in order to me
  protected static int nextOrderNumber; // number for next order. 

  /**
   * Creates a new order. Assumes all inputs have been given as valid from the front end
   * @param cookies A list with all the cookies in the order 
   * @param customer Customer who ordered the cookies
   */
  public Order(ArrayList<CookiePack> cookies, Client customer) {
    this.cookies = cookies;
    datePlaced = LocalDate.now().toString();
    this.customer = customer;
    isCompleted = false;
    orderNumber = nextOrderNumber;
    nextOrderNumber++;
    for (CookiePack pack : cookies) {
      price += pack.getPrice();
      myCost += pack.getMyCost();
    }
  }

  /**
  * Creates a new order from a single String. Used when loading data from the text file
  * @param data A string with all the data of an order
  */
  protected Order(String loadString) {
    String[] data = loadString.split(">"); // each index is a field. See toLoadString() below

    // cookies
    cookies = new ArrayList<>();
    String[] packData;
    String[] cookiePacks = data[0].split("&");
    for (String s : cookiePacks) {
      packData = s.split("<");
      cookies.add(new CookiePack(packData[0], Integer.parseInt(packData[1])));
    }

    // dates
    datePlaced = data[1];
    dateCompleted = (data[2].equals("")) ? null : data[2];

    // other data
    customer = BackEnd.getClient(data[3]);
    isCompleted = (data[4].equals("true")) ? true : false;
    orderNumber = Integer.parseInt(data[5]);
    for (CookiePack pack : cookies) {
      price += pack.getPrice();
      myCost += pack.getMyCost();
    }
    nextOrderNumber++; // so after loading, new orders have unique numbers
  }

  /**
  * Creates a string version of this Order to store into a text file
  */
  protected String toLoadString() {
    // first do cookies. < separates type and quantity, & separates cookie packs
    String s = "" + cookies.get(0).getType() + "<" + cookies.get(0).getQuantity();
    for (int i = 1; i < cookies.size(); i++) {
      s = s + "&" + cookies.get(i).getType() + "<" + cookies.get(i).getQuantity();
    }
    
    // now do rest. > separates the fields of an order
    s = s + ">" + datePlaced;
    if (dateCompleted == null) {
      s = s + ">" + "";
    } else {
      s = s + ">" + dateCompleted;
    }
    s = s + ">" + customer.getName() + ">" + isCompleted + ">" + orderNumber;
    return s;
    // example s:
    // type0<quantity0&type1<quantity1>dateSet>dateComplete>clientName>isComplete>orderNum
  }
  
  /**
  * Creates a String representation of this order
  */
  @Override
  public String toString() {
    String s = "Summary of Order #" + orderNumber + "\n-----\n" + customer.toString() + "-----\n"
        + "Cookies:\n";
    for (CookiePack pack : cookies) {
      s += pack.toString();
    }
    s += "Total:\t\t\t$" + String.format("%.2f", price) + "\n-----\n" + "Date placed: " + datePlaced + "\n";
    s += (isCompleted) ? "Date completed: " + dateCompleted
        : "Order has not been completed";
    return s;
  }


  /**
  * Checks if an Order is completed
  * @return true if completed, false if not completed
  */
  public boolean isCompleted() {
    return isCompleted;
  }

  /**
  * Sets the status of this order to completed. No negative effects if an order was 
  * already set as complete
  */
  public void setCompleted() {
    if (isCompleted == true) return; // so we don't mess up the dates if set as complete again
    isCompleted = true;
    dateCompleted = LocalDate.now().toString();
  }

  /**
  * Gets the cost to producer to complete this order
  */
  public double getMyCost() {
    return myCost;
  }

  /**
  * Returns the number of this order
  */
  public int getNumber() {
    return orderNumber;
  }

}


import java.util.Scanner;
import java.util.function.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Class works as the sole way that the user can communicate with the program.
 * @author Andres Quintanal
 */ 
public class FrontEnd {
  private static Scanner input; //Scanner used through the program to get input from user
  
  // the next 6 Strings are kept here instead of in the main body to reduce clutter.
  // They represent the choices the user has in the different menus
  private static String main = // used in mainMenu()
      "Please select an option (0-4):\n"
    + "[0] Add new order\n"
    + "[1] Explore or change status of past orders\n"
    + "[2] List all orders that have not been completed\n"
    + "[3] Calculate amount of money needed to complete all incomplete orders\n"
    + "[4] Save and quit\n";

  private static String incomplete =  // used in incomplete()
      "Please select an option (0-3):\n"
    + "[0] Print all orders with full details\n"
    + "[1] Print only all order numbers\n"
    + "[2] Set all these orders as complete\n"
    + "[3] Return to main menu\n";

  private static String findOrder = // used in findOrder()
      "Please select an option (0-2):\n"
    + "[0] Find an order by client name\n"
    + "[1] Find an order by order number\n"
    + "[2] Return to main menu\n";

  private static String findHelper = // used in findOrderHelper()
      "Please select and option (0-2):\n"
    + "[0] Set order as complete\n"
    + "[1] Search for another order\n"
    + "[2] Return to main menu\n";

  private static String addOrder = // used in addOrder()
      "Please select an option (0-2):\n"
    + "[0] Add more cookies\n"
    + "[1] Finish order\n"
    + "[2] Review cookies added to order\n";

  private static String cookieChoices = // used in makeCookiePack()
      "Choose a type of cookie (0-5):\n"
    + "[0] Macadamia\n"
    + "[1] Ferrero Rocher\n"
    + "[2] Chocolate Chip\n"
    + "[3] Triple Chocolate\n"
    + "[4] Almond and Chocolate Chips\n"
    + "[5] Pecan and Chocolate Chips\n";
  
  private static String greet =
      "===================================\n"
    + "== Welcome to the Cookie Manager ==\n"
    + "===================================\n";	      

  /**
   * Functions as the main menu of the program. Here user can choose what they want to do
   */
  public static void mainMenu() {
    System.out.println(greet);
    input = new Scanner(System.in);
    boolean done = false;

    while (!done) {
      System.out.println(main);
      switch (getValidInt()) {
        case 0: // add an order
          addOrder();
          continue;
        case 1: // find an order
          findOrder();
          continue;
        case 2: // deal with incomplete orders
          incomplete();
          continue;
        case 3: // calculate cost to produce all the incomplete orders
          allOrdersCost();
          continue;
        case 4: // exit program
          System.out.println("Storing data. Please do not turn off.");
          done = true;
          break;
        default: // input was not a valid choice
          continue;
      }
    }
  }

  /**
   * Menu where the user can add a new order to the program
   */
  private static void addOrder() {
    System.out.println("First, enter the client's first name:");
    String firstName = input.nextLine().trim().toLowerCase();
    System.out.println("Now, enter the client's last name:");
    String lastName = input.nextLine().trim().toLowerCase();
    String clientName = firstName + " " + lastName;
    Client client;

    // get the client part of the order
    try {
      // checks if client already in database. If not, throws exception
      client = BackEnd.getClient(clientName);
      System.out.println("Found a matching client:\n" + client);
      System.out.println("Is this the right client? y/n:");
      switch (input.nextLine().trim().toLowerCase()) {
        case "y":
        case "yes": // don't have to make a new client!
          break;
        case "n":
        case "no":
          System.out
              .println("Returning to main menu. Remember two clients can't have the same name");
          return;
        default:
          System.out.println("You should've typed yes or no. Going back to Main menu"); //TODO get rid of passive aggressive comments and make this easier to use
          return;
      }
    } 
    catch (NoSuchElementException e) { // thrown if this is a new client
      System.out.println("This client was not found in the system. Please add client's data");
      System.out.println("First, enter client's email:");
      String email = input.nextLine().trim().toLowerCase();
      System.out.println("Now, enter the client's phone:");
      String phone = input.nextLine().trim().toLowerCase();
      System.out.println("Finally, enter the client's address:");
      String address = input.nextLine().trim().toLowerCase();
    client = new Client(firstName, lastName, email, phone, address); 

      
      // checks that the given data is correct
      System.out.println("Does this look right to you? Choose yes or no.\n" + client);
      switch (input.nextLine().toLowerCase()) {
        case "y":
        case "yes":
          BackEnd.addClient(clientName, client);
          break;
        case "n":
        case "no": // TODO make it easy to fix data
          System.out.println("Be more careful with your typing next time. Returning to Main menu");
          return;
        default: // TODO get rid of passive agressive comments and just loop back instead of going to main menu
          System.out.println("You should've typed yes or no. Going back to Main menu");
          return;
      }
    }

    // get the cookie part of the order
    boolean done = false;
    ArrayList<CookiePack> cookies = new ArrayList<>();

    while (!done) {
      System.out.println(addOrder);
      switch (getValidInt()) { //TODO add a way to remove cookies if added incorrectly
        case 0: // add more cookies
          cookies.add(makeCookiePack());
          continue;
        case 1: // finish order
          if (cookies.size() < 1) { // order needs to have cookies
            System.out.println("You have to add cookies to the order to complete it");
            continue;
          }
          Order newOrder = new Order(cookies, client);
          BackEnd.addOrder(newOrder);
          client.addOrder(newOrder.getNumber());
          done = true;
          System.out.println("Order added succesfully. Returning to main menu.\n");
          continue;
        case 2: // review order
          System.out.println("These are the cookies in the order:");
          for (CookiePack pack : cookies) {
            System.out.println(pack);
          }
          continue;
        default: // choice not valid
          continue;
      }
    }
  }

  /**
   * Helper method of addOrder that creates CookiePacks
   * @return the CookiePack created
   */
  private static CookiePack makeCookiePack() {
    String type = "";
    boolean done = false;
    while (!done) { // gets cookie type
      System.out.println(cookieChoices);
      switch (getValidInt()) {
        case 0:
          type = "macadamia";
          done = true;
          continue;
        case 1:
          type = "ferrero";
          done = true;
          continue;
        case 2:
          type = "chocolate chip";
          done = true;
          continue;
        case 3:
          type = "triple chocolate";
          done = true;
          continue;
        case 4:
          type = "almond";
          done = true;
          continue;
        case 5:
          type = "pecan";
          done = true;
          continue;
        default: // choice not valid
          continue;
      }
    }
    
    // gets number of cookies of the chosen type
    int numCookies = 0;
    done = false;
    while (!done) {
      System.out.println("Choose the number of cookies of this type to add to order:");
      numCookies = getValidInt();
      if (numCookies > 0) break;
      System.out.println("Number has to be more than 0");
    }
    return new CookiePack(type, numCookies);
  }

  /**
   * Method where user can search for specific orders using an order number or a name
   */
  private static void findOrder() {
    boolean done = false;
    while (!done) {
      System.out.println(findOrder);
      switch (getValidInt()) {
        case 0: // search by client name
          System.out.println("Enter a client's first and last name separated by a space (eg \"firstname lastname\"):");
          String name = input.nextLine().toLowerCase().trim();
          try {
            ArrayList<Integer> orders = BackEnd.getClient(name).orders(); // gets order numbers, not actual Order
            System.out.println(
                "The given client has made " + orders.size() + " orders. Displaying them all:\n");
            for (Integer i : orders) {
              System.out.print(BackEnd.getOrder(i) + "\n\n*****************\n\n");
            }
          } 
          catch (NoSuchElementException e) { // client not in system
            System.out.println("The given name did not return any matches");
          }
          continue;
        case 1: // order number
          System.out.println("Enter an order number:");
          try {
            int orderNum = getValidInt();
            Order order = BackEnd.getOrder(orderNum);
            System.out.println("Found a matching order:\n" + order);
            done = findOrderHelper(order);
          } catch (NoSuchElementException e) {
            System.out.println("The given number did not return any matches");
          }
          continue;
        case 2: // main menu
          done = true;
          System.out.println("Going back to main menu");
          continue;
        default: // choice was not valid
          continue;
      }
    }
  }

  /**
   * Helper method to findOrder. Once an order is found, this method processes the ways the user interacts with the gotten order
   * @param order, the given order over which we the user can interact with
   * @return true if user wants to return to main menu, false if user wants to search for another order
   */
  private static boolean findOrderHelper(Order order) {
    boolean done = false;
    while (!done) {
      System.out.println(findHelper);
      switch (getValidInt()) {
        case 0: // set order as complete
          order.setCompleted();
          System.out.println("Order has been set as complete");
          continue;
        case 1: // search for another order
          return false;
        case 2: // return to main menu
          return true;
        default: // invalid choice
          continue;
      }

    }
    return true; // doesn't reach here, but included for compilation
  }

  /*
   * Finds the total cost to produce all incomplete orders 
   */
  private static void  allOrdersCost(){
    double cost = 0.0;
    ArrayList<Double> list = BackEnd.findOrders(o -> !o.isCompleted(), (o, l) -> l.add(o.getMyCost()));
    if(list.size() == 0) {
      System.out.println("There are no incomplete orders, the cost is $0.00");
      return;
    }
    for(Double d : list) cost += d;
    System.out.printf("The total cost to produce all incomplete orders is $%.2f\n", cost);
  }


  /**
   * Method where user can look at and interact with incomplete orders
   */
  private static void incomplete() {
    boolean done = false;
    ArrayList<Order> list = BackEnd.findOrders(o -> !o.isCompleted(), (o, l) -> l.add(o));
    if(list.size() == 0) {
      System.out.println("There are no incomplete orders.\n");
      return;
    }
    System.out.println("There's " + list.size() + " incomplete orders. What would you like to do?");
    while (!done) {
      System.out.println(incomplete);
      switch (getValidInt()) {
        case 0: // print all
          System.out.println("Incomplete orders:\n");
          for (Order o : list) {
            System.out.print(o + "\n\n***************\n\n");
          }
          continue;
        case 1: // print only the order numbers
          System.out.print("Incomplete orders: ");
          for (Order o : list) {
            System.out.print(o.getNumber() + ", ");
          }
          System.out.println("");
          continue;
        case 2: // set All the orders as complete
          for (Order o : list) {
            o.setCompleted();
          }
          continue;
        case 3:
          done = true;
          System.out.println("Going back to main menu\n");
          continue;
        default: // invalid choice
          continue;
      }
    }
  }

  /**
   * Ensures that int input is valid and won't crash the program
   */
  private static int getValidInt() {
    String in = input.nextLine();
    Scanner scanner = new Scanner(in);
    if (!scanner.hasNextInt()) {
      System.out.println("This is not a valid input. It has been assigned a value of 999,999");
      scanner.close();
      return 999999;
    }
    scanner.close();
    return Integer.parseInt(in.trim());
  }
}

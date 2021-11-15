import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.function.*;
import java.util.NoSuchElementException;

/**
 * Interfaces front end with the hashtables where data is stored
 * @author Andres Quintanal Escandon
 */
public class BackEnd {

  // key is client firstName + " " + lastName, value is client
  private static HashTableMap<String, Client> clients = new HashTableMap<>(); 
  // key is order number, value is order
  // i know i could just use an array but I wanna feel cool ok?
  private static HashTableMap<Integer, Order> orders = new HashTableMap<>();

  /**
   * Main, calls the main menu and the save/load operations
   */
  public static void main(String[] args) {
    Order.nextOrderNumber = 0;
    load("data.txt");
    FrontEnd.mainMenu(); // goes to front end, and only returns when user quits
    save("data.txt");
  }

  /**
   * Reads all orders and clients from text file, and loads it into the hash tables upon startup
   * @param filename The file to load data from
   */
  private static void load(String filename) {
    try { 
      File data = new File(filename);
      Scanner input = new Scanner(data);

      // load clients from text file to hash table
      String currLine;
      Client currClient;
      while (input.hasNextLine()) {
        currLine = input.nextLine();
        if (currLine.equals("ORDERS")) break; // leaves loop when gets to orders
        currClient = new Client(currLine);
        clients.put(currClient.getName(), currClient);
      }

      // load orders from text file to hash table
      Order currOrder;
      while (input.hasNextLine()) {
        currLine = input.nextLine();
        currOrder = new Order(currLine);
        orders.put(currOrder.getNumber(), currOrder);
      }
      input.close();
    } catch (FileNotFoundException e) {
      System.out.println("A load file does not exist. Creating new load file"); 
      // will create a new file when saving
    }
  }

  /**
   * Saves all the Clients and Orders from hash table to a text file when the user quits the program
   * @param filename the file to save data in
   */
  private static void save(String filename) {
    try {
      File data = new File(filename);
      PrintWriter out = new PrintWriter(data);

      // save clients
      ArrayList<Client> storedClients = clients.getAll();
      for (Client c : storedClients) {
        out.println(c.toLoadString());
      }

      // save orders
      ArrayList<Order> storedOrders = orders.getAll();
      out.println("ORDERS");
      for (Order o : storedOrders) {
        out.println(o.toLoadString());
      }
      out.close();
      System.out.println("Data saved successfully");
    } catch (FileNotFoundException e) {
      System.out.println("Trouble saving. Changes made in this session will be lost");
    }
  }

  /**
   * Finds data from multiple orders, specified by FrontEnd
   * @param tester tests an order
   * @param action does an action with an order and the array list
   * @return ArrayList<T> an ArrayList with the data retrieved
   */
  protected static <T> ArrayList<T> findOrders(Predicate<Order> tester,
      BiConsumer<Order, ArrayList<T>> action) {
    int i = 0;
    Order order;
    ArrayList<T> list = new ArrayList<T>();
    try {
      while (true) { // will finish when i overtakes # of orders, then exit through exception
        order = orders.get(i);
        if (tester.test(order)) {
          action.accept(order, list);
        }
        i++;
      }
    } catch (NoSuchElementException e) {} // will always happen, do nothing
    return list;
  }

  /**
   * Gets a client given their name.
   * @param name the name of the client, represented as firstName + " " + lastName
   * @return Client, the client with the given name
   */
  protected static Client getClient(String name) {
    return clients.get(name);
  }

  /**
   * Gets an order given its number
   * @param orderNumber the number of the order we're looking for
   * @return the order we were looking for
   */
  protected static Order getOrder(int orderNumber) {
    return orders.get(orderNumber);
  }

  /**
   * Adds a new client to the hash table
   */
  protected static void addClient(String name, Client client) {
    clients.put(name, client);
  }

  /**
   * Adds a new order to the hash table
   */
  protected static void addOrder(Order order) {
    orders.put(order.getNumber(), order);
  }
}

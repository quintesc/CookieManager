import java.util.ArrayList;

/**
 * Class that models a client
 * @author Andres Quintanal Escandon
 */
public class Client {
  private String firstName; // all fields pretty self-explanatory
  private String lastName;
  private String email;
  private String phone;
  private String address;
  private ArrayList<Integer> orders; // array with all order numbers this client has made

  /**
   * Constructs a Client. Assumes all input has already been proved valid by front-end
   */
  public Client(String firstName, String lastName, String email, String phone, String address) {
    this.firstName = firstName.toLowerCase(); // all String data stored in lower case
    this.lastName = lastName.toLowerCase();
    this.email = email.toLowerCase();
    this.phone = phone;
    this.address = address;
    orders = new ArrayList<>();
  }

  /**
   * Constructs a client using a string loaded from the text file
   */
  protected Client(String loadString) {
    String[] data = loadString.split(">");
    firstName = data[0]; // the numbers are just the way the data is saved, see toLoadString() below
    lastName = data[1];
    email = data[2];
    phone = data[3];
    address = data[4];
    orders = new ArrayList<>();
    String[] orderNums = data[5].split("<");
    for (String i : orderNums) {
      orders.add(Integer.parseInt(i));
    }
  }

  /**
   * Creates a string version of this client for saving and loading purposes
   */
  protected String toLoadString() {
    String s = firstName + ">" + lastName + ">" + email + ">" + phone + ">" + address + ">";
    s = s + orders.get(0);
    for (int i = 1; i < orders.size(); i++) {
      s = s + "<" + orders.get(i);
    }
    return s;
    // example s:
    // first>last>email>phone>address>o[0]<o[1]<o[2]
  }
  
  /**
   * Creates a string version of the client
   * @return string version of the client
   */
  @Override
  public String toString() {
    return "Name:\t\t" + firstName + "\n" 
         + "Last Name:\t" + lastName + "\n" 
         + "Email:\t\t" + email + "\n" 
         + "Phone number:\t" + phone + "\n" 
         + "Address:\t" + address + "\n";
  }

  /**
   * Gets this client's orders
   */
  public ArrayList<Integer> orders() {
    return orders;
  }

  /**
   * Adds an order to this client's order numbers
   */
  public void addOrder(int orderNumber) {
    orders.add(orderNumber);
  }

  /**
   * Gets the client's name in the format firstName + " " + lastName
   */
  public String getName() {
    return firstName + " " + lastName;
  }
}


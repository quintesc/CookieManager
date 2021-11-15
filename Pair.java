/**
 * Models a pair which contains the key and the value of a key-value for a hash table
 * @author Andres Quintanal Escandon
 */
public class Pair<KeyType,ValueType> {
  private KeyType key;
  private ValueType value;
  
  /**
   * Constructs a pair
   */
  public Pair(KeyType key, ValueType value) {
    this.key = key;
    this.value = value;
  }
  
  /**
   * Returns the key of the key-value pair
   */
  public KeyType getKey() {
    return key;
  }
  
  /**
   * Returns the value of the key-value pair
   */
  public ValueType getValue() {
    return value;
  }
}


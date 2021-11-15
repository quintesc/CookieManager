import java.util.NoSuchElementException;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Models a hash table, storing key value pairs
 * @author Andres Quintanal Escandon
 */
public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType>{
  
  private LinkedList<Pair<KeyType,ValueType>>[] hashTable; // the hash table
  private int capacity; // how many key-value pairs hash table can store
  private int size;     // number of key-value pairs in hash table
  
  
  /**
   * Constructor for a hash table with a given capacity
   * @param capacity initial capacity
   */
  @SuppressWarnings("unchecked")
  public HashTableMap(int capacity) {
    if(capacity < 1) throw new IllegalArgumentException("Capacity must be at least 1");
    
    // initializes fields
    this.capacity = capacity;
    size = 0;
    hashTable = (LinkedList<Pair<KeyType, ValueType>>[]) new LinkedList[capacity];
    
    // initializes linked lists in each index of the hash table
    for(int i = 0; i < capacity; i++) {
      hashTable[i] = new LinkedList<Pair<KeyType, ValueType>>();
    }
  }
  
  /**
   * Default constructor for a hash table, defaults to a capacity of 10
   */
  @SuppressWarnings("unchecked")
  public HashTableMap() {
    capacity = 10;
    size = 0;
    hashTable = (LinkedList<Pair<KeyType, ValueType>>[]) new LinkedList[capacity];

    // initializes linked lists in each index of the hash table
    for (int i = 0; i < capacity; i++) {
      hashTable[i] = new LinkedList<Pair<KeyType, ValueType>>();
    }

  }

  /**
   * Adds a key-value pair to the hash table
   * @param key the key of the pair
   * @param value the value of the pair
   * @return true if successfully added pair to the hash table, false if key already exists in table
   * or if key is null
   */
  @Override
  public boolean put(KeyType key, ValueType value) {
    if(containsKey(key) || key == null) return false;
    
    // adds the key-value pair to the hashTable
    int index = Math.abs(key.hashCode()) % capacity;
    hashTable[index].add(new Pair<KeyType, ValueType>(key,value));
    size++;
    
    // check if hash table is over an 85% load factor. If so, doubles capacity and rehashes
    if(((double)size/capacity) * 100 >= 85) rehash();
    
    return true;
  }

  /**
   * Given a key, gets its associated value from the hash table
   * @param key the key to find the associated value of
   * @return the value associated with the given key
   * @throws NoSuchElementException if the given key is not contained in the hash table
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    // finds index in hash table where key would be
    int index = Math.abs(key.hashCode()) % capacity;
   
    // iterates over linkedList at index to find specified key
    for(int i = 0; i < hashTable[index].size(); i++) {
      if(hashTable[index].get(i).getKey().equals(key)) {
        return hashTable[index].get(i).getValue();
      }
    }
    
    // if key not found, throw exception
    throw new NoSuchElementException("Key not found in hash table");
  }

  /**
   * returns the number of elements stored in the hash table
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Searches the hash table to find a match for a given key
   * @param key The key to search for
   * @return true if hash table contains key, false otherwise
   */
  @Override
  public boolean containsKey(KeyType key) {
    // finds index in hash table where key would be
    int index = Math.abs(key.hashCode()) % capacity;
   
    // iterates over linkedList at index to find a matching key
    for(int i = 0; i < hashTable[index].size(); i++) {
      if(hashTable[index].get(i).getKey().equals(key)) return true;
    }
   
    // if match not found, returns false
    return false;
  }

  /**
   * Removes a key-value pair from the hash table given the key
   * @param key the key of the key-value pair to be removed
   * @return value associated with the given key, or null if key did not match any pairs in table
   */
  @Override
  public ValueType remove(KeyType key) {
    // finds index in hash table where key would be
    int index = Math.abs(key.hashCode()) % capacity;
   
    // iterates over linkedList at index to find a match and then removes it if found
    for(int i = 0; i < hashTable[index].size(); i++) {
      if(hashTable[index].get(i).getKey().equals(key)) {
        ValueType value = hashTable[index].get(i).getValue();
        hashTable[index].remove(i);
        return value;
      }
    }
    return null; // if key-value pair wasnt found
  }

  /**
   * Removes all key-value pairs from the hash table.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void clear() {
    // pretty much constructs again
    capacity = 10;
    size = 0;
    hashTable = (LinkedList<Pair<KeyType, ValueType>>[]) new LinkedList[capacity];

    for (int i = 0; i < capacity; i++) {
      hashTable[i] = new LinkedList<Pair<KeyType, ValueType>>();
    }
  }

  /**
   * Gets all the elements of this hash table in no specific order
   */
  public ArrayList<ValueType> getAll() {
    ArrayList<ValueType> listWithAll = new ArrayList<ValueType>();
    for (LinkedList<Pair<KeyType, ValueType>> list : hashTable) { // for each list in table
      for (Pair<KeyType, ValueType> pair : list) { // for each pair in list
        listWithAll.add(pair.getValue());
      }
    }
    return listWithAll;
  }

  /**
   * Doubles capacity of hash table and rehashes every element in the hash table
   */
  @SuppressWarnings("unchecked")
  private void rehash() {
    // creates new hash table with double the capacity
    int oldCapacity = capacity;
    capacity *= 2;
    LinkedList<Pair<KeyType, ValueType>>[] newTable =
        (LinkedList<Pair<KeyType, ValueType>>[]) new LinkedList[capacity];

    // initializes linked lists in each index of the hash table
    for (int i = 0; i < capacity; i++) {
      newTable[i] = new LinkedList<Pair<KeyType, ValueType>>();
    }

    // adds all elements of old table into new table
    for (int i = 0; i < oldCapacity; i++) { // oldCapacity because we're going over all elements in og table
      for (int j = 0; j < hashTable[i].size(); j++) { 
        int index = Math.abs(hashTable[i].get(j).getKey().hashCode()) % capacity;
        newTable[index].add(hashTable[i].get(j));
      }
    }
    hashTable = newTable;
  }
}


import java.util.NoSuchElementException;

/**
 * Interface for a hash table, storing key value pairs
 * @author Andres Quintanal Escandon
 */
public interface MapADT<KeyType, ValueType> {

    public boolean put(KeyType key, ValueType value);
    
    public ValueType get(KeyType key) throws NoSuchElementException;
    
    public int size();
    
    public boolean containsKey(KeyType key);
    
    public ValueType remove(KeyType key);
    
    public void clear();
    
}

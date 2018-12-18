package de.tub.ise.ec.kv;

import java.io.Serializable;
import java.util.List;

/**
 * Specifies the generic interface of a key value store.
 * Keys are always Strings, whereas values are of type Serializable, i.e., any Java Object that can be serialized.
 */
public interface KeyValueInterface {

    /**
     * returns a value for a given key
     * @param key
     * @return
     */
    Object getValue(String key);

    /**
     * returns a list of all keys
     */
    List<String> getKeys();

    /**
     * stores a key value pair
     * @param key
     * @param value
     */
    void store(String key, Serializable value);

    /**
     * deletes the key value pair for a given key
     * @param key
     */
    void delete(String key);


}

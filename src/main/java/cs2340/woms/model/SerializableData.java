package cs2340.woms.model;

import java.util.Map;

/**
 * Denotes an object which can be serialized to allow for persistance of data.
 * Classes inheriting from this interface must provide a public no argument
 * constructor.
 */
public interface SerializableData {

    /**
     * Serializable classes should use this method to write any non-transient
     * data as key-value pairs in the given map.
     *
     * @param writeData the map to which serializable objects should write
     * their data to.
     * @return a map of all data which should be saved. Usually this will be
     * the same map that was passed in.
     */
    Map<String, String> write(Map<String, String> writeData);

    /**
     * Serializable classes should use this method to read any data saved using
     * {@link #write(Map)} to restore their previous state.
     *
     * @param readData a map of key value pairs created during the last call
     * to {@link #write(Map)}.
     */
    void read(Map<String, String> readData);

}

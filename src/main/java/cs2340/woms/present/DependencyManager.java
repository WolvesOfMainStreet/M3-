package cs2340.woms.present;

import java.util.HashMap;
import java.util.Map;

/**
 * A basic dependency injection manager.
 */
public final class DependencyManager {

    private static Map<Class<?>, Class<?>> bindings = new HashMap<Class<?>, Class<?>>();

    // No instances, all methods are class methods.
    private DependencyManager() { }

    /**
     * Binds the given implementation class to the given interface.
     *
     * @param interfaceClass the interface class to bind the implementation to.
     * @param implClass the implementation class to bind to the interface.
     */
    public static void bind(Class<?> interfaceClass, Class<?> implClass) {
        bindings.put(interfaceClass, implClass);
    }

    /**
     * Returns the implementation class associated with an interface, if any.
     *
     * @param interfaceClass the interface to search for an implementation of.
     * @return the most recently bound implementation, or null if no
     * implementation has been bound at all.
     */
    public static Class<?> getImplementation(Class<?> interfaceClass) {
        return bindings.get(interfaceClass);
    }

    /**
     * Returns an instance of the implementation associated with the given
     * interface.
     *
     * @param interfaceClass the interface to create an implementation instance
     * of.
     * @return a new instance of the most recently bound implementation.
     */
    public static Object createImplementation(Class<?> interfaceClass) {
        try {
            // For now, just use default constructor. Future versions may check
            // constructor arguments recursively against the bindings as well.
            return getImplementation(interfaceClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package cs2340.woms.present;

import java.util.HashMap;
import java.util.Map;

/**
 * A basic dependency injection manager.
 */
public final class DependencyManager {

    // I would parameterize this if I could, to Map<Class<T>, Class<? extends T>>
    /**A map of interface to implementation bindings.*/
    private static Map<Class<?>, Class<?>> bindings = new HashMap<Class<?>, Class<?>>();

    /**No instances, all methods are class methods.*/
    private DependencyManager() { }

    /**
     * Binds the given implementation class to the given interface.
     *
     * @param interfaceClass the interface class to bind the implementation to.
     * @param implClass the implementation class to bind to the interface.
     * @param <T> the type of the interface. The type of the implementation
     * must extend this type.
     */
    public static <T> void bind(Class<T> interfaceClass, Class<? extends T> implClass) {
        bindings.put(interfaceClass, implClass);
    }

    /**
     * Returns the implementation class associated with an interface, if any.
     *
     * @param interfaceClass the interface to search for an implementation of.
     * @param <T> then type of the interface. The returned implementation class
     * will extend this type.
     * @return the most recently bound implementation, or null if no
     * implementation has been bound at all.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> getImplementation(Class<T> interfaceClass) {
        return (Class<? extends T>) bindings.get(interfaceClass);
    }

    /**
     * Returns an instance of the implementation associated with the given
     * interface.
     *
     * @param interfaceClass the interface to create an implementation instance
     * of.
     * @param <T> the type of the interface. The returned object will be of
     * this type.
     * @return a new instance of the most recently bound implementation.
     * @throws IllegalAccessException if the default constructor is not public.
     * @throws InstantiationException if some other error occurs during
     * instantiation.
     */
    public static <T> T createImplementation(Class<T> interfaceClass) throws InstantiationException, IllegalAccessException {
        // For now, just use default constructor. Future versions may check
        // constructor arguments recursively against the bindings as well.
        return getImplementation(interfaceClass).newInstance();
    }
}

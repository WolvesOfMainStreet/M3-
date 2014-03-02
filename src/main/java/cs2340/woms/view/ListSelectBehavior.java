package cs2340.woms.view;

/**
 * Behavior to be executed when an element of a list is selected.
 */
public interface ListSelectBehavior<T> {

    /**
     * Should get called whenever an element in a list is selected.
     *
     * @param t the selected element.
     */
    void select(T t);
}

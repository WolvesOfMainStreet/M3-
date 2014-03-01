package cs2340.woms.view.screens;

/**
 * The base screen interface. All screens should support the operations outlined
 * in this interface.
 */
public interface BaseScreen {

    /**
     * Opens the given screen on top of this one.
     *
     * @param screen the screen to open.
     */
    void open(Class<?> screen);

    /**
     * Opens a popup message.
     *
     * @param message the message to display.
     */
    void popup(String message);
}

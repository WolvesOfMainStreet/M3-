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
     * Closes this screen, opening its parent if it has one and closing the
     * window if it doesn't.
     */
    void close();

    /**
     * Opens a popup message.
     *
     * @param message the message to display.
     */
    void popup(String message);

    // TODO: add on close and possibly on open events. Why? For automatic logout
}

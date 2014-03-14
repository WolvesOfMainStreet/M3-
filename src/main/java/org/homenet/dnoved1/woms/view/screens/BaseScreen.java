package org.homenet.dnoved1.woms.view.screens;

import java.util.Map;

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
    void open(Class<? extends BaseScreen> screen);

    /**
     * Opens the given screen on top of this one.
     *
     * @param screen the screen to open.
     * @param args additional arguments to pass to the new screen.
     */
    void open(Class<? extends BaseScreen> screen, Map<String, String> args);

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

package org.homenet.dnoved1.woms.swing.view.screens;

import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.homenet.dnoved1.woms.view.screens.BaseScreen;

/**
 * The swing implementation of {@link BaseScreen}.
 */
public abstract class SwingBaseScreen implements BaseScreen {

    /**The parent screen, or null if this screen is the root screen.*/
    private SwingBaseScreen parent;
    /**The window in which this screen is hosted.*/
    private JFrame frame;

    @Override
    public void open(Class<? extends BaseScreen> screen) {
        createScreen(screen).construct();
    }

    @Override
    public void open(Class<? extends BaseScreen> screen, Map<String, String> args) {
        createScreen(screen).construct(args);
    }

    /**
     * Creates and initializes a screen. The given screen should be an instance
     * of SwingBaseScreen.
     *
     * @param screen the screen to create.
     * @return the screen after it has been opened.
     */
    private SwingBaseScreen createScreen(Class<? extends BaseScreen> screen) {
        SwingBaseScreen newScreen;
        try {
            newScreen = (SwingBaseScreen) screen.newInstance();
        } catch (InstantiationException e) {
            // Should never happen under normal circumstances.
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            // Should never happen under normal circumstances, unless a screen
            // doesn't have a public no-args constructor.
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            // Should never happen under normal circumstances, unless someone
            // accidentally bound a non-swing screen to DependencyManager.
            throw new RuntimeException(e);
        }

        newScreen.parent = this;
        newScreen.frame = this.frame;
        return newScreen;
    }

    @Override
    public void close() {
        if (parent == null) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        } else {
            parent.reconstruct();
        }
    }

    @Override
    public void popup(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    /**
     * Returns the window in which this screen is being hosted.
     *
     * @return the window in which this screen is being hosted.
     */
    protected JFrame getFrame() {
        return frame;
    }

    /**
     * Will be called when this screen is first created. Screens should clear
     * any old components and set up their own here.
     */
    public abstract void construct();

    /**
     * Will be called when this screen is first created with the given
     * arguments. Screens should clear any old components and set up their own
     * here. By default simply calls {@link #construct()}, ignoring the
     * arguments.
     *
     * @param args the arguments passed to this screen when it was opened from
     * another screen.
     */
    public void construct(Map<String, String> args) {
        construct();
    }

    /**
     * Will be called whenever this screen is recreated. Screens should clear
     * any old components and re-add their own here.
     */
    public abstract void reconstruct();
}

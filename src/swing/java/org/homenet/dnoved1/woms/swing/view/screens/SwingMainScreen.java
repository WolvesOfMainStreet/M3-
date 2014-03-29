package org.homenet.dnoved1.woms.swing.view.screens;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.homenet.dnoved1.woms.present.DependencyManager;
import org.homenet.dnoved1.woms.present.MainPresenter;
import org.homenet.dnoved1.woms.view.screens.MainScreen;

/**
 * The swing implementation of {@link MainScreen}.
 */
public class SwingMainScreen extends SwingBaseScreen implements MainScreen {

    /**The application's name, used for setting the window title.*/
    public static final String APP_NAME = "WOMS";

    /**The presenter for this screen.*/
    private MainPresenter presenter;

    {
        this.presenter = new MainPresenter(this);
    }

    @Override
    public void construct() {
        JFrame frame = this.getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome to the Wolfpack!");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(welcomeLabel);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.onLoginButtonPressed();
            }
        });
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(loginButton);
        frame.pack();
    }

    @Override
    public void reconstruct() {
        construct();
    }

    /**
     * Main method for starting the swing version of the application.
     *
     * @param args the command line arguments. Ignored.
     */
    public static void main(String[] args) {
        initializeSwingEnvironment();

        JFrame frame = new JFrame();
        frame.setTitle(APP_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingMainScreen mainScreen = new SwingMainScreen();
        try {
            Field frameField = SwingBaseScreen.class.getDeclaredField("frame");
            frameField.setAccessible(true);
            frameField.set(mainScreen, frame);
            frameField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            // Indicates that the 'frame' field in SwingBaseScreen has been
            // renamed.
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            // Should never happen unless somehow SwingMainScreen is not an
            // instance of SwingBaseScreen.
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            // Should never happen, as the field was set accessible.
            throw new RuntimeException(e);
        }
        mainScreen.construct();
        frame.setVisible(true);
    }

    /**
     * Initializes the environment as swing by binding swing specific
     * implementations.
     */
    private static void initializeSwingEnvironment() {
        //-----Screens----------------------------------------------------------
        DependencyManager.bind(MainScreen.class, SwingMainScreen.class);
    }
}

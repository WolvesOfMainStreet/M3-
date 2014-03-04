package cs2340.woms.present;

import java.math.BigDecimal;

import cs2340.woms.model.BaseModel;
import cs2340.woms.model.FinanceAccount;
import cs2340.woms.model.Transaction;
import cs2340.woms.view.ListSelectBehavior;
import cs2340.woms.view.screens.AccountCreationScreen;
import cs2340.woms.view.screens.AccountManagementScreen;
import cs2340.woms.view.screens.BaseScreen;
import cs2340.woms.view.screens.LoginScreen;
import cs2340.woms.view.screens.MainScreen;
import cs2340.woms.view.screens.RegistrationScreen;
import cs2340.woms.view.screens.TransactionCreationScreen;
import cs2340.woms.view.screens.TransactionHistoryScreen;

/**
 * The presenter class. This class handles most of the main application logic
 * and links the model to the view.
 */
public final class Presenter {

    private static BaseModel model;
    private static FinanceAccount currentAccount;

    // No instances, all methods are class methods.
    private Presenter() { }

    static {
        model = (BaseModel) DependencyManager.createImplementation(BaseModel.class);
        model.register("admin", "pass123");
    }

    public static void initMainScreen(final MainScreen screen) {
        screen.setLoginButtonBehavior(new OpenScreen(screen, LoginScreen.class));
    }

    public static void initLoginScreen(final LoginScreen screen) {
        screen.setLoginButtonBehavior(new Runnable() {
            @Override
            public void run() {
                String username = screen.getUsernameField();
                String password = screen.getPasswordField();

                if (model.login(username, password)) {
                    screen.open(DependencyManager.getImplementation(AccountManagementScreen.class));
                } else {
                    screen.popup("Incorrect username or password.");
                }
            }
        });

        screen.setRegisterButtonBehavior(new OpenScreen(screen, RegistrationScreen.class));
    }

    public static void initAccountCreationScreen(final AccountCreationScreen screen) {
        screen.setConfirmButtonBehavior(new Runnable() {
            @Override
            public void run() {
                String name = screen.getNameField();
                String balanceString = screen.getBalanceField();
                BigDecimal balance = null;

                String error = null;

                if ("".equals(name)) {
                    error = "Name field cannot be empty.";
                } else if ("".equals(balanceString)) {
                    error = "Balance field cannot be empty.";
                }

                if (error == null) {
                    try {
                        balance = new BigDecimal(balanceString);
                    } catch (NumberFormatException e) {
                        error = "Balance field is not a number.";
                    }
                }

                if (error != null) {
                    screen.popup(error);
                } else {
                    FinanceAccount newAccount = new FinanceAccount(name, balance);
                    model.addAccount(newAccount);
                    screen.close();
                }
            }
        });

        screen.setCancelButtonBehavior(new CloseScreen(screen));
    }

    public static void initAccountManagementScreen(final AccountManagementScreen screen) {
        screen.setCreateAccountButtonBehavior(new OpenScreen(screen, AccountCreationScreen.class));

        screen.setAccountListSelectBehavior(new ListSelectBehavior<FinanceAccount>() {
            @Override
            public void select(FinanceAccount account) {
                currentAccount = account;
                screen.open(DependencyManager.getImplementation(TransactionHistoryScreen.class));
            }
        });

        model.registerAccountsObserver(screen.getAccountListObserver());
    }

    public static void initRegistrationScreen(final RegistrationScreen screen) {
        screen.setConfirmButtonBehavior(new Runnable() {
            @Override
            public void run() {
                String username = screen.getUsernameField();
                String password1 = screen.getPasswordField();
                String password2 = screen.getConfirmPasswordField();

                String error = null;

                if (username.equals("")) {
                    error = "Username cannot be empty.";
                } else if (password1.equals("") || password2.equals("")) {
                    error = "Password fields cannot be empty.";
                } else if (!password1.equals(password2)) {
                    error = "Password fields do no match.";
                } else if (!model.register(username, password1)) {
                    error = "Could not create account with that username.";
                }

                if (error != null) {
                    screen.popup(error);
                } else {
                    screen.close();
                }
            }
        });

        screen.setCancelButtonBehavior(new CloseScreen(screen));
    }

    public static void initTransactionCreationScreen(final TransactionCreationScreen screen) {
        screen.setConfirmButtonBehavior(new Runnable() {
            @Override
            public void run() {
                String amountString = screen.getAmountField();
                BigDecimal amount = null;

                String error = null;

                if ("".equals(amountString)) {
                    error = "Must set the amount.";
                }

                if (error == null) {
                    try {
                        amount = new BigDecimal(amountString);
                    } catch (NumberFormatException e) {
                        error = "Amount field is not a number.";
                    }
                }

                if (error != null) {
                    screen.popup(error);
                } else {
                    Transaction newTransaction = new Transaction(amount);
                    model.addTransaction(currentAccount, newTransaction);
                    screen.close();
                }
            }
        });

        screen.setCancelButtonBehavior(new CloseScreen(screen));
    }

    public static void initTransactionHistoryScreen(final TransactionHistoryScreen screen) {
        screen.setCreateTransactionButtonBehavior(new OpenScreen(screen, TransactionCreationScreen.class));

        model.registerTransactionsObserver(currentAccount, screen.getTransactionListObserver());
    }

     /**
      * Convenience behavior which simply opens the designated screen.
      */
    private static class OpenScreen implements Runnable {

        private BaseScreen screen;
        private Class<?> classToOpen;

        public OpenScreen(BaseScreen screen, Class<?> classToOpen) {
            this.screen = screen;
            this.classToOpen = classToOpen;
        }

        @Override
        public void run() {
            screen.open(DependencyManager.getImplementation(classToOpen));
        }
    }

    /**
     * Convenience behavior which simply closes the designated screen.
     */
    private static class CloseScreen implements Runnable {

        private BaseScreen screen;

        public CloseScreen(BaseScreen screen) {
            this.screen = screen;
        }

        @Override
        public void run() {
            screen.close();
        }
    }
}
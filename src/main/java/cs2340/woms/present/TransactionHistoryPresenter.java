package cs2340.woms.present;

import java.util.HashMap;
import java.util.Map;

import cs2340.woms.model.Account;
import cs2340.woms.model.BaseModel;
import cs2340.woms.model.Transaction;
import cs2340.woms.view.screens.TransactionCreationScreen;
import cs2340.woms.view.screens.TransactionHistoryScreen;

public class TransactionHistoryPresenter {

    protected TransactionHistoryScreen screen;
    protected Account account;

    public TransactionHistoryPresenter(TransactionHistoryScreen screen, Account account) {
        this.screen = screen;
        this.account = account;

        BaseModel model = DependencyManager.getModel();
        model.registerTransactionsObserver(account, screen.getTransactionListObserver());
    }

    public void onCreateDepositButtonPressed() {
        Map<String, String> args = new HashMap<String, String>();
        args.put(TransactionCreationScreen.ACCOUNT_NAME, account.getName());
        args.put(TransactionCreationScreen.ACCOUNT_BALANCE, account.getBalance().toEngineeringString());
        args.put(TransactionCreationScreen.TRANSACTION_TYPE, Transaction.TYPE_DEPOSIT);
        screen.open(DependencyManager.getImplementation(TransactionCreationScreen.class), args);
    }

    public void onCreateWithdrawalButtonPressed() {
        Map<String, String> args = new HashMap<String, String>();
        args.put(TransactionCreationScreen.ACCOUNT_NAME, account.getName());
        args.put(TransactionCreationScreen.ACCOUNT_BALANCE, account.getBalance().toEngineeringString());
        args.put(TransactionCreationScreen.TRANSACTION_TYPE, Transaction.TYPE_WITHDRAWAL);
        screen.open(DependencyManager.getImplementation(TransactionCreationScreen.class), args);
    }

    public void onTransactionSelected(Transaction transaction) { }
}

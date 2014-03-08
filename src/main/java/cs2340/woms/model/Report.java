package cs2340.woms.model;

/**
 * A report, summarizing some aspect of all the data stored in the model.
 */
public interface Report extends Displayable {

    /**
     * Accepts a user from which information can be gathered for this report.
     *
     * @param user the user object to get information from.
     */
    void accept(User user);

    /**
     * Accepts an account from which information can be gathered for this
     * report.
     *
     * @param account the account object to get information from.
     */
    void accept(Account account);

    /**
     * Accepts a transaction from which information can be gathered for this
     * report.
     *
     * @param transaction the transaction object to get information from.
     */
    void accept(Transaction transaction);
}

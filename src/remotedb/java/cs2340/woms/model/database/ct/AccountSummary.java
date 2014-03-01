package cs2340.woms.model.database.ct;

import java.util.Date;


/**
 * A complex type for account summaries, with the account's name and date of
 * last edit. This time can be checked against another account with the same
 * name to determine if one needs to be updated with the other's details.
 *
 * @see cs2340.woms.model.database.ct.Account
 */
public class AccountSummary {

    private String name;
    private Date lastEdited;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }
}

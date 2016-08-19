package transactionservice;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Matthew Orlinski on 17/08/2016.
 */
public class Transaction {

    private final Long transaction_id;
    private final String type;
    private final double amount;
    private final Long parent_id;

    public Transaction(double amount, String type, Long transaction_id, Long parent_id) {
        this.amount = amount;
        this.type = type;
        this.transaction_id = transaction_id;
        this.parent_id = parent_id;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public Long getParent_id() {
        return parent_id;
    }

    @JsonIgnore
    public Long getTransaction_id() {
        return transaction_id;
    }

}

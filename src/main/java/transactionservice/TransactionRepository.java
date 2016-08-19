package transactionservice;

import java.util.*;

/**
 * Created by Matthew Orlinski on 17/08/2016.
 */
public class TransactionRepository {

    // Transaction store
    public Map<Long, Transaction> transactions = new HashMap<Long, Transaction>();

    // Note: I tried to think of a single data store which would give good complexity for all the operations in the spec
    // but I ended up creating these lookup/linking tables which sacrifices space but makes returning data faster.

    public Map<String, List> types = new HashMap<String, List>(); // All types and their transaction ids
    public Map<Long, List> parents = new HashMap<Long, List>(); // Which transactions are parents and their children

    /**
     * Store a transaction. This method also stores types and parent_ids seperately so we can look them up efficiently
     * TODO: Should this check for duplicate transaction_ids and loops?
     *
     * @param transaction_id
     * @param t1
     */
    public void storeTransaction(Long transaction_id, Transaction t1) {
        transactions.put(transaction_id, t1);

        // Store the types for efficient lookup
        String type = t1.getType();
        List<Long> ids = types.get(type);
        if(ids == null) {
            List li = new LinkedList<Long>();
            li.add(transaction_id);
            types.put(type, li);
        }
        else {
            ids.add(transaction_id);
            types.put(type, ids);
        }

        // Store the parents for efficient lookup
        Long parent = t1.getParent_id();
        // If the parent of this transaction_id is given we need to add transaction_id as (one of) its children
        if(parent != null) {
            List<Long> children = parents.get(parent);
            if (children == null) { // this parent has no children yet
                List li = new LinkedList<Long>();
                li.add(transaction_id);
                parents.put(parent, li);
            } else {
                ids.add(transaction_id);
                parents.put(parent, ids);
            }
        }

    }

    /**
     * Get a transaction from the transaction id
     *
     * @param transaction_id
     * @return Transaction
     */
    public Transaction getTransaction(Long transaction_id) {
        return transactions.get(transaction_id);
    }

    /**
     * Return a list of all IDs that share a given type
     *
     * @param type
     * @return LinkedList
     */
    public List<Long> getIDsFromType(String type) {
        return types.get(type);
    }

    /**
     * Recursively sum transactions with any others which have given transaction_id as their parent_id
     *
     * Note:    I allowed parent_id to be null so I need to be careful of that
     * Note:    I'll assume no loops and duplicate IDs to make this code simpler,
     *          but its possible with the current insert method
     * Question: A transaction can only have 1 parent, but can a parent have many children? (assume yes)
     *
     * @param transaction_id
     * @return double sum
     */
    public double sumWithChildren(Long transaction_id) {
        Transaction t1 = getTransaction(transaction_id);
        double sum = t1.getAmount();

        // now recursively check if this node is the parent of any others
        List<Long> children = parents.get(transaction_id);
        if (children != null) { // this node has children we need to sum as well
            for(Long id : children) {
                sum = sum + sumWithChildren(id);
            }
        }

        return sum;
    }

}

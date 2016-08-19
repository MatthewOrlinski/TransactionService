package transactionservice;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactionservice")
public class TransactionController {

    private final TransactionRepository transactionRepository;

    TransactionController() {
        this.transactionRepository = new TransactionRepository();
    }

    /**
     *  Puts a transaction into the TransactionRepository
     *  Note: IDs are objects and parent_id is null if its not set
     *
     * @param type
     * @param amount
     * @param parent_id
     * @return ResponseStatus
     */
    @RequestMapping(value = "/transaction/{transaction_id}", method = RequestMethod.PUT)
    public ResponseStatus putTransaction(@PathVariable Long transaction_id,
                                      @RequestParam(value="type",required=true) String type,
                                      @RequestParam(value="amount",required=true) double amount,
                                      @RequestParam(value="parent_id",required=false) Optional<Long> parent_id) {
        try {
            Transaction t1 = new Transaction(amount,
                    type,
                    transaction_id,
                    parent_id.isPresent() ? parent_id.get() : null);
            this.transactionRepository.storeTransaction(transaction_id, t1);
            return new ResponseStatus("OK");
        } catch (Exception e) {
            return new ResponseStatus("ERROR");
        }
    }

    /**
     * Get a transaction given the ID
     * Note: We don't want to return the transaction_id in the json
     *
     * @param transaction_id
     * @return Transaction
     */
    @RequestMapping(value = "/transaction/{transaction_id}", method = RequestMethod.GET)
    public Transaction readTransaction(@PathVariable Long transaction_id) {
        try {
            return this.transactionRepository.getTransaction(transaction_id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a list of all transaction_ids that share the same $type
     *
     * @param type
     * @return List
     */
    @RequestMapping(value = "/types/{type}", method = RequestMethod.GET)
    public List<Long> getTransactionIDs(@PathVariable String type) {
        try {
            return this.transactionRepository.getIDsFromType(type);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the sum of all transactions that are transitively linked by their $parent_id to $transaction_id,
     *
     * @param transaction_id
     * @return ResponseSum
     */
     @RequestMapping(value = "/sum/{transaction_id}", method = RequestMethod.GET)
     public ResponseSum sumTransactions(@PathVariable Long transaction_id) {
         try {
             double sum = this.transactionRepository.sumWithChildren(transaction_id);;
             return new ResponseSum(sum);
         } catch (Exception e) {
             return null;
         }
     }

}

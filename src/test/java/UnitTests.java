import org.junit.Test;
import transactionservice.Transaction;
import transactionservice.TransactionRepository;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Unit tests to test the transactionRepository.
 *
 * TODO: Only doing unit tests and no integration tests for the moment because I have no time.
 * TODO: I need to ask what should happen when loops and duplicate IDs are created.
 *
 */
public class UnitTests {

    /**
     * Store/Read a transaction
     */
    @Test
    public void testStoreAndReadTransaction() {

        TransactionRepository tr = new TransactionRepository();

        // The first store an example transaction given in the spec
        Transaction t1 = new Transaction(5000,
                "cars",
                new Long(10),
                null);
        tr.storeTransaction(new Long(10), t1);

        // Now see if we can retrieve it
        Transaction t2 = tr.getTransaction(new Long(10));

        // it should have the same values
        assertEquals("cars", t2.getType());
        assertEquals(new Long(10), t2.getTransaction_id());
        assertEquals(null, t2.getParent_id());
        assertEquals(5000, t2.getAmount(),0);
    }

    /**
     * Test summing "all transactions" (implies 2 or more) linked to a given transaction by their parent_id
     *
     * As well as using the example from the spec, I also test the sum method works when there is a longer chain, e.g.,
     *  12 has parent_id:11 -> 11 has parent_id:10 -> 10 has no parent id
     *
     */
    @Test
    public void testSum() {
        TransactionRepository tr = new TransactionRepository();

        // Test sum(transaction_id) using the example given in the spec
        Transaction t1 = new Transaction(5000,
                "cars",
                new Long(10),
                null);
        tr.storeTransaction(new Long(10), t1);

        Transaction t2 = new Transaction(10000,
                "shopping",
                new Long(11),
                new Long(10));
        tr.storeTransaction(new Long(11), t2);

        assertEquals(15000, tr.sumWithChildren(new Long(10)), 0);
        assertEquals(10000, tr.sumWithChildren(new Long(11)), 0);

        // Test sum(transaction_id) using a chain of transactions longer than 2
        Transaction t3 = new Transaction(10000,
                "shoes",
                new Long(12),
                new Long(11));
        tr.storeTransaction(new Long(12), t3);

        assertEquals(25000, tr.sumWithChildren(new Long(10)), 0);

    }

    /**
     * test that getIDsFromType gives a list of ids
     *
     */
    @Test
    public void testType() {

        TransactionRepository tr = new TransactionRepository();

        // Store a transaction with type "cars"
        Transaction t1 = new Transaction(5000,
                "cars",
                new Long(10),
                null);
        tr.storeTransaction(new Long(10), t1);

        // Get all transaction IDs with type "cars"
        List ids = tr.getIDsFromType("cars");

        // The expected result in this case is a list with only one ID
        List<Long> expected = new LinkedList<Long>();
        expected.add(new Long(10));

        assertThat(expected, is(ids));
    }




}
package neha.test.vendakata;

import org.junit.Test;

import neha.test.vendakata.models.Product;
import neha.test.vendakata.models.Stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StockTest {
    @Test
    public void validStock() throws Exception {
        Stock s = new Stock(new Product("Something", 5), 2);
        assertNotNull("Stock should not have been null", s);
        assertNotNull(s.getProduct());
        assertEquals(2, s.getAvailable());
    }

    @Test()
    public void removeStock() throws Exception {
        Stock s = new Stock(new Product("Something", 5), 1);
        assertNotNull("Stock should not have been null", s);
        s.reduceAvailable();
        assertEquals(0, s.getAvailable());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeEmptyStock() throws Exception {
        Stock s = new Stock(new Product("Something", 5), 1);
        assertNotNull("Stock should not have been null", s);
        s.reduceAvailable();
        assertEquals("Should have passed one call to reduceAvailable since stock was 1", 0, s.getAvailable());
        // but the second one should fail with UnsupportedOperationException
        s.reduceAvailable();
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidCost() throws Exception {
        new Stock(new Product("Negative costs ain't cool, yo", 0), -2);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(
                "toString result did not match expected",
                "x5 a name/$0.04",
                new Stock(new Product("a name", 4), 5).toString());
    }
}
package neha.test.vendakata;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import neha.test.vendakata.models.Product;
import neha.test.vendakata.models.Stock;
import neha.test.vendakata.models.VendingMachine;
import neha.test.vendakata.services.IVendService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VendingMachineTest {
    private IVendService machine;

    private final Product cola = new Product("cola", 100);

    @Before
    public void setUp() {
        List<Stock> stock = new ArrayList<>(3);
        stock.add(new Stock(cola, 2));
        stock.add(new Stock(new Product("chips", 50), 2));
        stock.add(new Stock(new Product("candy", 65), 2));

        this.machine = new VendingMachine(stock);
    }

    @Test
    public void validMachineCreatedDuringTestSetup() throws Exception {
        assertNotNull(
                "Machine should have been setup by the fixture",
                this.machine);
    }

    @Test
    public void checkTestDataSetup() throws Exception {
        assertEquals(
                "Expected no money pending return in the standard machine setup",
                0,
                machine.getUscInReturn());
    }

    @Test
    public void returnCoins() {
        assertEquals("INSERT COIN", this.machine.updateAndGetCurrentMessageForDisplay());
        this.machine.insertCoin(5);
        assertEquals("$0.05", this.machine.updateAndGetCurrentMessageForDisplay());
    }

    @Test
    public void collectCoins() {
        this.machine.insertCoin(5);
        this.machine.returnCoins();
        assertEquals(5, this.machine.getUscInReturn());
        this.machine.collectCoins();
        assertEquals(0, this.machine.getUscInReturn());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(
                "toString result did not match expected",
                "$0.00 in flight, $4.00 in change, and 3 products",
                this.machine.toString());
    }
}
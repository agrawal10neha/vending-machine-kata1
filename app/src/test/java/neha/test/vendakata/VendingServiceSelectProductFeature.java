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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class VendingServiceSelectProductFeature {
    private IVendService machine;

    private final Product cola = new Product("cola", 100);

    @Before
    public void setUp() {
        List<Stock> stock = new ArrayList<>(3);
        stock.add(new Stock(cola, 20));
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
    public void purchaseProductHappyPath() {
        this.machine.insertCoin(5);
        this.machine.insertCoin(25);
        this.machine.insertCoin(25);
        this.machine.insertCoin(25);
        this.machine.insertCoin(25);
        assertEquals(105, this.machine.getAcceptedUsc());
        assertTrue(this.machine.purchaseProduct(0));
        assertEquals(0, this.machine.getAcceptedUsc());
        assertEquals("THANK YOU", this.machine.updateAndGetCurrentMessageForDisplay());
        assertEquals("INSERT COIN", this.machine.updateAndGetCurrentMessageForDisplay());
    }

    @Test
    public void purchaseProductInsufficientFunds() {
        assertFalse(this.machine.purchaseProduct(0));
        // feature spec wasn't explicit whether this state should animate "PRICE" then separately show "$0.05", nor the format of the price;
        // assuming they can be combined into one line without animation, and matching the currency format used elsewhere in the requirements document
        assertEquals("PRICE $1.00", this.machine.updateAndGetCurrentMessageForDisplay());
        assertEquals("INSERT COIN", this.machine.updateAndGetCurrentMessageForDisplay());

        this.machine.insertCoin(5);
        assertFalse(this.machine.purchaseProduct(0));
        // should still show price even if some money is accepted
        assertEquals("PRICE $1.00", this.machine.updateAndGetCurrentMessageForDisplay());
        // but next check should show accepted value
        assertEquals("$0.05", this.machine.updateAndGetCurrentMessageForDisplay());
    }
}
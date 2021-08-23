package neha.test.vendakata;

import org.junit.Test;

import neha.test.vendakata.models.Product;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class ProductTest {
    @Test
    public void validProduct() throws Exception {
        Product p = new Product("Something", 5);
        assertNotNull("Product should not have been null", p);
        assertEquals("Something", p.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidProductName() throws Exception {
        new Product("", 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidCost() throws Exception {
        new Product("Negative costs ain't cool, yo", -5);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(
                "toString result did not match expected",
                "a name/$0.04",
                new Product("a name", 4).toString());
    }

    @Test
    public void testEqualityOfDifferentButEqualObjects() throws Exception {
        assertEquals(
                "equals() implementation of two different but equal objects did not match expected",
                new Product("Name", 2),
                new Product("Name", 2));
    }

    @Test
    public void testEqualityOfDifferentTypes() throws Exception {
        assertFalse(
                "equals() implementation of two different types should return false",
                new Product("Name", 2).equals(new Object()));
    }

    @Test
    public void testHashOfDifferentButEqualObjects() throws Exception {
        assertEquals(
                "equals() implementation of two different but equal objects did not match expected",
                new Product("Name", 2).hashCode(),
                new Product("Name", 2).hashCode());
    }
}
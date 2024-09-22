import com.example.protrack.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(1, "Product", Date.valueOf("2024-01-01"));
    }

    @Test
    public void testGetProductId() {
        assertEquals(1, product.getProductId());
    }

    @Test
    public void testGetProductName() {
        assertEquals("Product", product.getProductName());
    }

    @Test
    public void testGetDateCreated() {
        assertEquals(Date.valueOf("2024-01-01"), product.getDateCreated());
    }

    @Test
    public void testNullProductId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(null, "Product", Date.valueOf("2024-01-01"));
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullProductName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(1, null, Date.valueOf("2024-01-01"));
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullDateCreated() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(1, "Product", null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNegativeProductId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(-1, "Product", new Date(System.currentTimeMillis()));
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testProductNameExceedsLength() {
        String longName = "P".repeat(256);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(1, longName, new Date(System.currentTimeMillis()));
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

}
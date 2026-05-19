package edu.cczu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testValidateProduct_ValidData_ReturnsTrue() {
        // Arrange
        Product product = new Product(1L, "Laptop", 5000.0, 10, "Electronics");
        // Act
        boolean result = product.validateProduct();
        // Assert
        assertTrue(result, "Valid product should return true");
    }

    @Test
    void testValidateProduct_NegativePrice_ReturnsFalse() {
        // Arrange
        Product product = new Product(1L, "Laptop", -1.0, 10, "Electronics");
        // Act
        boolean result = product.validateProduct();
        // Assert
        assertFalse(result, "Negative price should be invalid");
    }

    @Test
    void testUpdateStock_ValidQuantity_DecreasesStock() {
        // Arrange
        Product product = new Product(1L, "Bread", 2.0, 20, "Food");
        // Act
        product.updateStock(5);
        // Assert
        assertEquals(15, product.getStock());
    }
}
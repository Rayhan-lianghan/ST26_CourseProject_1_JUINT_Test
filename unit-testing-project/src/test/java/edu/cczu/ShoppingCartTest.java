package edu.cczu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartTest {
    private ShoppingCart cart;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn(101L);
        when(mockProduct.validateProduct()).thenReturn(true);
    }

    @Test
    void testAddProduct_SufficientStock_ReturnsTrue() {
        // Arrange
        when(mockProduct.getStock()).thenReturn(10);
        // Act
        boolean result = cart.addProduct(mockProduct, 5);
        // Assert
        assertTrue(result);
        verify(mockProduct).updateStock(5);
    }

    @Test
    void testCalculateTotalPrice_MultipleItems_ReturnsCorrectSum() {
        // Arrange
        when(mockProduct.getPrice()).thenReturn(20.0);
        when(mockProduct.getStock()).thenReturn(10);
        cart.addProduct(mockProduct, 2); // 40.0
        
        // Act
        double total = cart.calculateTotalPrice();
        
        // Assert
        assertEquals(40.0, total);
    }
}
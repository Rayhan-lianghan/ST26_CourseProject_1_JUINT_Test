package edu.cczu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceTest {
    private CheckoutService checkoutService;
    private ShoppingCart mockCart;
    private PaymentService mockPayment;

    @BeforeEach
    void setUp() {
        mockCart = mock(ShoppingCart.class);
        mockPayment = mock(PaymentService.class);
        checkoutService = new CheckoutService(mockCart, mockPayment);
    }

    @Test
    void testCalculateDiscount_Applied() {
        // Act
        double discount = checkoutService.calculateDiscount(200.0);
        // Assert
        assertEquals(20.0, discount);
    }
}
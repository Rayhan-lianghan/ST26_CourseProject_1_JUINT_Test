package edu.cczu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CheckoutServiceTest {
    private CheckoutService checkoutService;
    private ShoppingCart mockCart;
    private PaymentService mockPaymentService;

    @BeforeEach
    void setUp() {
        mockCart = mock(ShoppingCart.class);
        mockPaymentService = mock(PaymentService.class);
        checkoutService = new CheckoutService(mockCart, mockPaymentService);
    }

    @Test
    void testCalculateDiscount_HighAmount_ReturnsTenPercent() {
        // Arrange
        double amount = 200.0;
        // Act
        double discount = checkoutService.calculateDiscount(amount);
        // Assert
        assertEquals(20.0, discount, "Orders >= 100 should get 10% discount");
    }

    @Test
    void testCheckout_SuccessfulFlow_ReturnsPaidOrder() {
        // Arrange
        when(mockCart.checkStock()).thenReturn(true);
        when(mockCart.calculateTotalPrice()).thenReturn(100.0);
        
        PaymentResponse mockResponse = new PaymentResponse(true, "Success", 12345L);
        when(mockPaymentService.processPayment(any())).thenReturn(mockResponse);

        // Act
        Order result = checkoutService.checkout("CreditCard");

        // Assert
        assertEquals("PAID", result.getStatus());
        assertNotNull(result.getOrderId());
    }
}
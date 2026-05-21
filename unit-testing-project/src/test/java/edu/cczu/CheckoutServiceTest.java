package edu.cczu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        // Act: This method runs math on the total double passed to it
        double discount = checkoutService.calculateDiscount(200.0);
        
        // Assert: 10% discount on 200 should be 20.0
        assertEquals(20.0, discount);
    }

    @Test
    void testCalculateDiscount_NotApplied() {
        // Act: Below $100 should give no discount
        double discount = checkoutService.calculateDiscount(50.0);
        
        // Assert
        assertEquals(0.0, discount);
    }

    @Test
    void testCheckout_Successful() {
        // 1. Arrange: Setup the mock cart behavior
        when(mockCart.checkStock()).thenReturn(true);
        when(mockCart.calculateTotalPrice()).thenReturn(200.0); 

        // Mock PaymentResponse directly to avoid missing constructor/setter errors
        PaymentResponse simulatedResponse = mock(PaymentResponse.class);
        when(simulatedResponse.isSuccess()).thenReturn(true);
        // Note: getTransactionId() stub removed because CheckoutService doesn't call it.
        
        // Mock paymentService behavior when any PaymentRequest is passed to it
        when(mockPayment.processPayment(any(PaymentRequest.class))).thenReturn(simulatedResponse);

        // 2. Act
        Order order = checkoutService.checkout("Credit Card");

        // 3. Assert
        assertNotNull(order);
        assertEquals("PAID", order.getStatus());
        assertEquals(200.0, order.getTotalAmount());
        assertEquals(20.0, order.getDiscountAmount());
        assertEquals(180.0, order.getFinalAmount());

        // Verify the checkout emptied the cart upon successful validation
        verify(mockCart, times(1)).clearCart();
    }

    @Test
    void testCheckout_FailedDueToOutOfStock() {
        // 1. Arrange: If stock validation fails
        when(mockCart.checkStock()).thenReturn(false);

        // 2. Act
        Order order = checkoutService.checkout("PayPal");

        // 3. Assert
        assertNull(order, "Checkout should immediately exit and return null if stock check fails");
        
        // Verify payment processing and clearing cart are bypassed completely
        verify(mockPayment, never()).processPayment(any(PaymentRequest.class));
        verify(mockCart, never()).clearCart();
    }
}
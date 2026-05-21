package edu.cczu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Best practice: Integrates Mockito closely with JUnit 5
class CheckoutServiceTest {

    private CheckoutService checkoutService;
    private ShoppingCart mockCart;
    private PaymentService mockPayment;

    @BeforeEach
    void setUp() {
        // Correctly creating the fake (mock) dependencies
        mockCart = mock(ShoppingCart.class);
        mockPayment = mock(PaymentService.class);
        
        // Injecting those fake dependencies into the service we want to test
        checkoutService = new CheckoutService(mockCart, mockPayment);
    }

    @Test
    void testCalculateDiscount_Applied() {
        // 1. ARRANGE
        // If your calculateDiscount method needs to check the cart's total internally, 
        // we tell the mock exactly what to return when that happens:
        when(mockCart.getTotalPrice()).thenReturn(200.0);

        // 2. ACT
        // Call the real method under test. 
        // (Pass the expected amount or let it pull from the mockCart depending on your implementation)
        double discount = checkoutService.calculateDiscount(200.0);

        // 3. ASSERT
        // Verify the math calculation matches your expectations
        assertEquals(20.0, discount);
        
        // 4. VERIFY (Optional Mockito Step)
        // Ensure that checkoutService actually interacted with your mock dependencies
        // verify(mockCart, times(1)).getTotalPrice();
    }
    
    @Test
    void testProcessCheckout_SuccessfulPayment() {
        // Another example showing how mockPayment comes into play:
        when(mockCart.getTotalPrice()).thenReturn(100.0);
        when(mockPayment.charge(100.0)).thenReturn(true); // Tell the fake payment service to return success

        boolean paymentSuccess = checkoutService.processCheckout();

        assertTrue(paymentSuccess);
    }
}
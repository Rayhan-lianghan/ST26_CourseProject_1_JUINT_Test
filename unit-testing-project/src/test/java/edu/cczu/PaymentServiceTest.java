package edu.cczu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
    private PaymentService paymentService;
    private ThirdPartyPaymentInterface mockGateway;

    @BeforeEach
    void setUp() {
        // Arrange: Create the mock dependency
        mockGateway = mock(ThirdPartyPaymentInterface.class);
        paymentService = new PaymentService(mockGateway);
    }

    @Test
    void testProcessPayment_Success_ReturnsTrue() throws Exception {
        // Arrange
        PaymentRequest request = new PaymentRequest(1001L, 50.0, "AliPay");
        when(mockGateway.makePayment(anyLong(), anyDouble(), anyString())).thenReturn(12345L);

        // Act
        PaymentResponse response = paymentService.processPayment(request);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals(12345L, response.getTransactionId());
        verify(mockGateway, times(1)).makePayment(anyLong(), anyDouble(), anyString());
    }

    @Test
    void testProcessPayment_NullRequest_ReturnsFalse() {
        // Act
        PaymentResponse response = paymentService.processPayment(null);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Invalid payment request", response.getMessage());
    }
}
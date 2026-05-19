package edu.cczu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
    private PaymentService paymentService;
    private ThirdPartyPaymentInterface mockGateway;

    @BeforeEach
    void setUp() {
        mockGateway = mock(ThirdPartyPaymentInterface.class);
        paymentService = new PaymentService(mockGateway);
    }

    @Test
    void testProcessPayment_Success() throws Exception {
        // Arrange
        PaymentRequest request = new PaymentRequest(1L, 100.0, "PayPal");
        when(mockGateway.makePayment(anyLong(), anyDouble(), anyString())).thenReturn(999L);
        // Act
        PaymentResponse response = paymentService.processPayment(request);
        // Assert
        assertTrue(response.isSuccess());
        assertEquals(999L, response.getTransactionId());
    }
}
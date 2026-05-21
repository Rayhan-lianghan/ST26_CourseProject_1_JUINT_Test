package edu.cczu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Integrates Mockito seamlessly with the JUnit 5 test runner lifecycle
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private PaymentService paymentService;
    private ThirdPartyPaymentInterface mockGateway;

    @BeforeEach
    void setUp() {
        // Initializes the mock object instance safely
        mockGateway = mock(ThirdPartyPaymentInterface.class);
        paymentService = new PaymentService(mockGateway);
    }

    @Test
    void testProcessPayment_Success() throws Exception {
        // 1. Arrange
        PaymentRequest request = new PaymentRequest(1L, 100.0, "PayPal");
        
        // Define mock rules using Mockito's argument matchers
        when(mockGateway.makePayment(anyLong(), anyDouble(), anyString())).thenReturn(999L);

        // 2. Act
        PaymentResponse response = paymentService.processPayment(request);

        // 3. Assert
        assertTrue(response.isSuccess());
        assertEquals(999L, response.getTransactionId());
        
        // 4. Verify (Optional but highly recommended for mock gateways)
        // Confirms that paymentService actually triggered the gateway method exactly once
        verify(mockGateway, times(1)).makePayment(1L, 100.0, "PayPal");
    }
}
package edu.cczu;

/**
 * Simulates external payment service.
 * FIXED: Removed duplicate package declarations and corrected logic defects.
 */
public class PaymentService {
    private ThirdPartyPaymentInterface thirdPartyPayment;

    public PaymentService(ThirdPartyPaymentInterface thirdPartyPayment) {
        this.thirdPartyPayment = thirdPartyPayment;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        // FIXED Defect 16: Added check for null request
        if (request == null || request.getAmount() <= 0) {
            return new PaymentResponse(false, "Invalid payment request", null);
        }

        try {
            Long transactionId = thirdPartyPayment.makePayment(request.getOrderId(), request.getAmount(), request.getPaymentMethod());
            
            // FIXED Defect 17: If transactionId is null, it's a failure, not a success
            if (transactionId == null) {
                return new PaymentResponse(false, "Payment failed: No transaction ID", null);
            }
            return new PaymentResponse(true, "Payment successful", transactionId);
            
        } catch (Exception e) {
            // FIXED Defect 18: Included exception message for debugging
            return new PaymentResponse(false, "Payment failed: " + e.getMessage(), null);
        }
    }
}
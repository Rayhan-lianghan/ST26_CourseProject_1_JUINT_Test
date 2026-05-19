package edu.cczu;

public class PaymentService {
    private ThirdPartyPaymentInterface thirdPartyPayment;

    public PaymentService(ThirdPartyPaymentInterface thirdPartyPayment) {
        this.thirdPartyPayment = thirdPartyPayment;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        if (request == null || request.getAmount() <= 0 || request.getPaymentMethod() == null) {
            return new PaymentResponse(false, "Invalid payment request", null);
        }

        try {
            Long transactionId = thirdPartyPayment.makePayment(request.getOrderId(), request.getAmount(), request.getPaymentMethod());
            if (transactionId == null) {
                return new PaymentResponse(false, "Payment failed: No transaction ID", null);
            }
            return new PaymentResponse(true, "Payment successful", transactionId);
        } catch (Exception e) {
            return new PaymentResponse(false, "Payment failed: " + e.getMessage(), null);
        }
    }
}
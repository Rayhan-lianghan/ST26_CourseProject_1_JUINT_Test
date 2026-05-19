package edu.cczu;

/**
 * Data object containing information needed to process a payment.
 */
public class PaymentRequest {
    private Long orderId;
    private double amount;
    private String paymentMethod;

    public PaymentRequest(Long orderId, double amount, String paymentMethod) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public Long getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
}
package edu.cczu;

/**
 * Model class to represent a completed Order.
 */
public class Order {
    private Long orderId;
    private double totalAmount;
    private double discountAmount;
    private double finalAmount;
    private String status; // e.g., "PENDING", "PAID", "FAILED"

    public Order(Long orderId, double totalAmount, double discountAmount, double finalAmount, String status) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.status = status;
    }

    // Getters
    public Long getOrderId() { return orderId; }
    public double getTotalAmount() { return totalAmount; }
    public double getDiscountAmount() { return discountAmount; }
    public double getFinalAmount() { return finalAmount; }
    public String getStatus() { return status; }

    // Setter for status (used by CheckoutService to update to "PAID")
    public void setStatus(String status) { this.status = status; }
}
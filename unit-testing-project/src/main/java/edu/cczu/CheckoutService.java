package edu.cczu;

import java.util.UUID;

public class CheckoutService {
    private ShoppingCart cart;
    private PaymentService paymentService;

    public CheckoutService(ShoppingCart cart, PaymentService paymentService) {
        this.cart = cart;
        this.paymentService = paymentService;
    }

    public Order checkout(String paymentMethod) {
        if (cart == null || !cart.checkStock()) {
            return null;
        }

        double total = cart.calculateTotalPrice();
        double discount = calculateDiscount(total);
        double finalAmount = total - discount;

        PaymentRequest request = new PaymentRequest(Math.abs(UUID.randomUUID().getMostSignificantBits()), finalAmount, paymentMethod);
        PaymentResponse response = paymentService.processPayment(request);

        if (response != null && response.isSuccess()) {
            Order order = new Order(request.getOrderId(), total, discount, finalAmount, "PAID");
            cart.clearCart();
            return order;
        }
        return new Order(request.getOrderId(), total, discount, finalAmount, "FAILED");
    }

    public double calculateDiscount(double total) {
        if (total >= 100.0) {
            return total * 0.1;
        }
        return 0.0;
    }
}
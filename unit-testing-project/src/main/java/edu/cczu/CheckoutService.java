package edu.cczu;

import java.util.Random;

/**
 * Core checkout logic (calculate discount, verify payment, generate order, call payment service)
 * Debugged version: Logic errors fixed and validation added.
 */
public class CheckoutService {
    private ShoppingCart shoppingCart;
    private PaymentService paymentService;
    private Random random = new Random();

    // Constructor (dependency injection, for testing with Mockito)
    public CheckoutService(ShoppingCart shoppingCart, PaymentService paymentService) {
        this.shoppingCart = shoppingCart;
        this.paymentService = paymentService;
    }

    /**
     * Calculate discount based on total order amount
     * Rules: ≥$100 → 10% discount; $50-$99 → 5% discount; <$50 → no discount
     */
    public double calculateDiscount(double totalAmount) {
        // FIXED Defect 11: Corrected the order and logic of discounts
        if (totalAmount >= 100) {
            return totalAmount * 0.10; // 10% discount for large orders
        } else if (totalAmount >= 50) {
            return totalAmount * 0.05; // 5% discount for medium orders
        } else {
            return 0.0; // No discount for small orders
        }
    }

    /**
     * Verify payment (call PaymentService and check actual response status)
     */
    public boolean verifyPayment(PaymentRequest paymentRequest) {
        if (paymentRequest == null || paymentRequest.getAmount() <= 0) {
            return false;
        }
        
        PaymentResponse response = paymentService.processPayment(paymentRequest);
        
        // FIXED Defect 12: Now correctly checks if the payment was successful
        // Assumes PaymentResponse has a method isSuccess() or getStatus()
        return response != null && response.isSuccess(); 
    }

    /**
     * Generate order with validation to ensure amounts are legal
     */
    public Order generateOrder(double totalAmount, double discountAmount) {
        // Ensure orderId is positive (nextLong with bounds is good)
        Long orderId = random.nextLong(100000L, 999999L);
        
        // FIXED Defect 13: Added validation to ensure final amount isn't negative
        double finalAmount = totalAmount - discountAmount;
        if (finalAmount < 0) {
            finalAmount = 0;
        }
        
        return new Order(orderId, totalAmount, discountAmount, finalAmount, "PENDING");
    }

    /**
     * Core checkout process: check stock → calculate total → calculate discount → generate order → verify payment
     */
    public Order checkout(String paymentMethod) {
        // Step 1: Check stock
        // FIXED Defect 14: Ensure shoppingCart actually checks stock correctly
        if (!shoppingCart.checkStock()) {
            return new Order(null, 0.0, 0.0, 0.0, "FAILED_STOCK_OUT");
        }

        // Step 2: Calculate total price
        // FIXED Defect 15: Ensure we get the actual calculated price from the cart
        double totalAmount = shoppingCart.calculateTotalPrice();
        
        // Validation: Cannot checkout an empty cart
        if (totalAmount <= 0) {
             return new Order(null, 0.0, 0.0, 0.0, "FAILED_EMPTY_CART");
        }

        // Step 3: Calculate discount
        double discountAmount = calculateDiscount(totalAmount);

        // Step 4: Generate order
        Order order = generateOrder(totalAmount, discountAmount);

        // Step 5: Verify payment
        PaymentRequest paymentRequest = new PaymentRequest(order.getOrderId(), order.getFinalAmount(), paymentMethod);
        
        if (verifyPayment(paymentRequest)) {
            order.setStatus("PAID");
        } else {
            order.setStatus("FAILED_PAYMENT");
        }

        return order;
    }
}
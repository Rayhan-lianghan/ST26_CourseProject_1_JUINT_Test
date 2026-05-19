package edu.cczu;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== E-Commerce System Initialization ===");

        // 1. Setup Data
        Product laptop = new Product(101L, "Gaming Laptop", 8000.0, 5, "Electronics");
        ShoppingCart cart = new ShoppingCart();

        // 2. Add to Cart
        System.out.println("Adding " + laptop.getName() + " to cart...");
        boolean added = cart.addProduct(laptop, 1);

        if (added) {
            System.out.println("Item added successfully. Total price: " + cart.calculateTotalPrice());
        }

        // 3. Setup Services
        // Note: In a real app, this would connect to a real Bank API.
        // Here, we just ensure the structure is ready for the Unit Tests.
        ThirdPartyPaymentInterface paymentGateway = (orderId, amount, method) -> {
            System.out.println("Processing " + method + " payment of " + amount + " for Order #" + orderId);
            return (long) (Math.random() * 1000000); // Simulate a transaction ID
        };

        PaymentService paymentService = new PaymentService(paymentGateway);
        CheckoutService checkoutService = new CheckoutService(cart, paymentService);

        // 4. Process Checkout
        System.out.println("\nProceeding to Checkout...");
        Order finalOrder = checkoutService.checkout("AliPay");

        if (finalOrder != null && "PAID".equals(finalOrder.getStatus())) {
            System.out.println("Checkout Complete!");
            System.out.println("Order ID: " + finalOrder.getOrderId());
            System.out.println("Final Amount Paid: " + finalOrder.getFinalAmount());
        } else {
            System.out.println("Checkout Failed.");
        }

        System.out.println("\n=== System Ready for Unit Testing ===");
    }
}
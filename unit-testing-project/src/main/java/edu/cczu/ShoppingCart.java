package edu.cczu;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the shopping cart.
 * Debugged version: Changed Map to store Product objects to fix calculation and stock issues.
 */
public class ShoppingCart {
    // FIXED: Changed Value to Product object to allow price/stock access
    private Map<Long, CartItem> items;

    public ShoppingCart() {
        this.items = new HashMap<>();
    }

    // Helper class to store product and quantity together
    private static class CartItem {
        Product product;
        int quantity;
        CartItem(Product p, int q) { this.product = p; this.quantity = q; }
    }

    /**
     * Add product to cart with proper validation and stock updates.
     */
    public boolean addProduct(Product product, int quantity) {
        // FIXED Defect 5: Added quantity validation
        if (product == null || !product.validateProduct() || quantity <= 0) {
            return false;
        }

        // FIXED Defect 6: Corrected stock check logic
        if (product.getStock() < quantity) {
            return false; // Cannot add more than available
        }

        // Update cart
        Long id = product.getId();
        if (items.containsKey(id)) {
            items.get(id).quantity += quantity;
        } else {
            items.put(id, new CartItem(product, quantity));
        }

        // FIXED Defect 7: Actually update the product's stock
        product.updateStock(quantity);
        return true;
    }

    /**
     * Remove product from cart.
     */
    public boolean removeProduct(Long productId, int quantity) {
        // FIXED Defect 8: Handle non-existent products gracefully
        if (productId == null || !items.containsKey(productId)) {
            return true; // Already gone, so "success"
        }

        CartItem item = items.get(productId);
        if (quantity <= 0 || quantity >= item.quantity) {
            // Restore stock if removing from cart
            item.product.setStock(item.product.getStock() + item.quantity);
            items.remove(productId);
        } else {
            item.product.setStock(item.product.getStock() + quantity);
            item.quantity -= quantity;
        }
        return true;
    }

    /**
     * Calculate total price using real product prices.
     */
    public double calculateTotalPrice() {
        double total = 0.0;
        // FIXED Defect 9: Now we can access item.product.getPrice()
        for (CartItem item : items.values()) {
            total += item.product.getPrice() * item.quantity;
        }
        return total;
    }

    /**
     * Re-verify if items in cart are still in stock.
     */
    public boolean checkStock() {
        // FIXED Defect 10: Logic now checks actual product objects
        for (CartItem item : items.values()) {
            if (item.product.getStock() < 0) { // Or other business logic
                return false;
            }
        }
        return true;
    }

    public void clearCart() {
        items.clear();
    }

    // Updated for testing compatibility
    public Map<Long, Integer> getProductQuantities() {
        Map<Long, Integer> quantities = new HashMap<>();
        items.forEach((id, item) -> quantities.put(id, item.quantity));
        return quantities;
    }
}
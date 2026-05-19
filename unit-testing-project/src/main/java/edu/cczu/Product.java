package edu.cczu;

/**
 * Manages product information.
 * Debugged version: Fixed validation logic and stock management flaws.
 */
public class Product {
    private Long id;
    private String name;
    private double price; 
    private int stock;
    private String category;

    public Product(Long id, String name, double price, int stock, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    /**
     * Validates if product information follows business rules.
     * Rules: Price > 0, Stock >= 0, Name & Category not null/empty.
     */
    public boolean validateProduct() {
        // FIXED Defect 1: Price must be strictly greater than 0
        if (price <= 0) {
            return false;
        }
        
        // FIXED Defect 2: Stock must be 0 or greater
        if (stock < 0) {
            return false;
        }
        
        // FIXED Defect 3: Category and Name validation
        if (id == null || name == null || name.isEmpty()) {
            return false;
        }

        if (category == null || category.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Update product stock safely.
     */
    public void updateStock(int quantity) {
        // FIXED Defect 4: Ensure we don't subtract more than we have 
        // and ensure quantity isn't negative
        if (quantity > 0 && (this.stock - quantity) >= 0) {
            this.stock -= quantity;
        } else if (quantity < 0) {
            // If adding stock (restocking), that's usually a different method, 
            // but for this project, we prevent illegal subtractions.
            System.err.println("Invalid quantity update.");
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
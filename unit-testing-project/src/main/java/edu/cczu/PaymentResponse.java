package edu.cczu;

/**
 * Data object representing the result of a payment attempt.
 */
public class PaymentResponse {
    private boolean success;
    private String message;
    private Long transactionId;

    public PaymentResponse(boolean success, String message, Long transactionId) {
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Long getTransactionId() { return transactionId; }
}
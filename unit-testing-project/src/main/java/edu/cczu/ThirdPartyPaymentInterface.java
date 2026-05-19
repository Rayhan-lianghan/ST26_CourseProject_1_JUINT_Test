package edu.cczu;

/**
 * Auxiliary interface: Third-party payment interface.
 * This is what we will mock using Mockito in our tests.
 */
public interface ThirdPartyPaymentInterface {
    /**
     * @return a transaction ID (Long) if successful
     * @throws Exception if the gateway is down or connection fails
     */
    Long makePayment(Long orderId, double amount, String paymentMethod) throws Exception;
}
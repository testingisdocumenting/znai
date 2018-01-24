package com.twosigma.testing.components;

/**
 * @author mykola
 */
public interface PaymentService {
    int availableBalance(String walletId);
    void makePayment(String walletId, int amount);
}

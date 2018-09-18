package com.twosigma.testing.examples.components;

public interface PaymentService {
    int availableBalance(String walletId);
    void makePayment(String walletId, int amount);
}

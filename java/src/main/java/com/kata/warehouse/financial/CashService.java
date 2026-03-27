package com.kata.warehouse.financial;

import com.kata.warehouse.shared.Money;

public class CashService {
    private Money cashBalance = new Money(0.0);

    public Money getCashBalance() {
        return cashBalance;
    }

    public void addCash(Money amount) {
        this.cashBalance = this.cashBalance.add(amount);
    }

    public void deductCash(Money amount) {
        this.cashBalance = this.cashBalance.subtract(amount);
    }
}

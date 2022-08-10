package com.themonkeynauts.game.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

public class Balance {

    private BigDecimal amount;

    public Balance() {
        this.amount = BigDecimal.ZERO;
    }

    public Balance(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal add(BigDecimal value) {
        return this.amount.add(value);
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Balance balance = (Balance) o;

        return new EqualsBuilder().append(amount, balance.amount).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(amount).toHashCode();
    }
}

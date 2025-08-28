package cl.demo.polizas.domain.valueobject;

import cl.demo.polizas.seedwork.domain.Guard;
import cl.demo.polizas.seedwork.domain.ValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Value Object representing a monetary amount.
 * Immutable and validated money representation with currency support.
 */
public final class Money extends ValueObject {

    private final BigDecimal amount;
    private final Currency currency;

    private Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
        validate();
    }

    /**
     * Creates a Money instance with the specified amount and default currency (CLP).
     */
    public static Money of(BigDecimal amount) {
        return new Money(amount, Currency.getInstance("CLP"));
    }

    /**
     * Creates a Money instance with the specified amount and currency.
     */
    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    /**
     * Creates a Money instance with the specified amount as string.
     */
    public static Money of(String amount) {
        Guard.notNullOrEmpty(amount, "Amount cannot be null or empty");
        try {
            return new Money(new BigDecimal(amount), Currency.getInstance("CLP"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format: " + amount);
        }
    }

    /**
     * Creates a Money instance with zero amount.
     */
    public static Money zero() {
        return new Money(BigDecimal.ZERO, Currency.getInstance("CLP"));
    }

    /**
     * Gets the amount as BigDecimal.
     */
    public BigDecimal amount() {
        return amount;
    }

    /**
     * Gets the currency.
     */
    public Currency currency() {
        return currency;
    }

    /**
     * Adds another Money amount.
     */
    public Money add(Money other) {
        Guard.notNull(other, "Money to add cannot be null");
        Guard.isTrue(this.currency.equals(other.currency), 
                    "Cannot add money with different currencies");
        
        return new Money(this.amount.add(other.amount), this.currency);
    }

    /**
     * Subtracts another Money amount.
     */
    public Money subtract(Money other) {
        Guard.notNull(other, "Money to subtract cannot be null");
        Guard.isTrue(this.currency.equals(other.currency), 
                    "Cannot subtract money with different currencies");
        
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    /**
     * Multiplies by a factor.
     */
    public Money multiply(BigDecimal factor) {
        Guard.notNull(factor, "Factor cannot be null");
        return new Money(this.amount.multiply(factor), this.currency);
    }

    /**
     * Checks if the amount is positive.
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Checks if the amount is zero.
     */
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Checks if the amount is negative.
     */
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Gets the formatted string representation.
     */
    public String format() {
        return String.format("%s %s", 
                           currency.getSymbol(), 
                           amount.setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    protected void validate() {
        Guard.notNull(amount, "Amount cannot be null");
        Guard.notNull(currency, "Currency cannot be null");
        Guard.isTrue(amount.compareTo(BigDecimal.ZERO) >= 0, 
                    "Amount cannot be negative");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return amount.compareTo(money.amount) == 0 && 
               Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return format();
    }
}

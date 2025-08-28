package cl.demo.polizas.seedwork.domain;

import java.util.Collection;

/**
 * Utility class for common validation and guard clauses. Provides a fluent API for checking preconditions and business
 * rules.
 */
public final class Guard {

    private Guard() {
        // Utility class, prevent instantiation
    }

    /**
     * Ensures that the given object is not null.
     * 
     * @param obj The object to check
     * @param message The error message if the object is null
     * @throws IllegalArgumentException if the object is null
     */
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that the given string is not null or empty.
     * 
     * @param str The string to check
     * @param message The error message if the string is null or empty
     * @throws IllegalArgumentException if the string is null or empty
     */
    public static void notNullOrEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that the given string is not null or blank.
     * 
     * @param str The string to check
     * @param message The error message if the string is null or blank
     * @throws IllegalArgumentException if the string is null or blank
     */
    public static void notNullOrBlank(String str, String message) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that the given collection is not null or empty.
     * 
     * @param collection The collection to check
     * @param message The error message if the collection is null or empty
     * @throws IllegalArgumentException if the collection is null or empty
     */
    public static void notNullOrEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that the given number is positive.
     * 
     * @param number The number to check
     * @param message The error message if the number is not positive
     * @throws IllegalArgumentException if the number is not positive
     */
    public static void positive(Number number, String message) {
        if (number == null || number.doubleValue() <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that the given number is non-negative.
     * 
     * @param number The number to check
     * @param message The error message if the number is negative
     * @throws IllegalArgumentException if the number is negative
     */
    public static void nonNegative(Number number, String message) {
        if (number == null || number.doubleValue() < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that the given number is within the specified range.
     * 
     * @param number The number to check
     * @param min The minimum allowed value (inclusive)
     * @param max The maximum allowed value (inclusive)
     * @param message The error message if the number is out of range
     * @throws IllegalArgumentException if the number is out of range
     */
    public static void inRange(Number number, Number min, Number max, String message) {
        if (number == null || number.doubleValue() < min.doubleValue() || number.doubleValue() > max.doubleValue()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that the given condition is true.
     * 
     * @param condition The condition to check
     * @param message The error message if the condition is false
     * @throws IllegalArgumentException if the condition is false
     */
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that the given condition is false.
     * 
     * @param condition The condition to check
     * @param message The error message if the condition is true
     * @throws IllegalArgumentException if the condition is true
     */
    public static void isFalse(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that the given string matches the specified pattern.
     * 
     * @param str The string to check
     * @param pattern The regex pattern to match against
     * @param message The error message if the string doesn't match the pattern
     * @throws IllegalArgumentException if the string doesn't match the pattern
     */
    public static void matches(String str, String pattern, String message) {
        notNullOrEmpty(str, "String cannot be null or empty");
        notNullOrEmpty(pattern, "Pattern cannot be null or empty");

        if (!str.matches(pattern)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that the given string length is within the specified range.
     * 
     * @param str The string to check
     * @param minLength The minimum allowed length
     * @param maxLength The maximum allowed length
     * @param message The error message if the length is out of range
     * @throws IllegalArgumentException if the length is out of range
     */
    public static void lengthInRange(String str, int minLength, int maxLength, String message) {
        notNull(str, "String cannot be null");

        if (str.length() < minLength || str.length() > maxLength) {
            throw new IllegalArgumentException(message);
        }
    }
}

package cl.demo.polizas.seedwork.domain;

import java.util.List;

/**
 * Represents the result of an operation that can either succeed or fail. Provides a clean way to handle both success
 * and error cases.
 * 
 * @param <T> The type of the value returned on success
 */
public record Result<T>(boolean success, T value, List<String> errors) {

    /**
     * Creates a successful result with the given value.
     * 
     * @param value The value to return
     * @param <T> The type of the value
     * @return A successful result
     */
    public static <T> Result<T> ok(T value) {
        return new Result<>(true, value, List.of());
    }

    /**
     * Creates a successful result with no value.
     * 
     * @param <T> The type parameter
     * @return A successful result with no value
     */
    public static <T> Result<T> ok() {
        return new Result<>(true, null, List.of());
    }

    /**
     * Creates a failed result with the given error messages.
     * 
     * @param errors The error messages
     * @param <T> The type parameter
     * @return A failed result
     */
    public static <T> Result<T> fail(String... errors) {
        return new Result<>(false, null, List.of(errors));
    }

    /**
     * Creates a failed result with a list of error messages.
     * 
     * @param errors The list of error messages
     * @param <T> The type parameter
     * @return A failed result
     */
    public static <T> Result<T> fail(List<String> errors) {
        return new Result<>(false, null, errors);
    }

    /**
     * Creates a failed result with a single error message.
     * 
     * @param error The error message
     * @param <T> The type parameter
     * @return A failed result
     */
    public static <T> Result<T> fail(String error) {
        return new Result<>(false, null, List.of(error));
    }

    /**
     * Checks if the result is successful.
     * 
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Checks if the result is a failure.
     * 
     * @return true if failed, false otherwise
     */
    public boolean isFailure() {
        return !success;
    }

    /**
     * Gets the value if the result is successful.
     * 
     * @return The value, or null if failed
     */
    public T getValue() {
        return value;
    }

    /**
     * Gets the error messages if the result is a failure.
     * 
     * @return The list of error messages, or empty list if successful
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Gets the first error message if the result is a failure.
     * 
     * @return The first error message, or null if successful
     */
    public String getFirstError() {
        return errors.isEmpty() ? null : errors.get(0);
    }

    /**
     * Maps the value of a successful result using the given function. If the result is a failure, returns a new failure
     * result.
     * 
     * @param mapper The function to apply to the value
     * @param <R> The type of the mapped value
     * @return A new result with the mapped value
     */
    public <R> Result<R> map(java.util.function.Function<T, R> mapper) {
        if (isFailure()) {
            return Result.fail(errors);
        }
        try {
            R mappedValue = mapper.apply(value);
            return Result.ok(mappedValue);
        } catch (Exception e) {
            return Result.fail("Error during mapping: " + e.getMessage());
        }
    }

    /**
     * Flat maps the value of a successful result using the given function. If the result is a failure, returns a new
     * failure result.
     * 
     * @param mapper The function to apply to the value
     * @param <R> The type of the mapped result
     * @return A new result
     */
    public <R> Result<R> flatMap(java.util.function.Function<T, Result<R>> mapper) {
        if (isFailure()) {
            return Result.fail(errors);
        }
        try {
            return mapper.apply(value);
        } catch (Exception e) {
            return Result.fail("Error during flat mapping: " + e.getMessage());
        }
    }
}

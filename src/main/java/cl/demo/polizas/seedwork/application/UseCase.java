package cl.demo.polizas.seedwork.application;

/**
 * Base interface for all use cases in the application layer. Defines the contract for handling business operations.
 * 
 * @param <I> The type of the input/request
 * @param <O> The type of the output/response
 */
public interface UseCase<I, O> {

    /**
     * Executes the use case with the given input.
     * 
     * @param input The input data for the use case
     * @return The result of executing the use case
     */
    O handle(I input);

    /**
     * Gets the name of this use case for logging and debugging purposes.
     * 
     * @return The use case name
     */
    default String getName() {
        return getClass().getSimpleName();
    }
}

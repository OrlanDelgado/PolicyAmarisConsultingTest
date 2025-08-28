package cl.demo.polizas.seedwork.application;

/**
 * Base interface for mappers that convert between different object types. Provides a clean contract for object
 * transformation operations.
 * 
 * @param <S> The source type
 * @param <T> The target type
 */
public interface Mapper<S, T> {

    /**
     * Maps the source object to the target type.
     * 
     * @param source The source object to map
     * @return The mapped target object
     */
    T map(S source);

    /**
     * Maps a collection of source objects to a collection of target objects.
     * 
     * @param sources The collection of source objects
     * @return A collection of mapped target objects
     */
    default java.util.List<T> mapAll(java.util.Collection<S> sources) {
        if (sources == null) {
            return java.util.List.of();
        }
        return sources.stream().map(this::map).toList();
    }

    /**
     * Creates a mapper that applies a function to the result of this mapper.
     * 
     * @param after The function to apply after mapping
     * @param <U> The type of the final result
     * @return A new mapper that applies the additional transformation
     */
    default <U> Mapper<S, U> andThen(java.util.function.Function<T, U> after) {
        return source -> after.apply(map(source));
    }

    /**
     * Creates a mapper that applies a function before this mapper.
     * 
     * @param before The function to apply before mapping
     * @param <U> The type of the source for the new mapper
     * @return A new mapper that applies the additional transformation
     */
    default <U> Mapper<U, T> compose(java.util.function.Function<U, S> before) {
        return source -> map(before.apply(source));
    }

    /**
     * Creates a mapper that only maps when the source is not null.
     * 
     * @return A new mapper that handles null sources gracefully
     */
    default Mapper<S, T> nullSafe() {
        return source -> source != null ? map(source) : null;
    }
}

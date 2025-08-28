package cl.demo.polizas.seedwork.application;

/**
 * Marker interface for query use cases. Queries are read-only operations that don't modify the system state.
 * 
 * @param <I> The type of the query input/parameters
 * @param <O> The type of the query result
 */
public interface Query<I, O> extends UseCase<I, O> {

    /**
     * Queries are read-only operations. This method is inherited from UseCase but emphasizes the read-only nature.
     * 
     * @param input The query parameters
     * @return The query result
     */
    @Override
    O handle(I input);
}

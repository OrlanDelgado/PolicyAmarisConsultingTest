package cl.demo.polizas.seedwork.application;

/**
 * Marker interface for command use cases. Commands are operations that modify the system state.
 * 
 * @param <I> The type of the command input/parameters
 * @param <O> The type of the command result
 */
public interface Command<I, O> extends UseCase<I, O> {

    /**
     * Commands modify the system state. This method is inherited from UseCase but emphasizes the state-changing nature.
     * 
     * @param input The command parameters
     * @return The command result
     */
    @Override
    O handle(I input);
}

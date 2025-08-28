package cl.demo.polizas.seedwork.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for aggregate roots in Domain-Driven Design. Manages domain events and provides event publishing
 * capabilities.
 * 
 * @param <ID> The type of the aggregate's identifier
 */
public abstract class AggregateRoot<ID> extends Entity<ID> {

    private final List<DomainEvent> events = new ArrayList<>();

    protected AggregateRoot(ID id) {
        super(id);
    }

    /**
     * Raises a domain event that will be collected and published later.
     * 
     * @param event The domain event to raise
     */
    protected void raise(DomainEvent event) {
        events.add(event);
    }

    /**
     * Returns all pending domain events and clears the event list. This method is typically called by the application
     * layer after the aggregate has been successfully persisted.
     * 
     * @return A copy of all pending domain events
     */
    public List<DomainEvent> pullEvents() {
        List<DomainEvent> copy = List.copyOf(events);
        events.clear();
        return copy;
    }

    /**
     * Checks if there are any pending domain events.
     * 
     * @return true if there are pending events, false otherwise
     */
    public boolean hasEvents() {
        return !events.isEmpty();
    }

    /**
     * Returns the number of pending domain events.
     * 
     * @return The count of pending events
     */
    public int eventCount() {
        return events.size();
    }
}

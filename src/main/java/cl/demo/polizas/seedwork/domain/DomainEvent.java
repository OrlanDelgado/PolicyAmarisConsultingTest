package cl.demo.polizas.seedwork.domain;

import java.time.Instant;

/**
 * Contract for domain events in the system. All domain events must implement this interface to ensure consistent event
 * structure and metadata.
 */
public interface DomainEvent {

    /**
     * Returns the type/category of this domain event. This is typically the simple class name of the event.
     * 
     * @return The event type as a string
     */
    String type();

    /**
     * Returns the timestamp when this event occurred.
     * 
     * @return The instant when the event occurred
     */
    Instant occurredOn();

    /**
     * Returns the identifier of the aggregate that raised this event.
     * 
     * @return The aggregate ID as a string
     */
    String aggregateId();

    /**
     * Returns the version of the aggregate when this event was raised. This is useful for optimistic concurrency
     * control.
     * 
     * @return The aggregate version, or null if not applicable
     */
    default Long aggregateVersion() {
        return null;
    }

    /**
     * Returns additional metadata about this event.
     * 
     * @return Event metadata, or null if none
     */
    default String metadata() {
        return null;
    }
}

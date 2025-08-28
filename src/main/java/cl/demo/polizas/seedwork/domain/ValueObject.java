package cl.demo.polizas.seedwork.domain;

/**
 * Base class for value objects in Domain-Driven Design. Value objects are immutable and their equality is based on
 * their state, not their identity. They should be designed to be thread-safe.
 */
public abstract class ValueObject {

    /**
     * Value objects are equal if their state is equal. Subclasses should override this method to compare their specific
     * fields.
     * 
     * @param obj The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Hash code should be consistent with equals. Subclasses should override this method to include all relevant
     * fields.
     * 
     * @return The hash code for this value object
     */
    @Override
    public abstract int hashCode();

    /**
     * String representation should include all relevant state. Subclasses should override this method to provide
     * meaningful output.
     * 
     * @return String representation of this value object
     */
    @Override
    public abstract String toString();

    /**
     * Validates that the value object is in a valid state. This method is called during construction to ensure
     * invariants.
     * 
     * @throws IllegalArgumentException if the value object is invalid
     */
    protected void validate() {
        // Default implementation does nothing
        // Subclasses should override to add validation logic
    }

    /**
     * Creates a defensive copy of this value object. Since value objects are immutable, this method typically returns
     * this.
     * 
     * @return A copy of this value object
     */
    public ValueObject copy() {
        return this; // Default implementation for immutable objects
    }
}

package cl.demo.polizas.domain.valueobject;

import cl.demo.polizas.seedwork.domain.Guard;
import cl.demo.polizas.seedwork.domain.ValueObject;

import java.util.UUID;

/**
 * Value Object representing a Policy ID.
 * Immutable and validated identifier for policies.
 */
public final class PolicyId extends ValueObject {

    private final UUID value;

    private PolicyId(UUID value) {
        this.value = value;
        validate();
    }

    /**
     * Creates a new PolicyId with a random UUID.
     */
    public static PolicyId generate() {
        return new PolicyId(UUID.randomUUID());
    }

    /**
     * Creates a PolicyId from an existing UUID string.
     */
    public static PolicyId from(String uuid) {
        Guard.notNullOrEmpty(uuid, "Policy ID cannot be null or empty");
        try {
            return new PolicyId(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format: " + uuid);
        }
    }

    /**
     * Creates a PolicyId from an existing UUID.
     */
    public static PolicyId from(UUID uuid) {
        Guard.notNull(uuid, "Policy ID cannot be null");
        return new PolicyId(uuid);
    }

    /**
     * Gets the UUID value.
     */
    public UUID value() {
        return value;
    }

    /**
     * Gets the string representation of the UUID.
     */
    public String toString() {
        return value.toString();
    }

    @Override
    protected void validate() {
        Guard.notNull(value, "Policy ID value cannot be null");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PolicyId policyId = (PolicyId) obj;
        return value.equals(policyId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

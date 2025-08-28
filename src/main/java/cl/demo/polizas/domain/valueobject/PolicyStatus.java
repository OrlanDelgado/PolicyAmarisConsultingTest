package cl.demo.polizas.domain.valueobject;

/**
 * Enum representing the possible states of a Policy.
 * Defines the lifecycle states a policy can have.
 */
public enum PolicyStatus {
    
    /**
     * Policy is active and valid.
     */
    ACTIVE("Activa"),
    
    /**
     * Policy is inactive or suspended.
     */
    INACTIVE("Inactiva"),
    
    /**
     * Policy has been cancelled.
     */
    CANCELLED("Anulada"),
    
    /**
     * Policy is pending activation.
     */
    PENDING("Pendiente"),
    
    /**
     * Policy has expired.
     */
    EXPIRED("Vencida");

    private final String displayName;

    PolicyStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name in Spanish.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Checks if the policy is in a valid state for operations.
     */
    public boolean isValid() {
        return this == ACTIVE || this == PENDING;
    }

    /**
     * Checks if the policy can be cancelled.
     */
    public boolean canBeCancelled() {
        return this == ACTIVE || this == PENDING;
    }

    /**
     * Checks if the policy can be activated.
     */
    public boolean canBeActivated() {
        return this == PENDING;
    }

    /**
     * Gets the next valid status when activating a policy.
     */
    public PolicyStatus getNextStatus() {
        switch (this) {
            case PENDING:
                return ACTIVE;
            case ACTIVE:
                return INACTIVE;
            default:
                return this;
        }
    }
}

package cl.demo.polizas.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import cl.demo.polizas.domain.valueobject.Money;
import cl.demo.polizas.domain.valueobject.PolicyId;
import cl.demo.polizas.domain.valueobject.PolicyStatus;
import cl.demo.polizas.domain.valueobject.Rut;
import cl.demo.polizas.seedwork.domain.AggregateRoot;
import cl.demo.polizas.seedwork.domain.Clock;
import cl.demo.polizas.seedwork.domain.DomainEvent;

/**
 * Policy Aggregate Root. Represents an insurance policy with business rules and state management.
 */
public final class Policy extends AggregateRoot<PolicyId> {

    private PolicyId id;
    private String policyNumber;
    private Rut clientRut;
    private String clientName;
    private String clientEmail;
    private Money premium;
    private Money coverage;
    private PolicyStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private String insuranceType;

    // Constructor privado para factory methods
    private Policy() {
        super(PolicyId.generate());
    }

    /**
     * Creates a new policy with initial data.
     */
    public static Policy create(String policyNumber, Rut clientRut, String clientName, String clientEmail,
            Money premium, Money coverage, LocalDate startDate, LocalDate endDate, String description,
            String insuranceType) {

        Policy policy = new Policy();
        policy.id = PolicyId.generate();
        policy.policyNumber = policyNumber;
        policy.clientRut = clientRut;
        policy.clientName = clientName;
        policy.clientEmail = clientEmail;
        policy.premium = premium;
        policy.coverage = coverage;
        policy.startDate = startDate;
        policy.endDate = endDate;
        policy.description = description;
        policy.insuranceType = insuranceType;
        policy.status = PolicyStatus.PENDING;
        policy.createdAt = Clock.SYSTEM.nowLocal();
        policy.updatedAt = Clock.SYSTEM.nowLocal();

        // Validar reglas de negocio
        policy.validateBusinessRules();

        // Publicar evento de dominio
        policy.raise(new PolicyCreatedEvent(policy.id.toString(), policy.policyNumber, policy.clientRut.getFullRut()));

        return policy;
    }

    /**
     * Activates the policy.
     */
    public void activate() {
        if (!status.canBeActivated()) {
            throw new IllegalStateException("Policy cannot be activated from status: " + status);
        }

        status = PolicyStatus.ACTIVE;
        updatedAt = Clock.SYSTEM.nowLocal();

        // Publicar evento de dominio
        raise(new PolicyActivatedEvent(id.toString(), policyNumber, status.name()));
    }

    /**
     * Cancels the policy.
     */
    public void cancel() {
        if (!status.canBeCancelled()) {
            throw new IllegalStateException("Policy cannot be cancelled from status: " + status);
        }

        status = PolicyStatus.CANCELLED;
        updatedAt = Clock.SYSTEM.nowLocal();

        // Publicar evento de dominio
        raise(new PolicyCancelledEvent(id.toString(), policyNumber, status.name()));
    }

    /**
     * Updates the policy status.
     */
    public void updateStatus(PolicyStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }

        if (status == newStatus) {
            return; // No change needed
        }

        // Validar transición de estado
        validateStatusTransition(newStatus);

        status = newStatus;
        updatedAt = Clock.SYSTEM.nowLocal();

        // Publicar evento de dominio
        raise(new PolicyStatusUpdatedEvent(id.toString(), policyNumber, status.name(), newStatus.name()));
    }

    /**
     * Validates business rules for policy creation.
     */
    private void validateBusinessRules() {
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Policy number is required");
        }

        if (clientName == null || clientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Client name is required");
        }

        if (clientEmail == null || clientEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Client email is required");
        }

        if (premium == null || premium.isZero()) {
            throw new IllegalArgumentException("Premium must be greater than zero");
        }

        if (coverage == null || coverage.isZero()) {
            throw new IllegalArgumentException("Coverage must be greater than zero");
        }

        if (startDate == null) {
            throw new IllegalArgumentException("Start date is required");
        }

        if (endDate == null) {
            throw new IllegalArgumentException("End date is required");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
    }

    /**
     * Validates status transition.
     */
    private void validateStatusTransition(PolicyStatus newStatus) {
        switch (status) {
            case PENDING:
                if (newStatus != PolicyStatus.ACTIVE && newStatus != PolicyStatus.CANCELLED) {
                    throw new IllegalStateException("Pending policy can only be activated or cancelled");
                }
                break;
            case ACTIVE:
                if (newStatus != PolicyStatus.INACTIVE && newStatus != PolicyStatus.CANCELLED) {
                    throw new IllegalStateException("Active policy can only be deactivated or cancelled");
                }
                break;
            case INACTIVE:
                if (newStatus != PolicyStatus.ACTIVE && newStatus != PolicyStatus.CANCELLED) {
                    throw new IllegalStateException("Inactive policy can only be activated or cancelled");
                }
                break;
            case CANCELLED:
                throw new IllegalStateException("Cancelled policy cannot change status");
            case EXPIRED:
                throw new IllegalStateException("Expired policy cannot change status");
        }
    }

    // Getters
    @Override
    public PolicyId id() {
        return id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public Rut getClientRut() {
        return clientRut;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public Money getPremium() {
        return premium;
    }

    public Money getCoverage() {
        return coverage;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    /**
     * Checks if the policy is active.
     */
    public boolean isActive() {
        return status == PolicyStatus.ACTIVE;
    }

    /**
     * Checks if the policy can be modified.
     */
    public boolean canBeModified() {
        return status == PolicyStatus.PENDING || status == PolicyStatus.ACTIVE;
    }

    // Domain Events
    public static final class PolicyCreatedEvent implements DomainEvent {
        private final String policyId;
        private final String policyNumber;
        private final String clientRut;
        private final Instant occurredOn;

        public PolicyCreatedEvent(String policyId, String policyNumber, String clientRut) {
            this.policyId = policyId;
            this.policyNumber = policyNumber;
            this.clientRut = clientRut;
            this.occurredOn = Clock.SYSTEM.now();
        }

        @Override
        public String type() {
            return "PolicyCreated";
        }

        @Override
        public Instant occurredOn() {
            return occurredOn;
        }

        @Override
        public String aggregateId() {
            return policyId;
        }

        public String getPolicyNumber() {
            return policyNumber;
        }

        public String getClientRut() {
            return clientRut;
        }
    }

    public static final class PolicyActivatedEvent implements DomainEvent {
        private final String policyId;
        private final String policyNumber;
        private final String status;
        private final Instant occurredOn;

        public PolicyActivatedEvent(String policyId, String policyNumber, String status) {
            this.policyId = policyId;
            this.policyNumber = policyNumber;
            this.status = status;
            this.occurredOn = Clock.SYSTEM.now();
        }

        @Override
        public String type() {
            return "PolicyActivated";
        }

        @Override
        public Instant occurredOn() {
            return occurredOn;
        }

        @Override
        public String aggregateId() {
            return policyId;
        }

        public String getPolicyNumber() {
            return policyNumber;
        }

        public String getStatus() {
            return status;
        }
    }

    public static final class PolicyCancelledEvent implements DomainEvent {
        private final String policyId;
        private final String policyNumber;
        private final String status;
        private final Instant occurredOn;

        public PolicyCancelledEvent(String policyId, String policyNumber, String status) {
            this.policyId = policyId;
            this.policyNumber = policyNumber;
            this.status = status;
            this.occurredOn = Clock.SYSTEM.now();
        }

        @Override
        public String type() {
            return "PolicyCancelled";
        }

        @Override
        public Instant occurredOn() {
            return occurredOn;
        }

        @Override
        public String aggregateId() {
            return policyId;
        }

        public String getPolicyNumber() {
            return policyNumber;
        }

        public String getStatus() {
            return status;
        }
    }

    public static final class PolicyStatusUpdatedEvent implements DomainEvent {
        private final String policyId;
        private final String policyNumber;
        private final String oldStatus;
        private final String newStatus;
        private final Instant occurredOn;

        public PolicyStatusUpdatedEvent(String policyId, String policyNumber, String oldStatus, String newStatus) {
            this.policyId = policyId;
            this.policyNumber = policyNumber;
            this.oldStatus = oldStatus;
            this.newStatus = newStatus;
            this.occurredOn = Clock.SYSTEM.now();
        }

        @Override
        public String type() {
            return "PolicyStatusUpdated";
        }

        @Override
        public Instant occurredOn() {
            return occurredOn;
        }

        @Override
        public String aggregateId() {
            return policyId;
        }

        public String getPolicyNumber() {
            return policyNumber;
        }

        public String getOldStatus() {
            return oldStatus;
        }

        public String getNewStatus() {
            return newStatus;
        }
    }
}

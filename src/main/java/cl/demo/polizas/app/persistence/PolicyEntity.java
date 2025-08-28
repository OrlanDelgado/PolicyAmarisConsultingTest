package cl.demo.polizas.app.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import cl.demo.polizas.domain.model.Policy;
import cl.demo.polizas.domain.valueobject.Money;
import cl.demo.polizas.domain.valueobject.PolicyStatus;
import cl.demo.polizas.domain.valueobject.Rut;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * JPA Entity for Policy persistence. Maps the domain Policy to database representation.
 */
@Entity @Table(name = "policies", indexes = {
        @Index(name = "idx_policy_number", columnList = "policy_number", unique = true),
        @Index(name = "idx_client_rut", columnList = "client_rut"), @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_created_at", columnList = "created_at") })
public class PolicyEntity extends PanacheEntityBase {

    @Id @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "policy_number", nullable = false, length = 50, unique = true) @NotBlank(message = "Policy number is required") @Size(max = 50, message = "Policy number cannot exceed 50 characters")
    private String policyNumber;

    @Column(name = "client_rut", nullable = false, length = 12) @NotBlank(message = "Client RUT is required") @Size(max = 12, message = "Client RUT cannot exceed 12 characters")
    private String clientRut;

    @Column(name = "client_name", nullable = false, length = 100) @NotBlank(message = "Client name is required") @Size(max = 100, message = "Client name cannot exceed 100 characters")
    private String clientName;

    @Column(name = "client_email", nullable = false, length = 100) @NotBlank(message = "Client email is required") @Email(message = "Invalid email format") @Size(max = 100, message = "Client email cannot exceed 100 characters")
    private String clientEmail;

    @Column(name = "premium_amount", nullable = false, precision = 15, scale = 2) @NotNull(message = "Premium amount is required") @DecimalMin(value = "0.01", message = "Premium must be greater than zero") @DecimalMax(value = "999999999999.99", message = "Premium amount too large")
    private BigDecimal premiumAmount;

    @Column(name = "premium_currency", nullable = false, length = 3) @NotBlank(message = "Premium currency is required") @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String premiumCurrency;

    @Column(name = "coverage_amount", nullable = false, precision = 15, scale = 2) @NotNull(message = "Coverage amount is required") @DecimalMin(value = "0.01", message = "Coverage must be greater than zero") @DecimalMax(value = "999999999999.99", message = "Coverage amount too large")
    private BigDecimal coverageAmount;

    @Column(name = "coverage_currency", nullable = false, length = 3) @NotBlank(message = "Coverage currency is required") @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String coverageCurrency;

    @Enumerated(EnumType.STRING) @Column(name = "status", nullable = false, length = 20) @NotNull(message = "Status is required")
    private PolicyStatus status;

    @Column(name = "start_date", nullable = false) @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false) @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Column(name = "description", length = 500) @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Column(name = "insurance_type", length = 50) @Size(max = 50, message = "Insurance type cannot exceed 50 characters")
    private String insuranceType;

    @Column(name = "created_at", nullable = false) @NotNull(message = "Created at timestamp is required")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false) @NotNull(message = "Updated at timestamp is required")
    private LocalDateTime updatedAt;

    // Constructor por defecto para JPA
    public PolicyEntity() {
    }

    // Constructor para crear desde el dominio
    public static PolicyEntity fromDomain(Policy policy) {
        PolicyEntity entity = new PolicyEntity();
        entity.id = policy.id().toString();
        entity.policyNumber = policy.getPolicyNumber();
        entity.clientRut = policy.getClientRut().getFullRut();
        entity.clientName = policy.getClientName();
        entity.clientEmail = policy.getClientEmail();
        entity.premiumAmount = policy.getPremium().amount();
        entity.premiumCurrency = policy.getPremium().currency().getCurrencyCode();
        entity.coverageAmount = policy.getCoverage().amount();
        entity.coverageCurrency = policy.getCoverage().currency().getCurrencyCode();
        entity.status = policy.getStatus();
        entity.startDate = policy.getStartDate();
        entity.endDate = policy.getEndDate();
        entity.description = policy.getDescription();
        entity.insuranceType = policy.getInsuranceType();
        entity.createdAt = policy.getCreatedAt();
        entity.updatedAt = policy.getUpdatedAt();
        return entity;
    }

    // Método para convertir a dominio
    public Policy toDomain() {
        return Policy.create(policyNumber, Rut.from(clientRut), clientName, clientEmail,
                Money.of(premiumAmount, java.util.Currency.getInstance(premiumCurrency)),
                Money.of(coverageAmount, java.util.Currency.getInstance(coverageCurrency)), startDate, endDate,
                description, insuranceType);
    }

    // Método para actualizar desde el dominio
    public void updateFromDomain(Policy policy) {
        this.policyNumber = policy.getPolicyNumber();
        this.clientRut = policy.getClientRut().getFullRut();
        this.clientName = policy.getClientName();
        this.clientEmail = policy.getClientEmail();
        this.premiumAmount = policy.getPremium().amount();
        this.premiumCurrency = policy.getPremium().currency().getCurrencyCode();
        this.coverageAmount = policy.getCoverage().amount();
        this.coverageCurrency = policy.getCoverage().currency().getCurrencyCode();
        this.status = policy.getStatus();
        this.startDate = policy.getStartDate();
        this.endDate = policy.getEndDate();
        this.description = policy.getDescription();
        this.insuranceType = policy.getInsuranceType();
        this.updatedAt = policy.getUpdatedAt();
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getClientRut() {
        return clientRut;
    }

    public void setClientRut(String clientRut) {
        this.clientRut = clientRut;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getPremiumCurrency() {
        return premiumCurrency;
    }

    public void setPremiumCurrency(String premiumCurrency) {
        this.premiumCurrency = premiumCurrency;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public String getCoverageCurrency() {
        return coverageCurrency;
    }

    public void setCoverageCurrency(String coverageCurrency) {
        this.coverageCurrency = coverageCurrency;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

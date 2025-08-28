package cl.demo.polizas.app.mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import cl.demo.polizas.domain.model.Policy;
import cl.demo.polizas.domain.valueobject.Money;
import cl.demo.polizas.domain.valueobject.Rut;
import cl.demo.polizas.seedwork.application.Mapper;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper for converting between Policy entities and DTOs. Implements the Mapper pattern for object transformation.
 */
@ApplicationScoped
public final class PolicyMapper implements Mapper<Policy, PolicyMapper.PolicyDto> {

    @Override
    public PolicyDto map(Policy source) {
        if (source == null) {
            return null;
        }

        return new PolicyDto(source.id().toString(), source.getPolicyNumber(), source.getClientName(),
                source.getClientRut().getFullRut(), source.getClientEmail(), source.getPremium().amount(),
                source.getPremium().currency().getCurrencyCode(), source.getCoverage().amount(),
                source.getCoverage().currency().getCurrencyCode(), source.getStatus().name(),
                source.getStatus().getDisplayName(), source.getStartDate(), source.getEndDate(),
                source.getDescription(), source.getInsuranceType(), source.getCreatedAt(), source.getUpdatedAt());
    }

    /**
     * Maps a PolicyDto back to a Policy entity. This is not part of the Mapper interface but provides reverse mapping
     * functionality.
     */
    public Policy reverseMap(PolicyDto target) {
        if (target == null) {
            return null;
        }

        // Crear la póliza usando el factory method del dominio
        return Policy.create(target.policyNumber(), Rut.from(target.clientRut()), target.clientName(),
                target.clientEmail(),
                Money.of(target.premiumAmount(), java.util.Currency.getInstance(target.premiumCurrency())),
                Money.of(target.coverageAmount(), java.util.Currency.getInstance(target.coverageCurrency())),
                target.startDate(), target.endDate(), target.description(), target.insuranceType());
    }

    // DTO para representar una póliza completa
    public record PolicyDto(String policyId, String policyNumber, String clientName, String clientRut,
            String clientEmail, BigDecimal premiumAmount, String premiumCurrency, BigDecimal coverageAmount,
            String coverageCurrency, String status, String statusDisplayName, LocalDate startDate, LocalDate endDate,
            String description, String insuranceType, java.time.LocalDateTime createdAt,
            java.time.LocalDateTime updatedAt) {
    }

    // Métodos de utilidad para mapeo específico
    public PolicyDto toCreateResponse(Policy policy) {
        if (policy == null) {
            return null;
        }

        return new PolicyDto(policy.id().toString(), policy.getPolicyNumber(), policy.getClientName(),
                policy.getClientRut().getFullRut(), policy.getClientEmail(), policy.getPremium().amount(),
                policy.getPremium().currency().getCurrencyCode(), policy.getCoverage().amount(),
                policy.getCoverage().currency().getCurrencyCode(), policy.getStatus().name(),
                policy.getStatus().getDisplayName(), policy.getStartDate(), policy.getEndDate(),
                policy.getDescription(), policy.getInsuranceType(), policy.getCreatedAt(), policy.getUpdatedAt());
    }

    public PolicyDto toUpdateResponse(Policy policy) {
        return toCreateResponse(policy);
    }

    public PolicyDto toSummary(Policy policy) {
        if (policy == null) {
            return null;
        }

        return new PolicyDto(policy.id().toString(), policy.getPolicyNumber(), policy.getClientName(),
                policy.getClientRut().getFullRut(), null, // No incluir email en resumen
                policy.getPremium().amount(), policy.getPremium().currency().getCurrencyCode(),
                policy.getCoverage().amount(), policy.getCoverage().currency().getCurrencyCode(),
                policy.getStatus().name(), policy.getStatus().getDisplayName(), policy.getStartDate(),
                policy.getEndDate(), null, // No incluir descripción en resumen
                policy.getInsuranceType(), policy.getCreatedAt(), policy.getUpdatedAt());
    }
}

package cl.demo.polizas.application.usecase;

import cl.demo.polizas.domain.model.Policy;
import cl.demo.polizas.domain.valueobject.PolicyId;
import cl.demo.polizas.seedwork.application.Query;
import cl.demo.polizas.domain.repository.PolicyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Use Case for retrieving a specific insurance policy. Implements the Query pattern for read-only operations.
 */
@ApplicationScoped
public final class GetPolicyUseCase
        implements Query<GetPolicyUseCase.GetPolicyRequest, GetPolicyUseCase.GetPolicyResponse> {

    private final PolicyRepository policyRepository;

    @Inject
    public GetPolicyUseCase(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public String getName() {
        return "GetPolicy";
    }

    @Override
    public GetPolicyResponse handle(GetPolicyRequest request) {
        // Validar request
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (request.policyId() == null) {
            throw new IllegalArgumentException("Policy ID cannot be null");
        }

        // Buscar la póliza en el repositorio
        Policy policy = policyRepository.findById(request.policyId())
                .orElseThrow(() -> new IllegalArgumentException("Policy not found with ID: " + request.policyId()));

        // Mapear a DTO de respuesta
        return mapToPolicyResponse(policy);
    }

    private GetPolicyResponse mapToPolicyResponse(Policy policy) {
        return new GetPolicyResponse(policy.id().toString(), policy.getPolicyNumber(), policy.getClientName(),
                policy.getClientRut().getFullRut(), policy.getClientEmail(), policy.getPremium().format(),
                policy.getCoverage().format(), policy.getStatus().getDisplayName(), policy.getStartDate(),
                policy.getEndDate(), policy.getDescription(), policy.getInsuranceType(), policy.getCreatedAt(),
                policy.getUpdatedAt());
    }

    // Request DTO
    public record GetPolicyRequest(PolicyId policyId) {
    }

    // Response DTO
    public record GetPolicyResponse(String policyId, String policyNumber, String clientName, String clientRut,
            String clientEmail, String premium, String coverage, String status, java.time.LocalDate startDate,
            java.time.LocalDate endDate, String description, String insuranceType, java.time.LocalDateTime createdAt,
            java.time.LocalDateTime updatedAt) {
    }


}

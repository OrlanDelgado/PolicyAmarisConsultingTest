package cl.demo.polizas.application.usecase;

import java.util.List;

import cl.demo.polizas.domain.model.Policy;
import cl.demo.polizas.domain.valueobject.PolicyStatus;
import cl.demo.polizas.seedwork.application.Query;
import cl.demo.polizas.domain.repository.PolicyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Use Case for listing insurance policies. Implements the Query pattern for read-only operations.
 */
@ApplicationScoped
public final class ListPoliciesUseCase
        implements Query<ListPoliciesUseCase.ListPoliciesRequest, ListPoliciesUseCase.ListPoliciesResponse> {

    private final PolicyRepository policyRepository;

    @Inject
    public ListPoliciesUseCase(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public String getName() {
        return "ListPolicies";
    }

    @Override
    public ListPoliciesResponse handle(ListPoliciesRequest request) {
        // Validar request
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        // Obtener pólizas del repositorio
        List<Policy> policies = policyRepository.findAll(request.page(), request.size(), request.status(),
                request.clientRut());

        // Contar total de pólizas para paginación
        long total = policyRepository.count(request.status(), request.clientRut());

        // Mapear a DTOs de respuesta
        List<PolicySummaryDto> policySummaries = policies.stream().map(this::mapToPolicySummary).toList();

        return new ListPoliciesResponse(policySummaries, request.page(), request.size(), total,
                calculateTotalPages(total, request.size()));
    }

    private PolicySummaryDto mapToPolicySummary(Policy policy) {
        return new PolicySummaryDto(policy.id().toString(), policy.getPolicyNumber(), policy.getClientName(),
                policy.getClientRut().getFullRut(), policy.getPremium().format(), policy.getCoverage().format(),
                policy.getStatus().getDisplayName(), policy.getStartDate(), policy.getEndDate(),
                policy.getInsuranceType(), policy.getCreatedAt());
    }

    private int calculateTotalPages(long total, int size) {
        if (size <= 0)
            return 0;
        return (int) Math.ceil((double) total / size);
    }

    // Request DTO
    public record ListPoliciesRequest(int page, int size, PolicyStatus status, String clientRut) {
        public ListPoliciesRequest {
            if (page < 0)
                page = 0;
            if (size <= 0)
                size = 10;
            if (size > 100)
                size = 100;
        }
    }

    // Response DTO
    public record ListPoliciesResponse(List<PolicySummaryDto> policies, int page, int size, long total,
            int totalPages) {
    }

    // Policy Summary DTO
    public record PolicySummaryDto(String policyId, String policyNumber, String clientName, String clientRut,
            String premium, String coverage, String status, java.time.LocalDate startDate, java.time.LocalDate endDate,
            String insuranceType, java.time.LocalDateTime createdAt) {
    }


}

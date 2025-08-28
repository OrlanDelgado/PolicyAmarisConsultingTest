package cl.demo.polizas.application.usecase;

import cl.demo.polizas.domain.model.Policy;
import cl.demo.polizas.domain.valueobject.PolicyId;
import cl.demo.polizas.domain.valueobject.PolicyStatus;
import cl.demo.polizas.seedwork.application.Command;
import cl.demo.polizas.domain.repository.PolicyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Use Case for updating the status of an insurance policy. Implements the Command pattern for state-changing
 * operations.
 */
@ApplicationScoped
public final class UpdatePolicyStatusUseCase implements
        Command<UpdatePolicyStatusUseCase.UpdatePolicyStatusRequest, UpdatePolicyStatusUseCase.UpdatePolicyStatusResponse> {

    private final PolicyRepository policyRepository;

    @Inject
    public UpdatePolicyStatusUseCase(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public String getName() {
        return "UpdatePolicyStatus";
    }

    @Override
    public UpdatePolicyStatusResponse handle(UpdatePolicyStatusRequest request) {
        // Validar request
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (request.policyId() == null) {
            throw new IllegalArgumentException("Policy ID cannot be null");
        }

        if (request.newStatus() == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }

        // Buscar la póliza en el repositorio
        Policy policy = policyRepository.findById(request.policyId())
                .orElseThrow(() -> new IllegalArgumentException("Policy not found with ID: " + request.policyId()));

        // Obtener el estado anterior
        PolicyStatus oldStatus = policy.getStatus();

        // Actualizar el estado usando la lógica del dominio
        policy.updateStatus(request.newStatus());

        // Guardar los cambios en el repositorio
        policyRepository.save(policy);

        // Retornar respuesta exitosa
        return new UpdatePolicyStatusResponse(policy.id().toString(), policy.getPolicyNumber(),
                oldStatus.getDisplayName(), policy.getStatus().getDisplayName(), policy.getUpdatedAt());
    }

    // Request DTO
    public record UpdatePolicyStatusRequest(PolicyId policyId, PolicyStatus newStatus) {
    }

    // Response DTO
    public record UpdatePolicyStatusResponse(String policyId, String policyNumber, String oldStatus, String newStatus,
            java.time.LocalDateTime updatedAt) {
    }


}

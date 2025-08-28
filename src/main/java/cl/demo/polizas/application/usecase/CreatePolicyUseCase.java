package cl.demo.polizas.application.usecase;

import cl.demo.polizas.domain.model.Policy;
import cl.demo.polizas.domain.valueobject.Money;
import cl.demo.polizas.domain.valueobject.PolicyStatus;
import cl.demo.polizas.domain.valueobject.Rut;
import cl.demo.polizas.seedwork.application.Command;
import cl.demo.polizas.seedwork.domain.Result;
import cl.demo.polizas.domain.repository.PolicyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Use Case for creating a new insurance policy.
 * Implements the Command pattern for state-changing operations.
 */
@ApplicationScoped
public final class CreatePolicyUseCase implements Command<CreatePolicyUseCase.CreatePolicyRequest, CreatePolicyUseCase.CreatePolicyResponse> {

    private final PolicyRepository policyRepository;

    @Inject
    public CreatePolicyUseCase(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public String getName() {
        return "CreatePolicy";
    }

    @Override
    public CreatePolicyResponse handle(CreatePolicyRequest request) {
        // Validar request
        var validationResult = validateRequest(request);
        if (validationResult.isFailure()) {
            throw new IllegalArgumentException(validationResult.getFirstError());
        }

        // Crear la póliza usando el dominio
        Policy policy = Policy.create(
                request.policyNumber(),
                request.clientRut(),
                request.clientName(),
                request.clientEmail(),
                request.premium(),
                request.coverage(),
                request.startDate(),
                request.endDate(),
                request.description(),
                request.insuranceType()
        );

        // Guardar en el repositorio
        policyRepository.save(policy);

        // Retornar respuesta exitosa
        return new CreatePolicyResponse(
                policy.id().toString(),
                policy.getPolicyNumber(),
                policy.getStatus().getDisplayName(),
                policy.getCreatedAt()
        );
    }

    private Result<Void> validateRequest(CreatePolicyRequest request) {
        if (request == null) {
            return Result.fail("Request cannot be null");
        }

        if (request.policyNumber() == null || request.policyNumber().trim().isEmpty()) {
            return Result.fail("Policy number is required");
        }

        if (request.clientRut() == null) {
            return Result.fail("Client RUT is required");
        }

        if (request.clientName() == null || request.clientName().trim().isEmpty()) {
            return Result.fail("Client name is required");
        }

        if (request.clientEmail() == null || request.clientEmail().trim().isEmpty()) {
            return Result.fail("Client email is required");
        }

        if (request.premium() == null || request.premium().isZero()) {
            return Result.fail("Premium must be greater than zero");
        }

        if (request.coverage() == null || request.coverage().isZero()) {
            return Result.fail("Coverage must be greater than zero");
        }

        if (request.startDate() == null) {
            return Result.fail("Start date is required");
        }

        if (request.endDate() == null) {
            return Result.fail("End date is required");
        }

        if (request.startDate().isAfter(request.endDate())) {
            return Result.fail("Start date cannot be after end date");
        }

        if (request.startDate().isBefore(LocalDate.now())) {
            return Result.fail("Start date cannot be in the past");
        }

        return Result.ok(null);
    }

    // Request DTO
    public record CreatePolicyRequest(
            String policyNumber,
            Rut clientRut,
            String clientName,
            String clientEmail,
            Money premium,
            Money coverage,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            String insuranceType
    ) {}

    // Response DTO
    public record CreatePolicyResponse(
            String policyId,
            String policyNumber,
            String status,
            java.time.LocalDateTime createdAt
    ) {}


}

package cl.demo.polizas.app.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import cl.demo.polizas.application.usecase.CreatePolicyUseCase;
import cl.demo.polizas.application.usecase.GetPolicyUseCase;
import cl.demo.polizas.application.usecase.ListPoliciesUseCase;
import cl.demo.polizas.application.usecase.UpdatePolicyStatusUseCase;
import cl.demo.polizas.domain.valueobject.Money;
import cl.demo.polizas.domain.valueobject.PolicyId;
import cl.demo.polizas.domain.valueobject.PolicyStatus;
import cl.demo.polizas.domain.valueobject.Rut;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST Resource for Policy management. Provides endpoints for CRUD operations on insurance policies.
 */
@Path("/policies") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON) @Tag(name = "Policies", description = "Policy management operations")
public class PolicyResource {

    @Inject
    CreatePolicyUseCase createPolicyUseCase;

    @Inject
    ListPoliciesUseCase listPoliciesUseCase;

    @Inject
    GetPolicyUseCase getPolicyUseCase;

    @Inject
    UpdatePolicyStatusUseCase updatePolicyStatusUseCase;

    @POST @Operation(summary = "Create a new policy", description = "Creates a new insurance policy with the provided data") @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Policy created successfully", content = @Content(schema = @Schema(implementation = CreatePolicyResponse.class))),
            @APIResponse(responseCode = "400", description = "Invalid input data"),
            @APIResponse(responseCode = "500", description = "Internal server error") })
    public Response createPolicy(@Valid CreatePolicyRequest request) {
        try {
            Log.info("Creating new policy: " + request.policyNumber());

            // Crear la póliza usando el caso de uso
            var response = createPolicyUseCase.handle(new CreatePolicyUseCase.CreatePolicyRequest(
                    request.policyNumber(), Rut.from(request.clientRut()), request.clientName(), request.clientEmail(),
                    Money.of(request.premium()), Money.of(request.coverage()), request.startDate(), request.endDate(),
                    request.description(), request.insuranceType()));

            Log.info("Policy created successfully with ID: " + response.policyId());

            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (IllegalArgumentException e) {
            Log.warn("Invalid request for policy creation: " + e.getMessage());
            return createErrorResponse(400, "Bad Request", e.getMessage());
        } catch (Exception e) {
            Log.error("Error creating policy", e);
            return createErrorResponse(500, "Internal Server Error", "An unexpected error occurred");
        }
    }

    @GET @Operation(summary = "List policies", description = "Retrieves a paginated list of policies with optional filtering") @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Policies retrieved successfully", content = @Content(schema = @Schema(implementation = ListPoliciesResponse.class))),
            @APIResponse(responseCode = "400", description = "Invalid query parameters"),
            @APIResponse(responseCode = "500", description = "Internal server error") })
    public Response listPolicies(@QueryParam("page") @DefaultValue("0") @Min(0) int page,
            @QueryParam("size") @DefaultValue("10") @Min(1) @Max(100) int size, @QueryParam("status") String status,
            @QueryParam("clientRut") String clientRut) {

        try {
            Log.info("Listing policies - page: " + page + ", size: " + size);

            // Parse status if provided
            PolicyStatus policyStatus = null;
            if (status != null && !status.trim().isEmpty()) {
                try {
                    policyStatus = PolicyStatus.valueOf(status.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return createErrorResponse(400, "Bad Request", "Invalid status: " + status);
                }
            }

            // Obtener pólizas usando el caso de uso
            var request = new ListPoliciesUseCase.ListPoliciesRequest(page, size, policyStatus, clientRut);
            var response = listPoliciesUseCase.handle(request);

            Log.info("Retrieved " + response.policies().size() + " policies");

            return Response.ok(response).build();

        } catch (Exception e) {
            Log.error("Error listing policies", e);
            return createErrorResponse(500, "Internal Server Error", "An unexpected error occurred");
        }
    }

    @GET @Path("/{id}") @Operation(summary = "Get policy by ID", description = "Retrieves a specific policy by its unique identifier") @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Policy retrieved successfully", content = @Content(schema = @Schema(implementation = GetPolicyResponse.class))),
            @APIResponse(responseCode = "400", description = "Invalid policy ID"),
            @APIResponse(responseCode = "404", description = "Policy not found"),
            @APIResponse(responseCode = "500", description = "Internal server error") })
    public Response getPolicy(@PathParam("id") String id) {
        try {
            Log.info("Getting policy with ID: " + id);

            // Validar y parsear el ID
            PolicyId policyId;
            try {
                policyId = PolicyId.from(id);
            } catch (IllegalArgumentException e) {
                return createErrorResponse(400, "Bad Request", "Invalid policy ID format: " + id);
            }

            // Obtener la póliza usando el caso de uso
            var request = new GetPolicyUseCase.GetPolicyRequest(policyId);
            var response = getPolicyUseCase.handle(request);

            Log.info("Policy retrieved successfully: " + response.policyNumber());

            return Response.ok(response).build();

        } catch (IllegalArgumentException e) {
            Log.warn("Policy not found: " + e.getMessage());
            return createErrorResponse(404, "Not Found", e.getMessage());
        } catch (Exception e) {
            Log.error("Error getting policy", e);
            return createErrorResponse(500, "Internal Server Error", "An unexpected error occurred");
        }
    }

    @PUT @Path("/{id}/status") @Operation(summary = "Update policy status", description = "Updates the status of an existing policy") @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Policy status updated successfully", content = @Content(schema = @Schema(implementation = UpdatePolicyStatusResponse.class))),
            @APIResponse(responseCode = "400", description = "Invalid request data"),
            @APIResponse(responseCode = "404", description = "Policy not found"),
            @APIResponse(responseCode = "409", description = "Status transition not allowed"),
            @APIResponse(responseCode = "500", description = "Internal server error") })
    public Response updatePolicyStatus(@PathParam("id") String id, @Valid UpdatePolicyStatusRequest request) {

        try {
            Log.info("Updating status for policy ID: " + id + " to: " + request.status());

            // Validar y parsear el ID
            PolicyId policyId;
            try {
                policyId = PolicyId.from(id);
            } catch (IllegalArgumentException e) {
                return createErrorResponse(400, "Bad Request", "Invalid policy ID format: " + id);
            }

            // Validar y parsear el status
            PolicyStatus newStatus;
            try {
                newStatus = PolicyStatus.valueOf(request.status().toUpperCase());
            } catch (IllegalArgumentException e) {
                return createErrorResponse(400, "Bad Request", "Invalid status: " + request.status());
            }

            // Actualizar el status usando el caso de uso
            var useCaseRequest = new UpdatePolicyStatusUseCase.UpdatePolicyStatusRequest(policyId, newStatus);
            var response = updatePolicyStatusUseCase.handle(useCaseRequest);

            Log.info("Policy status updated successfully: " + response.oldStatus() + " -> " + response.newStatus());

            return Response.ok(response).build();

        } catch (IllegalArgumentException e) {
            Log.warn("Policy not found: " + e.getMessage());
            return createErrorResponse(404, "Not Found", e.getMessage());
        } catch (IllegalStateException e) {
            Log.warn("Status transition not allowed: " + e.getMessage());
            return createErrorResponse(409, "Conflict", e.getMessage());
        } catch (Exception e) {
            Log.error("Error updating policy status", e);
            return createErrorResponse(500, "Internal Server Error", "An unexpected error occurred");
        }
    }

    /**
     * Creates a standardized error response following RFC 7807.
     */
    private Response createErrorResponse(int status, String title, String detail) {
        var errorResponse = new ErrorResponse("https://api.polizas.demo.cl/errors/" + status, title, status, detail);

        return Response.status(status).entity(errorResponse).build();
    }

    // Request DTOs
    public record CreatePolicyRequest(@NotBlank(message = "Policy number is required") String policyNumber,
            @NotBlank(message = "Client RUT is required") String clientRut,
            @NotBlank(message = "Client name is required") String clientName,
            @NotBlank(message = "Client email is required") @Email(message = "Invalid email format") String clientEmail,
            @NotNull(message = "Premium is required") @DecimalMin(value = "0.01", message = "Premium must be greater than zero") BigDecimal premium,
            @NotNull(message = "Coverage is required") @DecimalMin(value = "0.01", message = "Coverage must be greater than zero") BigDecimal coverage,
            @NotNull(message = "Start date is required") LocalDate startDate,
            @NotNull(message = "End date is required") LocalDate endDate, String description, String insuranceType) {
    }

    public record UpdatePolicyStatusRequest(@NotBlank(message = "Status is required") String status) {
    }

    // Response DTOs
    public record CreatePolicyResponse(String policyId, String policyNumber, String status,
            java.time.LocalDateTime createdAt) {
    }

    public record ListPoliciesResponse(List<PolicySummaryDto> policies, int page, int size, long total,
            int totalPages) {
    }

    public record PolicySummaryDto(String policyId, String policyNumber, String clientName, String clientRut,
            String premium, String coverage, String status, LocalDate startDate, LocalDate endDate,
            String insuranceType, java.time.LocalDateTime createdAt) {
    }

    public record GetPolicyResponse(String policyId, String policyNumber, String clientName, String clientRut,
            String clientEmail, String premium, String coverage, String status, LocalDate startDate, LocalDate endDate,
            String description, String insuranceType, java.time.LocalDateTime createdAt,
            java.time.LocalDateTime updatedAt) {
    }

    public record UpdatePolicyStatusResponse(String policyId, String policyNumber, String oldStatus, String newStatus,
            java.time.LocalDateTime updatedAt) {
    }

    // Error Response DTO following RFC 7807
    public record ErrorResponse(String type, String title, int status, String detail) {
    }
}

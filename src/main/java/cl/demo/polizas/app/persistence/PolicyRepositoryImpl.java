package cl.demo.polizas.app.persistence;

import cl.demo.polizas.domain.model.Policy;
import cl.demo.polizas.domain.valueobject.PolicyId;
import cl.demo.polizas.domain.valueobject.PolicyStatus;
import cl.demo.polizas.domain.repository.PolicyRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of Policy repository using Panache. Provides database operations for Policy entities.
 */
@ApplicationScoped @Transactional
public class PolicyRepositoryImpl implements PolicyRepository, PanacheRepository<PolicyEntity> {

    @Override
    public void save(Policy policy) {
        // Verificar si ya existe
        Optional<PolicyEntity> existingEntity = find("id", policy.id().toString()).firstResultOptional();

        if (existingEntity.isPresent()) {
            // Actualizar entidad existente
            PolicyEntity entity = existingEntity.get();
            entity.updateFromDomain(policy);
            persist(entity);
        } else {
            // Crear nueva entidad
            PolicyEntity entity = PolicyEntity.fromDomain(policy);
            persist(entity);
        }
    }

    @Override
    public Optional<Policy> findById(PolicyId policyId) {
        return find("id", policyId.toString()).firstResultOptional().map(PolicyEntity::toDomain);
    }

    @Override
    public List<Policy> findAll(int page, int size, PolicyStatus status, String clientRut) {
        StringBuilder query = new StringBuilder();
        Parameters parameters = new Parameters();

        // Construir query dinámica
        if (status != null && clientRut != null && !clientRut.trim().isEmpty()) {
            query.append("status = :status and clientRut = :clientRut");
            parameters.and("status", status);
            parameters.and("clientRut", clientRut);
        } else if (status != null) {
            query.append("status = :status");
            parameters.and("status", status);
        } else if (clientRut != null && !clientRut.trim().isEmpty()) {
            query.append("clientRut = :clientRut");
            parameters.and("clientRut", clientRut);
        }

        // Aplicar paginación
        if (query.length() > 0) {
            return find(query.toString(), parameters).page(page, size).list().stream().map(PolicyEntity::toDomain)
                    .toList();
        } else {
            return findAll().page(page, size).list().stream().map(PolicyEntity::toDomain).toList();
        }
    }

    @Override
    public long count(PolicyStatus status, String clientRut) {
        StringBuilder query = new StringBuilder();
        Parameters parameters = new Parameters();

        // Construir query dinámica para contar
        if (status != null && clientRut != null && !clientRut.trim().isEmpty()) {
            query.append("status = :status and clientRut = :clientRut");
            parameters.and("status", status);
            parameters.and("clientRut", clientRut);
        } else if (status != null) {
            query.append("status = :status");
            parameters.and("status", status);
        } else if (clientRut != null && !clientRut.trim().isEmpty()) {
            query.append("clientRut = :clientRut");
            parameters.and("clientRut", clientRut);
        }

        // Contar registros
        if (query.length() > 0) {
            return count(query.toString(), parameters);
        } else {
            return count();
        }
    }

    @Override
    public boolean existsByPolicyNumber(String policyNumber) {
        return count("policyNumber", policyNumber) > 0;
    }

    // Métodos adicionales de utilidad
    public Optional<Policy> findByPolicyNumber(String policyNumber) {
        return find("policyNumber", policyNumber).firstResultOptional().map(PolicyEntity::toDomain);
    }

    public List<Policy> findByStatus(PolicyStatus status) {
        return find("status", status).list().stream().map(PolicyEntity::toDomain).toList();
    }

    public List<Policy> findByClientRut(String clientRut) {
        return find("clientRut", clientRut).list().stream().map(PolicyEntity::toDomain).toList();
    }

    public long countByStatus(PolicyStatus status) {
        return count("status", status);
    }

    public long countByClientRut(String clientRut) {
        return count("clientRut", clientRut);
    }

    public void deleteById(PolicyId policyId) {
        delete("id", policyId.toString());
    }

    public void deleteByPolicyNumber(String policyNumber) {
        delete("policyNumber", policyNumber);
    }

    public boolean existsById(PolicyId policyId) {
        return count("id", policyId.toString()) > 0;
    }
}

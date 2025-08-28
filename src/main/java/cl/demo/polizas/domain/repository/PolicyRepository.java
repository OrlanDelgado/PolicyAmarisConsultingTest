package cl.demo.polizas.domain.repository;

import cl.demo.polizas.domain.model.Policy;
import cl.demo.polizas.domain.valueobject.PolicyId;
import cl.demo.polizas.domain.valueobject.PolicyStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Policy domain entity.
 * Provides all necessary operations for policy management.
 */
public interface PolicyRepository {
    
    /**
     * Save a policy (create or update)
     */
    void save(Policy policy);
    
    /**
     * Find a policy by its ID
     */
    Optional<Policy> findById(PolicyId policyId);
    
    /**
     * Find all policies with pagination and optional filters
     */
    List<Policy> findAll(int page, int size, PolicyStatus status, String clientRut);
    
    /**
     * Count policies with optional filters
     */
    long count(PolicyStatus status, String clientRut);
    
    /**
     * Check if a policy exists by policy number
     */
    boolean existsByPolicyNumber(String policyNumber);
    
    /**
     * Find a policy by policy number
     */
    Optional<Policy> findByPolicyNumber(String policyNumber);
    
    /**
     * Find policies by status
     */
    List<Policy> findByStatus(PolicyStatus status);
    
    /**
     * Find policies by client RUT
     */
    List<Policy> findByClientRut(String clientRut);
    
    /**
     * Count policies by status
     */
    long countByStatus(PolicyStatus status);
    
    /**
     * Count policies by client RUT
     */
    long countByClientRut(String clientRut);
    
    /**
     * Delete a policy by ID
     */
    void deleteById(PolicyId policyId);
    
    /**
     * Delete a policy by policy number
     */
    void deleteByPolicyNumber(String policyNumber);
    
    /**
     * Check if a policy exists by ID
     */
    boolean existsById(PolicyId policyId);
}

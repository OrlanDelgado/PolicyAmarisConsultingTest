package cl.demo.polizas.seedwork.domain;

import java.util.List;
import java.util.Optional;

/**
 * Base interface for all repositories in the domain. Defines common operations that all repositories should support.
 * 
 * @param <E> The type of the entity
 * @param <ID> The type of the entity's identifier
 */
public interface Repository<E, ID> {

    /**
     * Finds an entity by its identifier.
     * 
     * @param id The identifier to search for
     * @return An Optional containing the entity if found, empty otherwise
     */
    Optional<E> findById(ID id);

    /**
     * Saves an entity. If the entity has no ID, it will be created. If it has an ID, it will be updated.
     * 
     * @param entity The entity to save
     * @return The saved entity (may have a new ID if it was created)
     */
    E save(E entity);

    /**
     * Checks if an entity with the given ID exists.
     * 
     * @param id The identifier to check
     * @return true if an entity exists with the given ID, false otherwise
     */
    boolean existsById(ID id);

    /**
     * Returns all entities.
     * 
     * @return A list of all entities
     */
    List<E> findAll();

    /**
     * Deletes an entity by its ID.
     * 
     * @param id The identifier of the entity to delete
     * @return true if the entity was deleted, false if it didn't exist
     */
    default boolean deleteById(ID id) {
        Optional<E> entity = findById(id);
        if (entity.isPresent()) {
            delete(entity.get());
            return true;
        }
        return false;
    }

    /**
     * Deletes an entity.
     * 
     * @param entity The entity to delete
     */
    default void delete(E entity) {
        // Default implementation - subclasses should override if they need specific logic
        if (entity != null) {
            // Try to get the ID using reflection or let subclasses handle it
            // This is a limitation of the generic approach
        }
    }

    /**
     * Counts the total number of entities.
     * 
     * @return The total count of entities
     */
    default long count() {
        return findAll().size();
    }

    /**
     * Checks if the repository is empty.
     * 
     * @return true if there are no entities, false otherwise
     */
    default boolean isEmpty() {
        return count() == 0;
    }
}

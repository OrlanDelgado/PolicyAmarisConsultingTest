package cl.demo.polizas.seedwork.domain;

import java.util.Objects;

/**
 * Base class for all domain entities. Provides identity-based equality and common entity behavior.
 * 
 * @param <ID> The type of the entity's identifier
 */
public abstract class Entity<ID> {

    protected ID id;

    protected Entity(ID id) {
        this.id = id;
    }

    public ID id() {
        return id;
    }

    protected void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Entity<?> that = (Entity<?>) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + "}";
    }
}

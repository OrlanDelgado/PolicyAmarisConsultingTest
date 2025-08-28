package cl.demo.polizas.seedwork.domain;

import java.util.function.Predicate;

/**
 * Specification pattern for creating complex filters and business rules. Allows combining multiple conditions in a
 * flexible and readable way.
 * 
 * @param <T> The type of the object being tested
 */
public interface Specification<T> {

    /**
     * Tests if the given object satisfies this specification.
     * 
     * @param obj The object to test
     * @return true if the object satisfies the specification, false otherwise
     */
    boolean isSatisfiedBy(T obj);

    /**
     * Creates a new specification that is satisfied when both this specification and the other specification are
     * satisfied (AND operation).
     * 
     * @param other The other specification
     * @return A new specification representing the AND of both specifications
     */
    default Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }

    /**
     * Creates a new specification that is satisfied when either this specification or the other specification is
     * satisfied (OR operation).
     * 
     * @param other The other specification
     * @return A new specification representing the OR of both specifications
     */
    default Specification<T> or(Specification<T> other) {
        return new OrSpecification<>(this, other);
    }

    /**
     * Creates a new specification that is satisfied when this specification is not satisfied (NOT operation).
     * 
     * @return A new specification representing the negation of this specification
     */
    default Specification<T> not() {
        return new NotSpecification<>(this);
    }

    /**
     * Converts this specification to a Java Predicate.
     * 
     * @return A Predicate equivalent to this specification
     */
    default Predicate<T> toPredicate() {
        return this::isSatisfiedBy;
    }

    /**
     * Creates a specification from a Java Predicate.
     * 
     * @param predicate The predicate to convert
     * @param <T> The type of the object being tested
     * @return A specification equivalent to the given predicate
     */
    static <T> Specification<T> from(Predicate<T> predicate) {
        return predicate::test;
    }

    /**
     * Always satisfied specification.
     * 
     * @param <T> The type of the object being tested
     * @return A specification that is always satisfied
     */
    static <T> Specification<T> always() {
        return obj -> true;
    }

    /**
     * Never satisfied specification.
     * 
     * @param <T> The type of the object being tested
     * @return A specification that is never satisfied
     */
    static <T> Specification<T> never() {
        return obj -> false;
    }

    /**
     * AND specification implementation.
     */
    class AndSpecification<T> implements Specification<T> {
        private final Specification<T> left;
        private final Specification<T> right;

        public AndSpecification(Specification<T> left, Specification<T> right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean isSatisfiedBy(T obj) {
            return left.isSatisfiedBy(obj) && right.isSatisfiedBy(obj);
        }
    }

    /**
     * OR specification implementation.
     */
    class OrSpecification<T> implements Specification<T> {
        private final Specification<T> left;
        private final Specification<T> right;

        public OrSpecification(Specification<T> left, Specification<T> right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean isSatisfiedBy(T obj) {
            return left.isSatisfiedBy(obj) || right.isSatisfiedBy(obj);
        }
    }

    /**
     * NOT specification implementation.
     */
    class NotSpecification<T> implements Specification<T> {
        private final Specification<T> specification;

        public NotSpecification(Specification<T> specification) {
            this.specification = specification;
        }

        @Override
        public boolean isSatisfiedBy(T obj) {
            return !specification.isSatisfiedBy(obj);
        }
    }
}

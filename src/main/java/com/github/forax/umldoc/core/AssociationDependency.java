package com.github.forax.umldoc.core;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

/**
 * An association dependency.
 *
 * @param left the left side of the association
 * @param right the right side of the association
 */
public record AssociationDependency(Side left, Side right) implements Dependency {
  public AssociationDependency {
    requireNonNull(left);
    requireNonNull(right);
  }

  /**
   * A side of the association.
   *
   * @param entity the entity the association is linked with
   * @param label an optional label
   * @param navigability the navigability
   * @param cardinality the cardinality
   */
  public record Side(Entity entity, Optional<String> label, boolean navigability,
                     Cardinality cardinality) {
    /**
     * Create a side of an association.
     *
     * @param entity the entity the association is linked with
     * @param label an optional label
     * @param navigability the navigability
     * @param cardinality the cardinality
     */
    public Side {
      requireNonNull(entity);
      requireNonNull(label);
      requireNonNull(cardinality);
    }
  }

  /**
   * Cardinality of the association.
   */
  public enum Cardinality {
    /**
     * only one instance.
     */
    ONLY_ONE,
    /**
     * zero or one instance.
     */
    ZERO_OR_ONE,
    /**
     * many instances.
     */
    MANY
  }
}

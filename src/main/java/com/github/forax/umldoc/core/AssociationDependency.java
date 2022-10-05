package com.github.forax.umldoc.core;

import static java.util.Objects.requireNonNull;

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
   * @param navigability the navigability
   * @param cardinality the cardinality
   */
  public record Side(Entity entity, boolean navigability, String cardinality) {
    /**
     * Create a side of an association.
     *
     * @param entity the entity the association is linked with
     * @param navigability the navigability
     * @param cardinality the cardinality
     */
    public Side {
      requireNonNull(entity);
      requireNonNull(cardinality);
    }
  }
}

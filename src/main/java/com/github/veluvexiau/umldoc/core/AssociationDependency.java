package com.github.veluvexiau.umldoc.core;

/**
 * An association dependency.
 *
 * @param left the left side of the association
 * @param right the right side of the association
 */
public record AssociationDependency(Side left, Side right) implements Dependency {
  /**
   * A side of the association.
   *
   * @param entity the entity
   * @param navigability the navigability
   * @param cardinality the cardinality
   */
  public record Side(Entity entity, boolean navigability, String cardinality) { }
}

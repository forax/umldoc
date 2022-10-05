package com.github.forax.umldoc.core;

import static java.util.Objects.requireNonNull;

/**
 * Subtype dependency.
 *
 * @param supertype the supertype of the dependency
 * @param subtype the subtype of the dependency
 */
public record SubtypeDependency(Entity supertype, Entity subtype) implements Dependency {
  /**
   * Creates a subtype dependency.
   *
   * @param supertype the supertype of the dependency
   * @param subtype the subtype of the dependency
   */
  public SubtypeDependency {
    requireNonNull(supertype);
    requireNonNull(subtype);
  }
}

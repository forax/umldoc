package com.github.forax.umldoc.core;

/**
 * Subtype dependency.
 *
 * @param supertype the supertype of the dependency
 * @param subtype the subtype of the dependency
 */
public record SubtypeDependency(Entity supertype, Entity subtype) implements Dependency {
}

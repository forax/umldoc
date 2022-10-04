package com.github.forax.umldoc.core;

/**
 * A dependency, can be either a {@link SubtypeDependency} or an
 * {@link AssociationDependency}.
 */
public sealed interface Dependency permits SubtypeDependency, AssociationDependency {
}

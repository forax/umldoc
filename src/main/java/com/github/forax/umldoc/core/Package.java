package com.github.forax.umldoc.core;

import java.util.List;

/**
 * A package containing the entities and the dependencies.
 *
 * @param name the package name
 * @param entities the entities declared in the package
 * @param dependencies the dependencies in between the entities
 */
public record Package(String name, List<Entity> entities, List<Dependency> dependencies) {
}

package com.github.forax.umldoc.core;

import static java.util.Objects.requireNonNull;

import java.util.Set;

/**
 * A field on an {@link Entity}.
 *
 * @param modifiers the field modifier
 * @param name the name of the field
 * @param type the type of the field
 */
public record Field(Set<Modifier> modifiers, String name, String type) {
  /**
   * Creates a field.
   *
   * @param modifiers the field modifier
   * @param name the name of the field
   * @param type the type of the field
   */
  public Field {
    modifiers = Set.copyOf(modifiers);
    requireNonNull(name);
    requireNonNull(type);
  }
}

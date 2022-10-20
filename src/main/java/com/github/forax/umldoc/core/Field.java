package com.github.forax.umldoc.core;

import static java.util.Objects.requireNonNull;

import java.util.Set;

/**
 * A field on an {@link Entity}.
 *
 * @param modifiers the field modifier
 * @param name the name of the field
 * @param typeInfo the type of the field
 */
public record Field(Set<Modifier> modifiers, String name, TypeInfo typeInfo) {
  /**
   * Creates a field.
   *
   * @param modifiers the field modifier
   * @param name the name of the field
   * @param typeInfo the type of the field
   */
  public Field {
    modifiers = Set.copyOf(modifiers);
    requireNonNull(name);
    requireNonNull(typeInfo);
  }

  /**
   * Creates a field.
   *
   * @param modifiers the field modifier
   * @param name the name of the field
   * @param type the type of the field as a string
   *
   * @deprecated use {@link #Field(Set, String, TypeInfo) instead}*
   */
  @Deprecated
  public Field(Set<Modifier> modifiers, String name, String type) {
    this(modifiers, name, TypeInfo.of(type));
  }

  /**
   * Returns the type as a string.
   *
   * @return the type as a string.
   *
   * @deprecated use {@link #typeInfo() instead}
   */
  @Deprecated
  public String type() {
    return typeInfo.name();
  }
}

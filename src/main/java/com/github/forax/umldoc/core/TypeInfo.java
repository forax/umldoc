package com.github.forax.umldoc.core;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A type with its type parameters.
 *
 * @param outer the optional outer type (for inner classes)
 * @param name the name
 * @param typeParameters the type parameters
 */
public record TypeInfo(Optional<TypeInfo> outer, String name, List<TypeInfo> typeParameters) {
  /**
   * A type with its type parameters.
   *
   * @param outer the optional outer type (for inner classes)
   * @param name the name
   * @param typeParameters the type parameters
   */
  public TypeInfo {
    requireNonNull(outer);
    requireNonNull(name);
    typeParameters = List.copyOf(typeParameters);
  }

  /**
   * Creates a type from a name.
   *
   * @param name name of the type
   * @return a type with no outer and no type parameter.
   */
  public static TypeInfo of(String name) {
    requireNonNull(name);
    return new TypeInfo(Optional.empty(), name, List.of());
  }

  /**
   * Returns a new type info with an additional type parameter.
   *
   * @param typeParameter the type parameter to add
   * @return a new type info with an additional type parameter.
   */
  public TypeInfo withTypeParameter(TypeInfo typeParameter) {
    Objects.requireNonNull(typeParameter);
    return new TypeInfo(outer, name,
        Stream.concat(typeParameters.stream(), Stream.of(typeParameter)).toList());
  }

  @Override
  public String toString() {
    return outer.map(type -> type + "$").orElse("") + name + (typeParameters.isEmpty() ? "" :
        typeParameters.stream()
            .map(TypeInfo::toString)
            .collect(joining(",", "<", ">")));
  }
}

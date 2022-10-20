package com.github.forax.umldoc.core;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;

/**
 * An entity (a class, an interface, a record, etc).
 *
 * @param modifiers the entity modifier
 * @param type the entity type
 * @param stereotype the stereotype
 * @param fields the fields of the entity
 * @param methods the methods of the entity
 */
public record Entity(Set<Modifier> modifiers, TypeInfo type,
                     Stereotype stereotype,
                     List<Field> fields, List<Method> methods) {
  /**
   * Creates an entity.
   *
   * @param modifiers the entity modifier
   * @param type the entity type
   * @param stereotype the stereotype if it exists
   * @param fields the fields of the entity
   * @param methods the methods of the entity
   */
  public Entity {
    modifiers = Set.copyOf(modifiers);
    requireNonNull(type);
    requireNonNull(stereotype);
    fields = List.copyOf(fields);
    methods = List.copyOf(methods);
  }

  /**
   * Creates an entity.
   *
   * @param modifiers the entity modifier
   * @param name the entity name as a string
   * @param stereotype the stereotype if it exists
   * @param fields the fields of the entity
   * @param methods the methods of the entity
   *
   * @deprecated use {@link #Entity(Set, TypeInfo, Stereotype, List, List) instead}
   */
  @Deprecated
  public Entity(
      Set<Modifier> modifiers,
      String name,
      Stereotype stereotype,
      List<Field> fields,
      List<Method> methods) {
    this(modifiers, TypeInfo.of(name), stereotype, fields, methods);
  }

  /**
   * Returns the entity name.
   *
   * @return the entity name
   *
   * @deprecated use {@link #type() instead}
   */
  @Deprecated
  public String name() {
    return type.name();
  }

  /**
   * Stereotype of an entity.
   */
  public enum Stereotype {
    /**
     * Stereotype of a class.
     */
    CLASS,
    /**
    * Stereotype of an interface.
    */
    INTERFACE,
    /**
    * Stereotype of an annotation.
    */
    ANNOTATION,
    /**
    * Stereotype of an enum.
    */
    ENUM,
    /**
    * Stereotype of a record.
    */
    RECORD,
    /**
    * Stereotype of an abstract class.
    */
    ABSTRACT,
  }
}

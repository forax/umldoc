package com.github.forax.umldoc.core;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;

/**
 * A method of an {@link Entity}.
 *
 * @param modifiers the method modifier
 * @param name the method name
 * @param returnTypeInfo the return type
 * @param parameters the parameters
 */
public record Method(Set<Modifier> modifiers, String name, TypeInfo returnTypeInfo,
                     List<Parameter> parameters, Call.Group callGroup) {
  /**
   * Creates a method.
   *
   * @param modifiers the method modifier
   * @param name the method name
   * @param returnTypeInfo the return type
   * @param parameters the parameters
   * @param callGroup the group method calls from the implementation
   */
  public Method {
    modifiers = Set.copyOf(modifiers);
    requireNonNull(name);
    requireNonNull(returnTypeInfo);
    parameters = List.copyOf(parameters);
    requireNonNull(callGroup);
  }

  /**
   * Creates a method.
   *
   * @param modifiers the method modifier
   * @param name the method name
   * @param returnType the return type
   * @param parameters the parameters
   *
   * @deprecated use {@link #Method(Set, String, TypeInfo, List, Call.Group) instead}
   */
  @Deprecated
  public Method(Set<Modifier> modifiers, String name, String returnType,
                List<Parameter> parameters) {
    this(modifiers, name, TypeInfo.of(returnType), parameters, Call.Group.EMPTY_GROUP);
  }

  /**
   * Returns the return type as a string.
   *
   * @return the return type as a string.
   *
   * @deprecated use {@link #returnTypeInfo() instead}
   */
  @Deprecated
  public String returnType() {
    return returnTypeInfo.name();
  }

  /**
   * A parameter of a method.
   *
   * @param name the parameter name
   * @param typeInfo the parameter type
   */
  public record Parameter(String name, TypeInfo typeInfo) {
    public Parameter {
      requireNonNull(name);
      requireNonNull(typeInfo);
    }

    /**
     * A parameter of a method.
     *
     * @param name the parameter name
     * @param type the parameter type as a string
     *
     * @deprecated use {@link #Parameter(String, TypeInfo) instead}
     */
    @Deprecated
    public Parameter(String name, String type) {
      this(name, TypeInfo.of(type));
    }

    /**
     * Returns the type as a string.
     *
     * @return the type as a string.
     *
     * @deprecated use {@link #returnTypeInfo() instead}
     */
    @Deprecated
    public String type() {
      return typeInfo.name();
    }
  }
}

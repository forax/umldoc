package com.github.forax.umldoc.core;

import java.util.List;
import java.util.Set;

/**
 * A method of an {@link Entity}.
 *
 * @param modifiers the method modifier
 * @param name the method name
 * @param returnType the return type
 * @param parameters the parameters
 */
public record Method(Set<Modifier> modifiers, String name, String returnType,
                     List<Parameter> parameters) {
  /**
   * A parameter of a method.
   *
   * @param name the parameter name
   * @param type the parameter type
   */
  public record Parameter(String name, String type) {}
}

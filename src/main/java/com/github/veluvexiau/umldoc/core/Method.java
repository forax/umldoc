package com.github.veluvexiau.umldoc.core;

import java.util.List;

/**
 * A method of an {@link Entity}.
 *
 * @param modifier the method modifier
 * @param name the method name
 * @param returnType the return type
 * @param parameters the parameters
 */
public record Method(Modifier modifier, String name, String returnType,
                     List<Parameter> parameters) {
  /**
   * A parameter of a method.
   *
   * @param name the parameter name
   * @param type the parameter type
   */
  public record Parameter(String name, String type) {}
}

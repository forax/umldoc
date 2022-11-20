package com.github.forax.umldoc.core;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;


/**
 * A method of an {@link Entity}.
 *
 * @param signature the method signature
 * @param callGroup the method call group
 */
public record Method(Signature signature, Call.Group callGroup) {

  /**
   * A method of an {@link Entity}.
   *
   * @param modifiers      the method modifier
   * @param name           the method name
   * @param returnTypeInfo the return type
   * @param parameters     the parameters
   *
   * @deprecated use {@link Method#Method(Signature, Call.Group)} instead.
   */
  @Deprecated
  public Method(Set<Modifier> modifiers, String name, TypeInfo returnTypeInfo, List<Parameter> parameters, Call.Group callGroup) {
    this(new Signature(modifiers, name, returnTypeInfo, parameters), callGroup);
  }

  /**
   * A method signature.
   *
   * @param modifiers      the method modifier
   * @param name           the method name
   * @param returnTypeInfo the return type
   * @param parameters     the parameters
   */
  public record Signature(Set<Modifier> modifiers, String name, TypeInfo returnTypeInfo,
                          List<Parameter> parameters) {

    /**
     * Creates a method signature.
     *
     * @param modifiers      the method modifier
     * @param name           the method name
     * @param returnTypeInfo the return type
     * @param parameters     the parameters
     */
    public Signature {
      requireNonNull(modifiers);
      requireNonNull(name);
      requireNonNull(returnTypeInfo);
      requireNonNull(parameters);
    }
  }

  /**
   * Creates a method.
   *
   * @param signature the method signature
   * @param callGroup the group method calls from the implementation
   */
  public Method {
    requireNonNull(signature);
    requireNonNull(callGroup);
  }

  public Call.Group relevantCallsGroup(Package p) {
    var relevantCalls = callGroup.getCallsFromPackage(p);
    return new Call.Group(callGroup.kind(), relevantCalls);
  }

  public TypeInfo returnTypeInfo() {
    return signature.returnTypeInfo();
  }

  public String name() {
    return signature.name();
  }

  public List<Parameter> parameters() {
    return signature.parameters();
  }

  public Set<Modifier> modifiers() {
    return signature.modifiers();
  }

  /**
   * Creates a method.
   *
   * @param modifiers  the method modifier
   * @param name       the method name
   * @param returnType the return type
   * @param parameters the parameters
   * @deprecated use {@link #Method(Signature, Call.Group) instead}
   */
  @Deprecated
  public Method(Set<Modifier> modifiers, String name, String returnType,
                List<Parameter> parameters) {
    this(new Signature(modifiers, name, TypeInfo.of(returnType), parameters),
            Call.Group.EMPTY_GROUP);
  }

  /**
   * Returns the return type as a string.
   *
   * @return the return type as a string.
   * @deprecated use {@link #returnTypeInfo() instead}
   */
  @Deprecated
  public String returnType() {
    return signature.returnTypeInfo.name();
  }

  /**
   * A parameter of a method.
   *
   * @param name     the parameter name
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
     * @deprecated use {@link #returnTypeInfo() instead}
     */
    @Deprecated
    public String type() {
      return typeInfo.name();
    }
  }
}

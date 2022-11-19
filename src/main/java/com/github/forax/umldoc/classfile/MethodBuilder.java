package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A builder for a {@link Method}.
 */
public class MethodBuilder {
  private Set<Modifier> modifiers;
  private String name;
  private TypeInfo returnTypeInfo;
  private List<Method.Parameter> parameters;
  private Call.Group callGroup = new Call.Group(Call.Group.Kind.NONE, new ArrayList<>());

  /**
   * Constructor of MethodBuilder.
   *
   * @param modifiers the modifiers of the method
   * @param name the name of the method
   * @param returnTypeInfo the return type of the method
   * @param parameters the list of parameters of the method
   */
  public MethodBuilder(Set<Modifier> modifiers, String name, TypeInfo returnTypeInfo,
                       List<Method.Parameter> parameters) {
    Objects.requireNonNull(modifiers);
    Objects.requireNonNull(name);
    Objects.requireNonNull(returnTypeInfo);
    Objects.requireNonNull(parameters);
    this.modifiers = modifiers;
    this.name = name;
    this.returnTypeInfo = returnTypeInfo;
    this.parameters = parameters;
  }

  /**
   * Add a call to the {@link com.github.forax.umldoc.core.Call.Group} of the method.
   *
   * @param call the call to add to the group
   * @return the instance of {@link MethodBuilder}
   */
  public MethodBuilder addCallToGroup(Call call) {
    Objects.requireNonNull(call);
    this.callGroup.calls().add(call);
    return this;
  }

  public Method build() {

    return new Method(modifiers, name, returnTypeInfo, parameters, callGroup);
  }
}

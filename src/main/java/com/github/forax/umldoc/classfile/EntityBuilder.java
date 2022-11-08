package com.github.forax.umldoc.classfile;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A builder of an entity.
 */
final class EntityBuilder {
  private TypeInfo type;
  private Entity.Stereotype stereotype;
  private final ArrayList<Field> fields = new ArrayList<>();
  private final ArrayList<Method> methods = new ArrayList<>();
  private final HashMap<Method, ArrayList<Call.MethodCall>> mappedMethodsToCall = new HashMap<>();

  public void type(TypeInfo type) {
    requireNonNull(type);
    this.type = type;
  }

  public TypeInfo type() {
    return type;
  }

  public void stereotype(Entity.Stereotype stereotype) {
    requireNonNull(stereotype);
    this.stereotype = stereotype;
  }

  public void addField(Set<Modifier> modifiers, String name, TypeInfo type) {
    requireNonNull(modifiers);
    requireNonNull(name);
    requireNonNull(type);
    fields.add(new Field(modifiers, name, type));
  }

  public void addMethod(Set<Modifier> modifiers, String name, TypeInfo returnType,
                        List<Method.Parameter> parameters, Call.Group group) {
    requireNonNull(modifiers);
    requireNonNull(name);
    requireNonNull(returnType);
    requireNonNull(parameters);
    requireNonNull(group);
    methods.add(new Method(modifiers, name, returnType, parameters, group));
  }

  public void addMethodsCallToMethod(TypeInfo type, String name, TypeInfo returnType,
                                    List<TypeInfo> parametersType) {
    requireNonNull(type);
    requireNonNull(name);
    requireNonNull(returnType);
    requireNonNull(parametersType);
    var methodCall = new Call.MethodCall(type, name, returnType, parametersType);
    var currentMethod = methods.get(methods.size() - 1);
    var methodCallList = mappedMethodsToCall.getOrDefault(currentMethod, new ArrayList<>());
    methodCallList.add(methodCall);
    mappedMethodsToCall.put(currentMethod, methodCallList);
  }

  public Entity build() {
    return new Entity(Set.of(), type, stereotype, fields, methods);
  }
}
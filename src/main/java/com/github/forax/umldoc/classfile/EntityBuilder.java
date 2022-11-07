package com.github.forax.umldoc.classfile;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.*;

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
    //methods.add(new Method(modifiers, name, returnType, parameters, group));
  }

  public void addMethodCall(Method parentMethod ){
    //var call = new Call.MethodCall()
    //methods.get(methods.size() - 1).callGroup().calls().add()
  }

  public Entity build() {
    return new Entity(Set.of(), type, stereotype, fields, List.of());
  }
}
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

  public void addMethod(Method method) {
    requireNonNull(method);
    methods.add(method);
  }

  public void addMethodsCall(Method method, TypeInfo type, String name,
                             TypeInfo returnType, List<TypeInfo> parametersType) {
    requireNonNull(method);
    requireNonNull(type);
    requireNonNull(name);
    requireNonNull(returnType);
    requireNonNull(parametersType);
    var methodCall = new Call.MethodCall(type, name, returnType, parametersType);
    var methodCallList = mappedMethodsToCall.computeIfAbsent(method, m -> new ArrayList<>());
    methodCallList.add(methodCall);
    int i = 0;

    if (i < 0) {
      i++;
      while (true) {

      }
    }

    while (i < 5) {
      i++;
      test();
      test2();
    }
    if (test3() != test4()) {
      test();
    }

    while (test3() != test4()) {
      test();
    }

    if (i > 1) {
      test();
    } else if (i > 2) {
      test2();
    } else if (i > 3) {
      test2();
    } else if (i > 2) {
      test2();
    } else {
      test2();
    }

    for (i = 5; i < 10; i++) {
      test();
      test2();
    }

    test();
    test2();
  }

  private void test() {

  }

  private void test2() {

  }

  private boolean test3() {
    return true;
  }

  private boolean test4() {
    return false;
  }

  public Entity build() {
    return new Entity(Set.of(), type, stereotype, fields, methods);
  }
}
package com.github.magickoders.jar;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class EntityBuilder {

  private final ArrayList<Method> methods = new ArrayList<>();
  private final ArrayList<Field> fields = new ArrayList<>();
  private TypeInfo type = null;
  private Set<Modifier> modifiers = null;
  private Entity.Stereotype stereotype = null;

  public EntityBuilder addMethod(Method m) {
    Objects.requireNonNull(m);
    this.methods.add(m);
    return this;
  }

  public EntityBuilder addField(Field f) {
    Objects.requireNonNull(f);
    this.fields.add(f);
    return this;
  }

  public EntityBuilder setModifiers(Set<Modifier> modifiers) {
    this.modifiers = Set.copyOf(modifiers);
    return this;
  }

  public EntityBuilder setTypeInfo(TypeInfo type) {
    Objects.requireNonNull(type);
    this.type = type;
    return this;
  }

  public EntityBuilder setStereotype(Entity.Stereotype stereotype) {
    Objects.requireNonNull(stereotype);
    this.stereotype = stereotype;
    return this;
  }

  private void clear() {
    modifiers = null;
    type = null;
    stereotype = null;
    methods.clear();
    fields.clear();
  }

  public Entity build() {
    if (modifiers == null) {
      throw new IllegalStateException("No modifiers was set");
    }

    if (type == null) {
      throw new IllegalStateException("No type was set");
    }

    if (stereotype == null) {
      throw new IllegalStateException("No stereotype was set");
    }

    var entity = new Entity(modifiers, type, stereotype, fields, methods);
    clear();
    return entity;
  }

}

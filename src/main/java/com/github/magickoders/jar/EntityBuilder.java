package com.github.magickoders.jar;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

/**
 * Builder for com.github.forax.umldoc.core.Entity. It helps build an entity when you need to add
 * fields and methods multiple times on the same entity.
 */
public class EntityBuilder {

  private final ArrayList<Method> methods = new ArrayList<>();
  private final ArrayList<Field> fields = new ArrayList<>();
  private TypeInfo type = null;
  private Set<Modifier> modifiers = null;
  private Entity.Stereotype stereotype = null;

  /**
   * Adds a method to the entity to build.
   *
   * @param m
   *         method to add on the buildable entity
   * @return a reference to this object
   */
  public EntityBuilder addMethod(Method m) {
    Objects.requireNonNull(m);
    this.methods.add(m);
    return this;
  }

  /**
   * Adds a field to the entity to build.
   *
   * @param f
   *         field to add on the buildable entity
   * @return a reference to this object
   */
  public EntityBuilder addField(Field f) {
    Objects.requireNonNull(f);
    this.fields.add(f);
    return this;
  }

  /**
   * Sets all modifiers of the entity to build. This value is required to build the entity.
   *
   * @param modifiers
   *         a set of Modifier
   * @return a reference to this object
   */
  public EntityBuilder setModifiers(Set<Modifier> modifiers) {
    this.modifiers = Set.copyOf(modifiers);
    return this;
  }

  /**
   * Sets the type of the entity to build. This value is required to build the entity.
   *
   * @param type
   *         a TypeInfo representing the "class" type
   * @return a reference to this object
   */
  public EntityBuilder setTypeInfo(TypeInfo type) {
    Objects.requireNonNull(type);
    this.type = type;
    return this;
  }

  /**
   * Sets the stereotype of the entity to build. This value is required to build the entity.
   *
   * @param stereotype
   *         an Entity.Stereotype representing the type of the class (e.g. enum, record, ...)
   * @return a reference to this object
   */
  public EntityBuilder setStereotype(Entity.Stereotype stereotype) {
    Objects.requireNonNull(stereotype);
    this.stereotype = stereotype;
    return this;
  }

  /**
   * Builds the entity with the previously set values.
   *
   * @return the built entity.
   * @throws NullPointerException
   *         if a required value was not set
   * @see #setModifiers(Set)
   * @see #setTypeInfo(TypeInfo)
   * @see #setStereotype(Entity.Stereotype)
   * @see #addField(Field)
   * @see #addMethod(Method)
   */
  public Entity build() {
    Objects.requireNonNull(modifiers, "No value set for modifiers");
    Objects.requireNonNull(type, "No value set for type");
    Objects.requireNonNull(stereotype, "No value set for stereotype");

    return new Entity(modifiers, type, stereotype, fields, methods);
  }

}

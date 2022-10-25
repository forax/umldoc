package com.github.forax.umldoc.core;

import com.github.forax.umldoc.core.AssociationDependency.Cardinality;
import com.github.forax.umldoc.core.AssociationDependency.Side;
import com.github.forax.umldoc.core.Entity.Stereotype;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {
  @Test
  public void modifiers() {
    assertAll(
        () -> assertEquals("PRIVATE", Modifier.PRIVATE.name()),
        () -> assertEquals("PROTECTED", Modifier.PROTECTED.name()),
        () -> assertEquals("PUBLIC", Modifier.PUBLIC.name()),
        () -> assertEquals("STATIC", Modifier.STATIC.name()),
        () -> assertEquals("FINAL", Modifier.FINAL.name())
    );
  }

  @Test
  public void field() {
    var field = new Field(Set.of(Modifier.PRIVATE), "age", TypeInfo.of("int"));
    assertAll(
        () -> assertEquals(Set.of(Modifier.PRIVATE), field.modifiers()),
        () -> assertEquals("age", field.name()),
        () -> assertEquals(TypeInfo.of("int"), field.typeInfo())
    );
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> new Field(null, "age", TypeInfo.of("int"))),
        () -> assertThrows(NullPointerException.class, () -> new Field(Set.of(), null, TypeInfo.of("int"))),
        () -> assertThrows(NullPointerException.class, () -> new Field(Set.of(), "age", (TypeInfo) null))
    );
  }

  @Test
  public void methodParameter() {
    var parameter = new Method.Parameter("navigability", TypeInfo.of("boolean"));
    assertAll(
        () -> assertEquals("navigability", parameter.name()),
        () -> assertEquals(TypeInfo.of("boolean"), parameter.typeInfo())
    );
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> new Method.Parameter(null, TypeInfo.of("boolean"))),
        () -> assertThrows(NullPointerException.class, () -> new Method.Parameter("navigability", (TypeInfo) null))
    );
  }

  @Test
  public void method() {
    var method = new Method(Set.of(Modifier.PUBLIC), "println", TypeInfo.of("void"),
        List.of(new Method.Parameter("object", TypeInfo.of("java.lang.Object"))));
    assertAll(
        () -> assertEquals(Set.of(Modifier.PUBLIC), method.modifiers()),
        () -> assertEquals("println", method.name()),
        () -> assertEquals(TypeInfo.of("void"), method.returnTypeInfo()),
        () -> assertEquals(List.of(new Method.Parameter("object", TypeInfo.of("java.lang.Object"))), method.parameters())
    );
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> new Method(null, "toString", TypeInfo.of("java.lang.String"), List.of())),
        () -> assertThrows(NullPointerException.class, () -> new Method(Set.of(), null, TypeInfo.of("java.lang.String"), List.of())),
        () -> assertThrows(NullPointerException.class, () -> new Method(Set.of(), "toString", (TypeInfo) null, List.of())),
        () -> assertThrows(NullPointerException.class, () -> new Method(Set.of(), "toString", TypeInfo.of("java.lang.String"), null))
    );
  }

  @Test
  public void entity() {
    var nameField = new Field(Set.of(Modifier.PUBLIC), "name", TypeInfo.of("java.lang.String"));
    var methodToString = new Method(Set.of(Modifier.PUBLIC), "toString", TypeInfo.of("java.lang.String"), List.of());
    var entity = new Entity(Set.of(Modifier.PUBLIC), TypeInfo.of("Entity"), Stereotype.CLASS,
        List.of(nameField), List.of(methodToString));
    assertAll(
        () -> assertEquals(Set.of(Modifier.PUBLIC), entity.modifiers()),
        () -> assertEquals(TypeInfo.of("Entity"), entity.type()),
        () -> assertEquals(Stereotype.CLASS, entity.stereotype()),
        () -> assertEquals(List.of(nameField), entity.fields()),
        () -> assertEquals(List.of(methodToString), entity.methods())
    );
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> new Entity(null, TypeInfo.of(""), Stereotype.CLASS, List.of(), List.of())),
        () -> assertThrows(NullPointerException.class, () -> new Entity(Set.of(), (TypeInfo) null, Stereotype.CLASS, List.of(), List.of())),
        () -> assertThrows(NullPointerException.class, () -> new Entity(Set.of(), TypeInfo.of(""), null, List.of(), List.of())),
        () -> assertThrows(NullPointerException.class, () -> new Entity(Set.of(), TypeInfo.of(""), Stereotype.CLASS, null, List.of())),
        () -> assertThrows(NullPointerException.class, () -> new Entity(Set.of(), TypeInfo.of(""), Stereotype.CLASS, List.of(), null))
    );
  }

  @Test
  public void typeInfo() {
    var intTypeInfo = new TypeInfo(Optional.empty(), "int", List.of());
    var stringTypeInfo = new TypeInfo(Optional.empty(), "java.lang.String", List.of());
    var listOfStringTypeInfo = new TypeInfo(Optional.empty(), "java.util.List", List.of())
        .withTypeParameter(stringTypeInfo);
    var mapEntryTypeInfo = new TypeInfo(Optional.of(TypeInfo.of("java.util.Map")), "Entry", List.of(TypeInfo.of("K"), TypeInfo.of("V")));
    assertAll(
        () -> assertTrue(intTypeInfo.outer().isEmpty()),
        () -> assertEquals("int", intTypeInfo.name()),
        () -> assertEquals(List.of(), intTypeInfo.typeParameters()),
        () -> assertEquals("int", intTypeInfo.toString()),
        () -> assertTrue(stringTypeInfo.outer().isEmpty()),
        () -> assertEquals("java.lang.String", stringTypeInfo.name()),
        () -> assertEquals(List.of(), stringTypeInfo.typeParameters()),
        () -> assertEquals("java.lang.String", stringTypeInfo.toString()),
        () -> assertTrue(listOfStringTypeInfo.outer().isEmpty()),
        () -> assertEquals("java.util.List", listOfStringTypeInfo.name()),
        () -> assertEquals(List.of(stringTypeInfo), listOfStringTypeInfo.typeParameters()),
        () -> assertEquals("java.util.List<java.lang.String>", listOfStringTypeInfo.toString()),
        () -> assertEquals(TypeInfo.of("java.util.Map"), mapEntryTypeInfo.outer().orElseThrow()),
        () -> assertEquals("Entry", mapEntryTypeInfo.name()),
        () -> assertEquals(List.of(TypeInfo.of("K"), TypeInfo.of("V")), mapEntryTypeInfo.typeParameters()),
        () -> assertEquals("java.util.Map$Entry<K,V>", mapEntryTypeInfo.toString())
    );

    assertAll(
        () -> assertThrows(NullPointerException.class, () -> new TypeInfo(null, "int", List.of())),
        () -> assertThrows(NullPointerException.class, () -> new TypeInfo(Optional.empty(), null, List.of())),
        () -> assertThrows(NullPointerException.class, () -> new TypeInfo(Optional.empty(), "int", null)),
        () -> assertThrows(NullPointerException.class, () -> new TypeInfo(Optional.empty(), "int", List.of()).withTypeParameter(null))
    );
  }

  @Test
  public void association() {
    var entity = new Entity(Set.of(Modifier.PUBLIC), TypeInfo.of("Entity"), Stereotype.CLASS, List.of(), List.of());
    var stereotype = new Entity(Set.of(Modifier.PACKAGE), TypeInfo.of("Stereotype"), Stereotype.ENUM, List.of(), List.of());
    var association = new AssociationDependency(
        new Side(entity, Optional.empty(), false, Cardinality.ONLY_ONE),
        new Side(stereotype, Optional.of("stereotype"), true, Cardinality.ONLY_ONE)
    );
    assertAll(
        () -> assertEquals(entity, association.left().entity()),
        () -> assertFalse(association.left().navigability()),
        () -> assertEquals(Cardinality.ONLY_ONE, association.left().cardinality()),
        () -> assertEquals(stereotype, association.right().entity()),
        () -> assertTrue(association.right().navigability()),
        () -> assertEquals(Cardinality.ONLY_ONE, association.right().cardinality())
    );
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> new AssociationDependency(
            null,
            new Side(entity, Optional.of("label"), true, Cardinality.ONLY_ONE))),
        () -> assertThrows(NullPointerException.class, () -> new AssociationDependency(
            new Side(entity, Optional.of("label"), true, Cardinality.ONLY_ONE),
            null)),
        () -> assertThrows(NullPointerException.class, () -> new Side(null, Optional.of("label"), true, Cardinality.ONLY_ONE)),
        () -> assertThrows(NullPointerException.class, () -> new Side(entity, null, true, Cardinality.ONLY_ONE)),
        () -> assertThrows(NullPointerException.class, () -> new Side(entity, Optional.of("label"), true, null))
    );
  }

  @Test
  public void subtype() {
    var dependency = new Entity(Set.of(Modifier.PUBLIC), TypeInfo.of("Dependency"), Stereotype.INTERFACE, List.of(), List.of());
    var association = new Entity(Set.of(Modifier.PACKAGE), TypeInfo.of("AssociationDependency"), Stereotype.CLASS, List.of(), List.of());
    var subtypeDependency = new SubtypeDependency(dependency, association);
    assertAll(
        () -> assertEquals(dependency, subtypeDependency.supertype()),
        () -> assertEquals(association, subtypeDependency.subtype())
    );
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> new SubtypeDependency(dependency, null)),
        () -> assertThrows(NullPointerException.class, () -> new SubtypeDependency(null, association))
    );
  }
}
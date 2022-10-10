package com.github.forax.umldoc.core;

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
        () -> assertEquals("PACKAGE", Modifier.PACKAGE.name()),
        () -> assertEquals("PROTECTED", Modifier.PROTECTED.name()),
        () -> assertEquals("PUBLIC", Modifier.PUBLIC.name()),
        () -> assertEquals("STATIC", Modifier.STATIC.name()),
        () -> assertEquals("SEALED", Modifier.SEALED.name())
    );
  }

  @Test
  public void field() {
    var field = new Field(Set.of(Modifier.PRIVATE), "age", "int");
    assertAll(
        () -> assertEquals(Set.of(Modifier.PRIVATE), field.modifiers()),
        () -> assertEquals("age", field.name()),
        () -> assertEquals("int", field.type())
    );
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> new Field(null, "age", "int")),
        () -> assertThrows(NullPointerException.class, () -> new Field(Set.of(), null, "int")),
        () -> assertThrows(NullPointerException.class, () -> new Field(Set.of(), "age", null))
    );
  }

  @Test
  public void methodParameter() {
    var parameter = new Method.Parameter("navigability", "boolean");
    assertAll(
        () -> assertEquals("navigability", parameter.name()),
        () -> assertEquals("boolean", parameter.type())
    );
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> new Method.Parameter(null, "boolean")),
        () -> assertThrows(NullPointerException.class, () -> new Method.Parameter("navigability", null))
    );
  }

  @Test
  public void method() {
    var method = new Method(Set.of(Modifier.PUBLIC), "println", "void",
        List.of(new Method.Parameter("object", "java.lang.Object")));
    assertAll(
        () -> assertEquals(Set.of(Modifier.PUBLIC), method.modifiers()),
        () -> assertEquals("println", method.name()),
        () -> assertEquals("void", method.returnType()),
        () -> assertEquals(List.of(new Method.Parameter("object", "java.lang.Object")), method.parameters())
    );
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> new Method(null, "toString", "java.lang.String", List.of())),
        () -> assertThrows(NullPointerException.class, () -> new Method(Set.of(), null, "java.lang.String", List.of())),
        () -> assertThrows(NullPointerException.class, () -> new Method(Set.of(), "toString", null, List.of())),
        () -> assertThrows(NullPointerException.class, () -> new Method(Set.of(), "toString", "java.lang.String", null))
    );
  }

  @Test
  public void entity() {
    var nameField = new Field(Set.of(Modifier.PUBLIC), "name", "java.lang.String");
    var methodToString = new Method(Set.of(Modifier.PUBLIC), "toString", "java.lang.String", List.of());
    var entity = new Entity(Set.of(Modifier.PUBLIC), "Entity", Optional.empty(),
        List.of(nameField), List.of(methodToString));
    assertAll(
        () -> assertEquals(Set.of(Modifier.PUBLIC), entity.modifiers()),
        () -> assertEquals("Entity", entity.name()),
        () -> assertTrue(entity.stereotype().isEmpty()),
        () -> assertEquals(List.of(nameField), entity.fields()),
        () -> assertEquals(List.of(methodToString), entity.methods())
    );
  }


}
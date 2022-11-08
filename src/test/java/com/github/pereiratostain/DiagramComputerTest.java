package com.github.pereiratostain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class DiagramComputerTest {

  @Nested
  class AssociationsTest {

    private static final String TEST_ENTITY_NAME = "TestEntity";
    private static Set<Modifier> getPrivateFinalModifiers() {
      return Set.of(Modifier.PRIVATE, Modifier.FINAL);
    }

    private static Entity getTestEntityWith(List<Field> fields) {
      return new Entity(
              getPrivateFinalModifiers(),
              TypeInfo.of(TEST_ENTITY_NAME),
              Entity.Stereotype.CLASS,
              fields,
              List.of());
    }

    @Test
    void diagramBuilderPreConditions() {
      assertThrows(NullPointerException.class, () -> new DiagramComputer(null));
    }

    @Test
    void emptyEntityListReturnsEmptyList() {
      assertEquals(List.of(), new DiagramComputer(new ArrayList<>()).buildAssociations());
    }

    @Test
    void javaTypesOnlyReturnsEmptyList() {
      var fields = new ArrayList<Field>();
      var privateFinal = getPrivateFinalModifiers();
      fields.add(new Field(privateFinal, "a", TypeInfo.of("int")));
      fields.add(new Field(privateFinal, "testString", TypeInfo.of("String")));
      fields.add(new Field(privateFinal, "testList",
              new TypeInfo(Optional.empty(), "List", List.of(TypeInfo.of("String")))));
      fields.add(new Field(privateFinal, "testMap",
              new TypeInfo(Optional.empty(), "Map", List.of(TypeInfo.of("String"),
                      new TypeInfo(Optional.empty(), "List", List.of(TypeInfo.of("Boolean")))))));

      var entity = getTestEntityWith(fields);

      assertEquals(List.of(), new DiagramComputer(List.of(entity)).buildAssociations());
    }

    @Test
    void knownEntityTypeReturnsOneOneAssociation() {
      var field = new Field(getPrivateFinalModifiers(), "a", TypeInfo.of("TestEntity"));
      var entity = getTestEntityWith(List.of(field));
      var association = new AssociationDependency(
              new AssociationDependency.Side(entity,
                      Optional.empty(), false, AssociationDependency.Cardinality.ONLY_ONE),
              new AssociationDependency.Side(entity,
                      Optional.empty(), true, AssociationDependency.Cardinality.ONLY_ONE));

      assertEquals(List.of(association), new DiagramComputer(List.of(entity)).buildAssociations());
    }

    @Test
    void optionalOfKnownEntityTypeReturnsZeroOneAssociation() {
      var field = new Field(getPrivateFinalModifiers(), "a",
              new TypeInfo(Optional.of(TypeInfo.of("java.util.Optional")), TEST_ENTITY_NAME, List.of()));
      var entity = getTestEntityWith(List.of(field));
      var association = new AssociationDependency(
              new AssociationDependency.Side(entity,
                      Optional.empty(), false, AssociationDependency.Cardinality.ZERO_OR_ONE),
              new AssociationDependency.Side(entity,
                      Optional.empty(), true, AssociationDependency.Cardinality.ZERO_OR_ONE));

      assertEquals(List.of(association), new DiagramComputer(List.of(entity)).buildAssociations());
    }

    @Test
    void collectionsOfKnownEntityTypeReturnsManyAssociation() {
      var privateFinal = getPrivateFinalModifiers();
      var fields = new ArrayList<Field>();
      var typeList = new TypeInfo(Optional.empty(), "java.util.List", List.of());
      var typeMap = new TypeInfo(Optional.empty(), "java.util.Map", List.of());
      var typeSet = new TypeInfo(Optional.empty(), "java.util.Set", List.of());
      fields.add(new Field(privateFinal, "list", typeList.withTypeParameter(
              new TypeInfo(Optional.of(typeList), TEST_ENTITY_NAME, List.of()))));
      fields.add(new Field(privateFinal, "map", typeMap.withTypeParameter(
              new TypeInfo(Optional.of(typeMap), TEST_ENTITY_NAME, List.of()))));
      fields.add(new Field(privateFinal, "set", typeSet.withTypeParameter(
              new TypeInfo(Optional.of(typeSet), TEST_ENTITY_NAME, List.of()))));

      var entity = getTestEntityWith(fields);
      var association = new AssociationDependency(
              new AssociationDependency.Side(entity,
                      Optional.empty(), false, AssociationDependency.Cardinality.MANY),
              new AssociationDependency.Side(entity,
                      Optional.empty(), true, AssociationDependency.Cardinality.MANY));

      assertEquals(List.of(association, association, association), new DiagramComputer(List.of(entity)).buildAssociations());
    }
  }
}

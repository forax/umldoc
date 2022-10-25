package com.github.donnebelin.umldoc.classdiagram;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This class handles the associations and the fields.
 * It built the class diagram
 */
public class DiagramFormater {
  private final List<Entity> entities;


  /**
   * Create a new Diagram formatter from a list of entities.
   *
   * @param entities the list of entities of the class diagram.
   */
  public DiagramFormater(List<Entity> entities) {
    Objects.requireNonNull(entities);
    this.entities = entities;

  }

  private boolean entityNameExist(String entityName) {
    return entities.stream().anyMatch(entity ->
            getEntityNameWithoutPackage(entity.type().name())
                    .equals(getEntityNameWithoutPackage(entityName)));
  }

  private Optional<Entity> getEntityByName(String name) {
    return entities.stream().filter(entity ->
            getEntityNameWithoutPackage(entity.type().name())
                    .equals(name)).findFirst();
  }

  /**
   * Supply a list with computed dependencies between all entities of the same package.
   *
   * @return A list of AssociationDependency instances.
   */
  public List<AssociationDependency> createAssociationDependencies() {
    var associationList = new ArrayList<AssociationDependency>();
    for (var entity : entities) {
      for (var field : entity.fields()) {
        var newAssociation = createAssociation(field, entity);
        newAssociation.ifPresent(associationList::add);
      }
    }
    
    return associationList.stream()
        .filter(associationDependency ->
                !associationDependency.left().entity().type().name()
                        .equals(associationDependency.right().entity().type().name())
        )
        .toList();
  }

  private Optional<AssociationDependency> createAssociation(Field field, Entity entityLeft) {
    var fieldTypeInfo = parseEntityFieldToType(entityLeft, field);
    if (fieldTypeInfo.isPresent()) {
      var cardinality = getCardinality();
      var left = new AssociationDependency.Side(entityLeft, Optional.empty(), false, cardinality);
      var rightEntity = getEntityByName(getEntityNameWithoutPackage(fieldTypeInfo.get().name()));
      if (rightEntity.isPresent()) {
        var right = new AssociationDependency.Side(
                rightEntity.get(), Optional.empty(), true, cardinality
        );
        return Optional.of(new AssociationDependency(left, right));
      }
    }
    return Optional.empty();
  }

  private boolean entityAsSamePackage(String entityName1, String entityName2) {
    var entity1 = getEntityByName(getEntityNameWithoutPackage(entityName1));
    var entity2 = getEntityByName(getEntityNameWithoutPackage(entityName2));
    if (entity1.isEmpty() || entity2.isEmpty()) {
      return false;
    }
    return getPackageName(entity1.get()).equals(getPackageName(entity2.get()));
  }

  private static String getPackageName(Entity entity) {
    var packageWithName = entity.type().name();
    return packageWithName.substring(0, packageWithName.lastIndexOf("/"));
  }

  /**
   * Get the name of the entity without the package name.
   *
   * @param entityNameWithPackage entity name with the package name
   * @return The name of the entity without package name.
   */
  public static String getEntityNameWithoutPackage(String entityNameWithPackage) {
    var index = entityNameWithPackage.lastIndexOf("/");
    if (index < 0) {
      //Already no package then return them
      return entityNameWithPackage;
    }
    return entityNameWithPackage.substring(index + 1);
  }

  private Optional<TypeInfo> parseEntityFieldToType(Entity left, Field field) {
    var type = field.typeInfo();
    if (entityAsSamePackage(left.type().name(), type.name())) {
      if (entityNameExist(getEntityNameWithoutPackage(type.name()))) {
        return Optional.of(type);
      }
    }
    return Optional.empty();
  }

  private AssociationDependency.Cardinality getCardinality() {
    return AssociationDependency.Cardinality.MANY;
  }

}

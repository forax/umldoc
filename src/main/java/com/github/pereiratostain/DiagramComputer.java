package com.github.pereiratostain;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that, from a list of Entity, build the associated class diagram.
 */
public class DiagramComputer {

  private final List<Entity> entities;

  private final Set<String> entitiesName;

  /**
   * Create a new DiagramComputer for the given entities.
   *
   * @param entities The entities on which
   */
  public DiagramComputer(List<Entity> entities) {
    Objects.requireNonNull(entities);
    this.entities = List.copyOf(entities);
    this.entitiesName = getEntitiesName(entities);
  }

  private static Set<String>  getEntitiesName(List<Entity> entities) {
    return entities.stream()
            .map(entity -> entity.type().name())
            .collect(Collectors.toSet());
  }

  /**
   * Compute the associations between the entities.
   *
   * @return The computed list of associations
   */
  public List<AssociationDependency> buildAssociations() {

    var associations = new ArrayList<AssociationDependency>();

    for (var entity : entities) {
      for (var field : entity.fields()) {
        var typeInfoOptional = getEntityTypeInfo(field.typeInfo());
        if (typeInfoOptional.isEmpty()) {
          continue;
        }
        var typeInfo = typeInfoOptional.get();
        var cardinality = getCardinalityFrom(typeInfo);
        var left = new AssociationDependency.Side(entity, Optional.empty(),
                false, cardinality);
        var right = new AssociationDependency.Side(getEntityByName(typeInfo.name()),
                Optional.empty(), true, cardinality);
        associations.add(new AssociationDependency(left, right));
      }
    }
    return associations;
  }

  private Optional<TypeInfo> getEntityTypeInfo(TypeInfo typeInfo) {
    if (entitiesName.contains(typeInfo.name())) {
      return Optional.of(typeInfo);
    }
    for (var subTypeInfo : typeInfo.typeParameters()) {
      var subType = getEntityTypeInfo(subTypeInfo);
      if (subType.isPresent()) {
        return subType;
      }
    }
    return Optional.empty();
  }

  private AssociationDependency.Cardinality getCardinalityFrom(TypeInfo typeInfo) {
    var outer = typeInfo.outer();
    if (outer.isPresent()) {
      var outerType = outer.get();
      var typeName = outerType.name();
      if (typeName.equals("java.util.Optional")) {
        return AssociationDependency.Cardinality.ZERO_OR_ONE;
      }
      return AssociationDependency.Cardinality.MANY;
    }
    return AssociationDependency.Cardinality.ONLY_ONE;
  }

  private Entity getEntityByName(String name) {
    for (var entity : entities) {
      if (entity.type().name().equals(name)) {
        return entity;
      }
    }
    throw new AssertionError();
  }
}

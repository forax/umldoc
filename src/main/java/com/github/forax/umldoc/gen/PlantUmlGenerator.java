package com.github.forax.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.AssociationDependency.Cardinality;
import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Generate a class diagram using the plantuml format.
 */
public final class PlantUmlGenerator implements Generator {
  private static String cardinality(Cardinality cardinality) {
    return switch (cardinality) {
      case ONLY_ONE -> "1";
      case ZERO_OR_ONE -> "0..1";
      case MANY -> "*";
    };
  }

  private static void generateDependencies(List<Dependency> dependencies, Writer writer)
      throws IOException {
    for (var dependency : dependencies) {
      if (dependency instanceof AssociationDependency association) {
        writer.append(
            """
            %s --> "%s" %s : %s
            """.formatted(
                association.left().entity().type().name(),
                cardinality(association.right().cardinality()),
                association.right().entity().type().name(),
                association.right().label().orElse("")));
        continue;
      }
      //if (dependency instanceof SubtypeDependency subtype) {
      //   // TODO
      // continue;
      //}
      throw new AssertionError("unknown dependency");
    }
  }

  private static String entityType(Entity.Stereotype stereotype) {
    return switch (stereotype) {
      case ANNOTATION, INTERFACE -> "interface";
      case ABSTRACT -> "abstract";
      case CLASS, RECORD -> "class";
      case ENUM -> "enum";
    };
  }

  private static void generateEntities(List<Entity> entities, Writer writer)
      throws IOException {
    for (var entity : entities) {
      var stereotype = entity.stereotype();
      var entityType = entityType(stereotype);
      writer.append("""
          %s %s {
          """.formatted(entityType, entity.type()));

      if (stereotype == Entity.Stereotype.ENUM) {
        generateEnumConstants(entity.fields(), writer);
      } else {
        generateFields(entity.fields(), writer);
      }

      writer.append("""
          }

          """);
    }
  }

  private static void generateFields(List<Field> fields, Writer writer)
      throws IOException {
    for (var field : fields) {
      writer.append("""
            %s %s
          """.formatted(field.typeInfo(), field.name()));
    }
  }

  private static void generateEnumConstants(List<Field> fields, Writer writer)
      throws IOException {
    for (var field : fields) {
      writer.append("""
            %s
          """.formatted(field.name()));
    }
  }

  @Override
  public void generate(boolean header, List<Entity> entities, List<Dependency> dependencies,
                       Writer writer) throws IOException {
    requireNonNull(entities);
    requireNonNull(dependencies);
    requireNonNull(writer);
    if (header) {
      writer.append("""
          @startuml
          
          """);
    }

    generateEntities(entities, writer);
    generateDependencies(dependencies, writer);

    if (header) {
      writer.append("""
          @enduml
          """);
    }
  }
}

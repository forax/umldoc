package com.github.forax.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.AssociationDependency.Cardinality;
import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Entity.Stereotype;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.TypeInfo;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Locale;

/**
 * Generate a class diagram using the mermaid format.
 */
public final class MermaidGenerator implements Generator {

  private static String shortName(String name) {
    var index = name.lastIndexOf('.');
    var localName = index == -1 ? name : name.substring(index + 1);
    return localName.replaceAll("[^\\p{Alnum}]", "_");
  }

  private static String shortName(TypeInfo typeInfo) {
    return shortName(typeInfo.name());
  }

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
        writer.append("""
            %s --> "%s" %s : %s
            """.formatted(
                shortName(association.left().entity().type()),
                cardinality(association.right().cardinality()),
                shortName(association.right().entity().type()),
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

  private static String toStereotypeText(Stereotype stereotype) {
    return switch (stereotype) {
      case ABSTRACT, ANNOTATION, INTERFACE, RECORD ->
          "<<" + stereotype.name().toLowerCase(Locale.ROOT) + ">>";
      case ENUM -> "<<enumeration>>";
      case CLASS -> throw new AssertionError();
    };
  }

  private static void generateEntities(List<Entity> entities, Writer writer)
      throws IOException {
    for (var entity : entities) {
      writer.append("""
          class %s {
          """.formatted(shortName(entity.type())));

      var stereotype = entity.stereotype();
      if (stereotype != Stereotype.CLASS) {
        writer.append("""
              %s
            """.formatted(toStereotypeText(stereotype)));
      }

      if (entity.stereotype() == Stereotype.ENUM) {
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
          classDiagram
          direction TB
          
          """);
    }

    generateEntities(entities, writer);
    generateDependencies(dependencies, writer);
  }
}

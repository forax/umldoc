package com.github.forax.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.AssociationDependency.Cardinality;
import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.TypeInfo;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Generate a class diagram using the plantuml format.
 */
public final class PlantUmlGenerator implements Generator {
  private TypeInfo currentEntity;

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

  private static void addHeader(Writer writer) throws IOException {
    writer.append("""
          @startuml
          
          """);
  }

  private static void addFooter(Writer writer) throws IOException {
    writer.append("""
          @enduml
          """);
  }

  private static String groupKind(Call.Group.Kind groupKind) {
    return switch (groupKind) {
      case LOOP -> "loop";
      case OPTIONAL -> "opt";
      case ALTERNATE -> "alt";
      case NONE, PARALLEL -> "group";
    };
  }

  private void generateMethodCall(Call.MethodCall methodCall, Writer writer)
          throws IOException {

    writer.append(currentEntity.name())
            .append(" -> ")
            .append(methodCall.type().name())
            .append(": ")
            .append(methodCall.name())
            .append("()")
            .append('\n');

    // Alice -> Bob: method name
    // activate Bob

    // Bob -> Alice: method name
    // deactivate Bob
  }

  private void generateCalls(Call.Group group, Writer writer) throws IOException {
    if (group.equals(Call.Group.EMPTY_GROUP)) {
      return;
    }

    if (!group.kind().equals(Call.Group.Kind.NONE)) {
      writer.append(groupKind(group.kind())).append('\n');
    }

    for (var call : group.calls()) {
      if (call instanceof Call.MethodCall methodCall) {
        generateMethodCall(methodCall, writer);
        currentEntity = methodCall.type();
      } else if (call instanceof Call.Group groupCall) {
        generateCalls(groupCall, writer);
      } else {
        throw new IllegalStateException();
      }
    }

    if (!group.kind().equals(Call.Group.Kind.NONE)) {
      writer.append('\n').append("end");
    }

    //    writer.append("""
    //            %s
    //                %s
    //            end
    //            """.formatted(groupKind(group.kind()), generateMethodCall(methodCall)));
  }

  @Override
  public void generate(boolean header, List<Entity> entities, List<Dependency> dependencies,
                                   Writer writer) throws IOException {
    requireNonNull(entities);
    requireNonNull(dependencies);
    requireNonNull(writer);

    if (header) {
      addHeader(writer);
    }

    generateEntities(entities, writer);
    generateDependencies(dependencies, writer);

    if (header) {
      addFooter(writer);
    }
  }

  @Override
  public void generateSequenceDiagram(boolean header, Entity entryEntity, Method entryPoint,
                                      Writer writer) throws IOException {
    requireNonNull(entryEntity);
    requireNonNull(entryPoint);
    requireNonNull(writer);

    if (!entryEntity.methods().contains(entryPoint)) {
      throw new IllegalStateException();
    }

    if (header) {
      addHeader(writer);
    }
    currentEntity = entryEntity.type();
    generateCalls(entryPoint.callGroup(), writer);

    if (header) {
      addFooter(writer);
    }
  }
}

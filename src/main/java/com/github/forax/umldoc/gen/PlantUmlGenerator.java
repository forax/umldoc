package com.github.forax.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.AssociationDependency.Cardinality;
import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.core.SubtypeDependency;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
      if (dependency instanceof SubtypeDependency subtype) {
        writer.append("""
             %s --|> %s
             """.formatted(
                subtype.subtype().type().name(),
                subtype.supertype().type().name()));
        continue;
      }
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
      case LOOP -> "loop\n";
      case OPTIONAL -> "opt\n";
      case ALTERNATE -> "alt\n";
      case PARALLEL -> "group\n";
      case NONE -> "";
    };
  }

  private static void generateMethodCall(String sourceEntity, String dstEntity, String methodName,
                                         Writer writer) throws IOException {
    writer.append(sourceEntity)
            .append(" -> ")
            .append(dstEntity)
            .append(": ")
            .append(methodName)
            .append("()\n");
  }

  private static void generateCalls(Call.Group group, Set<Entity> entities,
                                    Map<String, Entity> cache,
                                    Writer writer) throws IOException {
    if (group.equals(Call.Group.EMPTY_GROUP)) {
      return;
    }

    var currentGroupKind = group.kind();

    if (!currentGroupKind.equals(Call.Group.Kind.NONE)) {
      writer.append(groupKind(currentGroupKind));
    }

    var nbCalls = 0;
    for (var call : group.calls()) {
      nbCalls += 1;
      if (call instanceof Call.Group groupCall) {
        generateCalls(
                groupCall,
                entities,
                cache,
                writer
        );

        if (currentGroupKind.equals(Call.Group.Kind.ALTERNATE) && nbCalls < group.calls().size()) {
          writer.append("else\n");
        }
      } else if (call instanceof Call.MethodCall methodCall) {
        var srcEntityName = methodCall.ownerName();
        var dstEntityName = ExecutionPathResolver
                .getEntityFromMethodName(methodCall.name(), entities, cache)
                .type().name();

        generateMethodCall(srcEntityName, dstEntityName, methodCall.name(), writer);
      } else {
        throw new IllegalStateException("Unknown Call subtype : must be Group or MethodCall");
      }
    }

    if (!currentGroupKind.equals(Call.Group.Kind.NONE)) {
      writer.append("end\n");
    }
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
  public void generateSequenceDiagram(boolean header, Method entryPoint, Package p,
                                      Writer writer) throws IOException {
    requireNonNull(entryPoint);
    requireNonNull(writer);

    if (header) {
      addHeader(writer);
    }

    var firstGroup = ExecutionPathResolver.relevantCallsGroup(entryPoint.callGroup(), p);

    var entities = p.entities().stream()
            .filter(entity -> entity.type().name().startsWith(p.name()))
            .collect(Collectors.toSet());
    var cache = new HashMap<String, Entity>();
    generateCalls(firstGroup, entities, cache, writer);

    if (header) {
      addFooter(writer);
    }
  }
}

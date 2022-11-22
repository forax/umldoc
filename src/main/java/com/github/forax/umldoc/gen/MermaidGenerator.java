package com.github.forax.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.AssociationDependency.Cardinality;
import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Entity.Stereotype;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.core.SubtypeDependency;
import com.github.forax.umldoc.core.TypeInfo;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

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

  private static String groupKind(Call.Group.Kind groupKind) {
    return switch (groupKind) {
      case LOOP -> "loop\n";
      case OPTIONAL -> "opt\n";
      case ALTERNATE -> "alt\n";
      case PARALLEL -> "par\n";
      default -> "";
    };
  }

  private static void generateMethodCall(String sourceEntity, String dstEntity, String methodName,
                                         Writer writer) throws IOException {

    writer.append(sourceEntity)
            .append(" ->> ")
            .append(dstEntity)
            .append(": ")
            .append(methodName)
            .append("()\n");
  }

  private static void generateCalls(Call.Group group, Set<Entity> entities,
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
                writer
        );

        if (currentGroupKind.equals(Call.Group.Kind.ALTERNATE) && nbCalls < group.calls().size()) {
          writer.append("else\n");
        }
      }

      if (call instanceof Call.MethodCall methodCall) {
        var srcEntityName = methodCall.ownerName();
        var dstEntityName = ExecutionPathResolver
                .findEntityFromMethodName(methodCall.name(), entities).type().name();
        generateMethodCall(srcEntityName, dstEntityName, methodCall.name(), writer);
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
      writer.append("""
          classDiagram
          direction TB
          
          """);
    }

    generateEntities(entities, writer);
    generateDependencies(dependencies, writer);
  }

  @Override
  public void generateSequenceDiagram(boolean header, Method entryPoint, Package p,
                                      Writer writer) throws IOException {
    requireNonNull(entryPoint);
    requireNonNull(writer);

    if (header) {
      writer.append("""
          sequenceDiagram
          
          """);
    }

    var firstGroup = ExecutionPathResolver.relevantCallsGroup(entryPoint.callGroup(), p);

    var entities = p.entities().stream()
            .filter(entity -> entity.type().name().startsWith(p.name()))
            .collect(Collectors.toSet());

    generateCalls(firstGroup, entities, writer);

    writer.append('\n');
  }
}

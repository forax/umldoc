package com.github.pereiratostain.generator;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generate a schema in the Mermaid format.
 */
public class MermaidSchemaGenerator implements Generator {

  private static String cleanName(String name) {
    name = name.replace('-', '_');
    name = name.replace('$', ' ');
    name = name.replace(';', ' ');
    return name.substring(name.lastIndexOf('/') + 1);
  }

  @Override
  public void generate(Writer writer, List<Entity> entities,
                       List<AssociationDependency> associations) throws IOException {
    requireNonNull(writer);
    requireNonNull(entities);
    requireNonNull(associations);

    generateHeader(writer);
    for (var entity : entities) {
      generateEntity(writer, entity);
    }
    for (var association : associations) {
      generateAssociation(writer, association);
    }
  }

  private void generateHeader(Writer writer) throws IOException {
    writer.append("""
            classDiagram
                direction TB

            """);
  }

  private void generateEntity(Writer writer, Entity entity) throws IOException {
    var fields = generateFieldsOfEntity(entity);
    var stereotype = stereotypeToString(entity.stereotype());
    var entityName = cleanName(entity.type().name());
    writer.append("""
                class %s {
                  %s
                  %s
                }
            
            """
            .formatted(entityName, stereotype, fields));
  }

  private String generateFieldsOfEntity(Entity entity) {
    return entity.fields().stream()
            .map(field -> applyModifiersToName(
                    field.modifiers(),
                    field.typeInfo(),
                    cleanName(field.name())))
            .collect(Collectors.joining("\n      "));
  }

  private void generateAssociation(Writer writer,
                                   AssociationDependency association) throws IOException {
    var leftSide = association.left();
    var rightSide = association.right();
    var leftClass = cleanName(leftSide.entity().type().name());
    var rightClass = cleanName(rightSide.entity().type().name());
    var leftCardinality = cardinalityToString(leftSide.cardinality());
    var rightCardinality = cardinalityToString(rightSide.cardinality());
    var arrow = generateArrow(leftSide, rightSide);
    var label = getAssociationLabel(leftSide, rightSide);
    writer.append("""
            %s %s %s %s %s %s
          """
          .formatted(leftClass,
                  leftCardinality,
                  arrow,
                  rightCardinality,
                  rightClass,
                  label));
  }

  private String generateArrow(AssociationDependency.Side leftSide,
                               AssociationDependency.Side rightSide) {
    String arrow = "";
    if (leftSide.navigability()) {
      arrow += "<";
    }
    arrow += "--";
    if (rightSide.navigability()) {
      arrow += ">";
    }
    return arrow;
  }

  private String getAssociationLabel(AssociationDependency.Side leftSide,
                                     AssociationDependency.Side rightSide) {
    var leftLabel = leftSide.label();
    var rightLabel = rightSide.label();
    if (leftLabel.isPresent() && rightLabel.isPresent()) {
      throw new IllegalStateException("Only one side of the association can hold the label");
    }
    return leftLabel.orElseGet(() -> {
      if (rightLabel.isEmpty()) {
        return "";
      }
      return rightLabel.get();
    });
  }

  private String stereotypeToString(Entity.Stereotype stereotype) {
    return switch (stereotype) {
      case ENUM -> "<<enumeration>>";
      case RECORD -> "<<record>>";
      case INTERFACE -> "<<interface>>";
      case ABSTRACT -> "<<abstract>>";
      case ANNOTATION, CLASS -> "";
    };
  }

  private static String cardinalityToString(AssociationDependency.Cardinality cardinality) {
    return switch (cardinality) {
      case MANY -> "\"*\"";
      case ONLY_ONE -> "\"1\"";
      case ZERO_OR_ONE -> "\"0..1\"";
    };
  }

  private static String applyModifiersToName(Set<Modifier> modifiers, TypeInfo type, String name) {
    String prefix = "";
    String suffix = "";
    for (var modifier : modifiers) {
      switch (modifier) {
        case PUBLIC -> prefix = "+";
        case PRIVATE -> prefix = "-";
        case PROTECTED -> prefix = "#";
        case PACKAGE -> prefix = "~";
        case STATIC -> suffix = "$";
        case FINAL -> suffix = "*";
        default -> throw new AssertionError();
      }
    }
    return prefix + cleanName(type.name()) + " " +  name + suffix;
  }
}

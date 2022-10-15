package com.github.pereiratostain.generator;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Modifier;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Generate a schema in the Mermaid format.
 */
public class MermaidSchemaGenerator implements Generator {

  @Override
  public void generate(Writer writer, List<Entity> entities) throws IOException {
    requireNonNull(writer);
    requireNonNull(entities);

    var entitiesName = entities.stream().map(Entity::name).collect(Collectors.toSet());

    generateHeader(writer);
    for (var entity : entities) {
      generateEntity(writer, entity, entitiesName);
    }
  }

  private void generateHeader(Writer writer) throws IOException {
    writer.append("""
            classDiagram
                direction TB

            """);
  }

  private void generateEntity(Writer writer, Entity entity, Set<String> entitiesName) throws IOException {
    var associations = new ArrayList<String>();
    var stereotype = "";
    String fields;

    if (entity.stereotype() == Entity.Stereotype.ENUM) {
      fields = computeFieldsEnum(entity);
      stereotype = "\t<<enumeration>>\n";
    } else {
      fields = computeFieldsClass(entity, associations, entitiesName);
    }

    writer.append("    class ")
            .append(entity.name())
            .append(" {\n")
            .append(stereotype)
            .append(fields)
            .append("\n    }\n")
            .append(generateAssociations(entity, associations))
            .append("\n");
  }

  private String computeFieldsEnum(Entity entity) {
    var fields = new ArrayList<Field>();

    for (var field : entity.fields()) {
      fields.add(field);
    }
    return generateRecordFields(fields);
  }

  private String computeFieldsClass(Entity entity, ArrayList<String> associations, Set<String> entitiesName) {
    var fields = new ArrayList<Field>();
    Pattern pattern = Pattern.compile("<.*>");

    for (var field : entity.fields()) {
      var fieldType = field.type();
      fieldType = fieldType.replace(";", "");

      Matcher matcher = pattern.matcher(fieldType);

      if (matcher.find()) {
        var string = matcher.group(0);
        string = string.replace("<", "");
        string = string.replace(">", "");
        fieldType = string;
      }

      if (entitiesName.contains(fieldType)) {
        associations.add(fieldType);
      } else {
        fields.add(field);
      }
    }

    return generateFields(fields);
  }

  private String generateFields(List<Field> fields) {
    var string = new StringBuilder();

    for (var field : fields) {
      string
              .append("\t")
              .append(modifierToString(field.modifiers().iterator().next()))
              .append(field.name()).append(" : ")
              .append(field.type().replace(";", ""))
              .append("\n");
    }
    return string.toString();
  }

  private String generateRecordFields(List<Field> fields) {
    var string = new StringBuilder();

    for (var field : fields) {
      string.append("\t").append(field.name()).append("\n");
    }
    return string.toString();
  }

  private String generateAssociations(Entity entity, List<String> associations) {
    var string = new StringBuilder();

    for (var field : associations) {
      string.append("\t").append(entity.name()).append(" --> ").append(field).append('\n');
    }
    return string.toString();
  }

  private static String modifierToString(Modifier modifier) {
    return switch (modifier) {
      case PUBLIC -> "+";
      case PRIVATE -> "-";
      case PROTECTED -> "#";
      case PACKAGE -> "~";
      default -> throw new IllegalArgumentException("This modifier can't be convert to a String");
    };
  }
}

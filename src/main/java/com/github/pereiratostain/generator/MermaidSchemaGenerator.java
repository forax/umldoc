package com.github.pereiratostain.generator;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.Entity;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Generate a schema in the Mermaid format.
 */
public class MermaidSchemaGenerator implements Generator {

  @Override
  public void generate(Writer writer, List<Entity> entities) throws IOException {
    requireNonNull(writer);
    requireNonNull(entities);

    generateHeader(writer);
    for (var entity : entities) {
      generateEntity(writer, entity);
    }
  }

  private void generateHeader(Writer writer) throws IOException {
    writer.append("""
            classDiagram
                direction TB

            """);
  }

  private void generateEntity(Writer writer, Entity entity) throws IOException {
    writer.append("    class ")
            .append(entity.name())
            .append(" {")
            .append("\n    }\n\n");
  }
}

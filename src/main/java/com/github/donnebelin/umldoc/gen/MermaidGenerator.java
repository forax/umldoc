package com.github.donnebelin.umldoc.gen;

import com.github.donnebelin.umldoc.Helper;
import com.github.donnebelin.umldoc.builder.GeneratorBuilder;
import com.github.donnebelin.umldoc.classdiagram.DiagramFormater;
import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.Entity;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Generate a class diagram using the mermaid format.
 */
public final class MermaidGenerator implements Generator {
  private final GeneratorBuilder.MermaidBuilder builder = new GeneratorBuilder.MermaidBuilder();

  @Override
  public void generate(boolean header, List<Entity> entities,
                       List<AssociationDependency> dependencies,
                       Writer writer) throws IOException {
    Objects.requireNonNull(entities);
    Objects.requireNonNull(dependencies);
    Objects.requireNonNull(writer);
    if (header) {
      writer.append("""
              classDiagram
                  direction TB
                        
              """);
    }

    //Entity --> "*" Field : fields
    for (var dependency : dependencies) {
      writer.append("""
              %s --> "%s" %s : %s
              """.formatted(DiagramFormater.getEntityNameWithoutPackage(
                      dependency.left().entity().type().name()),
              Helper.parseCardinalities(dependency.right().cardinality()),
              DiagramFormater.getEntityNameWithoutPackage(
                      dependency.right().entity().type().name()),
              dependency.right().label().orElse("Not defined")
      ));
    }

    for (var entity : entities) {
      writer.append("""
                  class %s {
                    %s
                  }

              """.formatted(
              builder.build(DiagramFormater.getEntityNameWithoutPackage(entity.type().name())),
              entity.fields()
                      .stream()
                      .map(field -> Generator.fieldToString(field, builder))
                      .collect(Collectors.joining("\n\t\t\t"))
      ));
    }
  }
}

package com.github.donnebelin.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.donnebelin.umldoc.Helper;
import com.github.donnebelin.umldoc.builder.GeneratorBuilder;
import com.github.donnebelin.umldoc.classdiagram.DiagramFormater;
import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.Entity;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generate a class diagram using the plantuml format.
 */
public final class PlantUmlGenerator implements Generator {
  private final GeneratorBuilder.PlantBuilder builder = new GeneratorBuilder.PlantBuilder();

  @Override
  public void generate(boolean header, List<Entity> entities,
                       List<AssociationDependency> dependencies,
                       Writer writer) throws IOException {
    requireNonNull(entities);
    requireNonNull(dependencies);
    requireNonNull(writer);
    if (header) {
      writer.append("""
              @startuml
                        
              """);
    }

    for (var dependency : dependencies) {
      writer.append("""
              %s "%s" -->  "%s" %s : %s
              """.formatted(DiagramFormater.getEntityNameWithoutPackage(
                      dependency.left().entity().type().name()),
              Helper.parseCardinalities(dependency.left().cardinality()),
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

    if (header) {
      writer.append("""
              @enduml
              """);
    }
  }
}

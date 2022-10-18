package com.github.donnebelin.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.donnebelin.umldoc.Helper;
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
              """.formatted(dependency.left().entity().name(),
              Helper.parseCardinalities(dependency.left().cardinality()),
              Helper.parseCardinalities(dependency.right().cardinality()),
              dependency.right().entity().name(),
              dependency.right().label().orElse("Not defined")
      ));
    }

    for (var entity : entities) {
      writer.append("""
                  class %s {
                    %s
                  }

              """.formatted(
              entity.name(),
              entity.fields()
                      .stream()
                      .map(Generator::fieldToString)
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

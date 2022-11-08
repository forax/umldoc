package com.github.forax.umldoc.editor;

import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.gen.Generator;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

/**
 * Create a configuration capable of generating an uml class diagram when given a writer.
 *
 * @param entities
 *         list of entities that will be use for uml generation
 * @param dependencies
 *         list of entities that will be use for uml generation
 * @param generator
 *         the generator to use.
 */
public record GeneratorConfiguration(List<Entity> entities, List<Dependency> dependencies,
                                     Generator generator) {

  /**
   * Create a configuration capable of generating an uml class diagram when given a writer.
   *
   * @param entities
   *         list of entities that will be use for uml generation
   * @param dependencies
   *         list of entities that will be use for uml generation
   * @param generator
   *         the generator to use.
   */
  public GeneratorConfiguration {
    entities = List.copyOf(entities);
    dependencies = List.copyOf(dependencies);
    Objects.requireNonNull(generator);
  }

  public static GeneratorConfiguration filterPackage(String packageName, Generator generator) {
    return new GeneratorConfiguration(null, null, generator);
  }

  public void generate(Writer writer) throws IOException {
    generator.generate(false, entities, dependencies, writer);
  }

}

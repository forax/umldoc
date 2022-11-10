package com.github.forax.umldoc.editor;

import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.gen.Generator;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

/**
 * Create a configuration capable of generating an uml class diagram when given a writer.
 *
 * @param filterPackage
 *         the package name that will be used for generation
 * @param generator
 *         the generator to use.
 */
public record GeneratorConfiguration(String filterPackage, Generator generator) {

  /**
   * Create a configuration capable of generating an uml class diagram when given a writer.
   *
   * @param filterPackage
   *         the package name that will be used for generation
   * @param generator
   *         the generator to use.
   */
  public GeneratorConfiguration {
    Objects.requireNonNull(filterPackage);
    Objects.requireNonNull(generator);
  }

  public static GeneratorConfiguration filterPackage(String packageName, Generator generator) {
    return new GeneratorConfiguration(packageName, generator);
  }

  public void generate(Writer writer, List<Package> module) throws IOException {
    var p = filterPackage(module);
    var entities = p.entities();
    var dependencies = p.dependencies();
    generator.generate(false, entities, dependencies, writer);
  }

  Package filterPackage(List<Package> module) {
    return module.stream()
                 .filter(p -> p.name()
                               .equals(filterPackage))
                 .findFirst()
                 .orElseThrow(() -> new IllegalArgumentException(
                         "package %s not found in module".formatted(filterPackage)));
  }

}

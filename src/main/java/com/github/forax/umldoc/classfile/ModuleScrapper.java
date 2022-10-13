package com.github.forax.umldoc.classfile;

import static java.util.stream.Collectors.groupingBy;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Package;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.module.ModuleReference;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Collect all the entities and dependencies as a list of packages.
 */
public final class ModuleScrapper {
  private ModuleScrapper() {
    throw new AssertionError();
  }

  private static String packageName(String entityName) {
    var index = entityName.lastIndexOf('.');
    return index == -1 ? "" : entityName.substring(0, index);
  }

  private static void parseModule(ModuleReference moduleReference, Consumer<Entity> consumer) {
    try (var reader = moduleReference.open()) {
      for (var filename : (Iterable<String>) reader.list()::iterator) {
        if (!filename.endsWith(".class") || filename.endsWith("-info.class")) {
          continue;
        }
        try (var inputStream = reader.open(filename).orElseThrow()) {
          var entity = ClassFileParser.parseClass(inputStream);
          consumer.accept(entity);
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Returns a list of packages contained in the module reference taken as argument.
   *
   * @param moduleReference the module reference
   * @return a list of package
   * @throws IOException if an I/O error occurs
   */
  public static List<Package> scrapModule(ModuleReference moduleReference) throws IOException {
    try {
      return Stream.of(moduleReference)
          .mapMulti(ModuleScrapper::parseModule)
          .collect(groupingBy(entity -> packageName(entity.name())))
          .entrySet().stream()
          .map(entry -> new Package(entry.getKey(), entry.getValue(), List.of()))
          .toList();
    } catch (UncheckedIOException e) {
      throw e.getCause();
    }
  }
}

package com.github.forax.umldoc.gen;

import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Convert a list of entities and a list of dependencies to a textual representation.
 */
public interface Generator {
  /**
   * Generate a textual representation of the entities and the dependencies.
   *
   * @param header if a header/footer should be generated
   * @param entities the list of entities
   * @param dependencies the list of dependencies
   * @param writer the writer
   * @throws IOException if an I/O exception occurs
   */
  void generate(boolean header, List<Entity> entities, List<Dependency> dependencies,
                            Writer writer) throws IOException;

  static void addHeader(boolean header, Writer writer) throws IOException {
    if (header) {
      writer.append("""
          @startuml
          
          """);
    }
  }

  static void addFooter(boolean header, Writer writer) throws IOException {
    if (header) {
      writer.append("""
          
          @enduml
          """);
    }
  }
}

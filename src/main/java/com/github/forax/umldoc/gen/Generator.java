package com.github.forax.umldoc.gen;

import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Method;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

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

  /**
   * Generate a Sequence diagram from an entry point method.
   * This method must be configured by the user.
   * Eg : Could be the main() method.
   *
   * @param header if a header/footer should be generated
   * @param entryEntity The entity where the entry point method is located
   * @param entryPoint the first method that will appear in the sequence diagram.
   *                   This method must be configured by the user.
   * @param writer the writer
   * @throws IOException if an I/O exception occurs
   */
  void generateSequenceDiagram(boolean header, Entity entryEntity, Method entryPoint,
                               Set<String> entities, Writer writer) throws IOException;
}

package com.github.pereiratostain.generator;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.Entity;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * A schema generator.
 */
public interface Generator {

  /**
   * Generates the schema made of the given entities.
   *
   * @param writer The writer used to output the schema
   * @param entities The entities composing the schema
   * @throws IOException if an I/O error occurs
   */
  void generate(Writer writer, List<Entity> entities,
                List<AssociationDependency> associations) throws IOException;
}

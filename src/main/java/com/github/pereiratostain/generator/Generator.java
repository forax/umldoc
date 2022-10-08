package com.github.pereiratostain.generator;

import com.github.forax.umldoc.core.Entity;
import java.io.IOException;
import java.util.List;

/**
 * A schema generator.
 */
public interface Generator {

  /**
   * Generates the schema made of the given entities.
   *
   * @param entities The entities composing the schema
   * @throws IOException if an I/O error occurs
   */
  void generate(List<Entity> entities) throws IOException;
}

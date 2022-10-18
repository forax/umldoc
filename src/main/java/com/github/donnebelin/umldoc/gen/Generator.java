package com.github.donnebelin.umldoc.gen;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Modifier;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

/**
 * Convert a list of entities and a list of dependencies to a textual representation.
 */
public interface Generator {
  private static char fieldAccessor(Set<Modifier> modifiers) {
    if (modifiers.contains(Modifier.PRIVATE)) {
      return '-';
    }

    if (modifiers.contains(Modifier.PROTECTED)) {
      return '#';
    }

    if (modifiers.contains(Modifier.PUBLIC)) {
      return '+';
    }

    return ' ';
  }

  /**
   * Supply the String representation of the given field for Plant UML and Mermaid UML languages.
   *
   * @param field the field to translate into Plant UML or Mermaid UML String representation
   * @return the String representation of the given field for Plant UML and Mermaid UML
   */
  static String fieldToString(Field field) {
    return fieldAccessor(field.modifiers()) + field.name() + ": " + field.type(); // -name: String
  }

  /**
   * Generate a textual representation of the entities and the dependencies.
   *
   * @param header       if a header/footer should be generated
   * @param entities     the list of entities
   * @param dependencies the list of dependencies
   * @param writer       the writer
   * @throws IOException if an I/O exception occurs
   */
  void generate(boolean header, List<Entity> entities, List<AssociationDependency> dependencies,
                Writer writer) throws IOException;
}

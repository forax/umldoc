package com.github.donnebelin.umldoc.builder;

import static java.util.Objects.requireNonNull;

/**
 * This class contains 2 records that respectively build a field for Mermaid and Plant UML.
 */
public final class GeneratorBuilder {
  /**
   * Build a field for planUML from a field parsed into a String.
   */
  public record PlantBuilder() implements Builder<String> {

    @Override
    public String build(String name) {
      requireNonNull(name);
      return name.replace('/', '_');
    }
  }

  /**
   * Build a field for MermaidBuilder from a field parsed into a String.
   */
  public record MermaidBuilder() implements Builder<String> {

    @Override
    public String build(String name) {
      requireNonNull(name);
      return name
              .replace('/', '_')
              .replace('$', '_')
              .replaceAll("<", "[")
              .replaceAll(">", "]");
    }
  }
}

package com.github.forax.umldoc.editor;

import java.util.Optional;

/**
 * This interface is implemented by MermaidCmdLineParser and PlantCmdLineParse.
 */
public interface CommandLineParser {
  Optional<GeneratorConfiguration> parseLine(String line);

  public boolean endLine(String line);

}

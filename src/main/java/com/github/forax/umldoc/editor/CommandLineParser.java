package com.github.forax.umldoc.editor;

import java.util.Optional;

public interface CommandLineParser {
  Optional<GeneratorConfiguration> parseLine (String line);
  public boolean endline (String line);

}

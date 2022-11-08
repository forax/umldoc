package com.github.forax.umldoc.editor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Objects;

public class Editor {

  private final HashMap<String, CommandLineParser> registration;

  public Editor(Map<String, CommandLineParser> registration) {
    Objects.requireNonNull(registration);
    this.registration = registration;
  }


  public void edit(BufferedWriter writer, BufferedReader reader) throws IOException {
    var line = "";
    while ((line = reader.readLine()) != null) {
      if (line.matches("```.*")) {
        var type = line.replace("```", "");
        var parser = registration.get(type);
        writer.write(line);
        if (parser == null) {
          // TODO : the string after ``` is wrong
          continue;
        }
        var generatorConfiguration = 0;
        // TODO : Optional<GeneratorConfiguration> line(String) to develop
        // if (gene == isEmpty) => write line
        // else => write generator result
      }
    }
    // We write in the file with writer
  }

}

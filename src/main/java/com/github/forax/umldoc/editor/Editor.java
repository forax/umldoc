package com.github.forax.umldoc.editor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class Editor {

  private final HashMap<String, CommandLineParser> registration;

  public Editor(Map<String, CommandLineParser> registration) {
    Objects.requireNonNull(registration);
    this.registration = registration;
  }


  public void edit(BufferedWriter writer, BufferedReader reader) throws IOException {
    var isInConfiguration = false; // Boolean to see if we are in ```
    var line = "";
    Optional<CommandLineParser> parser = Optional.empty();
    while ((line = reader.readLine()) != null) {
      if (line.matches("```.*")) {
        var type = line.replace("```", "");
        parser = registration.get(type);
        if(parser != null) {
          isInConfiguration = true;
        }
      } else if (isInConfiguration) {
        // TODO : call the parser to get the generator configuration
        // if (yes) => line = result
        // else => do nothing, isInConfiguration = !CommandLineParser.endLine()
      }
      writer.write(line);
    }
    // WRITE FINAL FILE
  }

}

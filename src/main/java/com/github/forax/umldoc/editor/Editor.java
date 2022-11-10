package com.github.forax.umldoc.editor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Editor {

  private enum State {
    PARSEOUTPUTTYPE,
    SEARCHCOMMANDLINE,
    READONLY,
    READWRITE
  }

  private final HashMap<String, CommandLineParser> registration;
  private final List<Package> module;
  private State state;

  public Editor(Map<String, CommandLineParser> registration,
                List<Package> module) {
    this.registration = Map.copyOf(registration);
    this.module = List.copyOf(module);
    this.state = State.READWRITE;
  }

  /**
   *
   * Can modify a file by calling methods to generate a diagram.
   *
   * @param writer File writer, used to modify file.
   * @param reader File reader, read the file.
   * @throws IOException If the file cannot be opened.
   */
  public void edit(Writer writer, BufferedReader reader) throws IOException {
    var isInConfiguration = false; // Boolean to see if we are in ```
    var line = "";
    var parser = Optional.empty();
    while ((line = reader.readLine()) != null) {
      switch (state) {
        case READWRITE :
          if (line.matches("```.*")) {
            var type = line.substring("```".length() + 1);
            parser = registration.get(type);
            if(parser != null) {
              state = State.SEARCHCOMMANDLINE;
              break;
            }
          }
          writer.write(line);
          break;
        case SEARCHCOMMANDLINE:
          // TODO : search the command line
          break;
        case PARSEOUTPUTTYPE:
          // TODO : get the type mermaid or plant
          break;
        case READONLY:
          // TODO : after the diagram, we don't care about next lines until ```
          break;
      }
      if (isInConfiguration) {
        // TODO : call the parser to get the generator configuration
        // if (yes) => line = result
        // else => do nothing, isInConfiguration = !CommandLineParser.endLine()
        // Give line to parser,if parser isNotEndline we do not write
        // else isEndLIne, we quit isInConfiguration
      }
      if (line.matches("```.*")) {
        var type = line.substring("```".length() + 1);
        parser = registration.get(type);
        if(parser != null) {
          isInConfiguration = true;
        }
      }
      writer.write(line);
    }
    // MOVE FINAL FILE
  }

  private Package getScope(List<Package> packages) {
    // TODO : call GeneratorConfig method to get the scope
    throw new IllegalStateException();
  }

  private void getDiagram(Writer writer) {
    // TODO : call Generator method to create the diagram according to the scope
    throw new IllegalStateException();
  }

}

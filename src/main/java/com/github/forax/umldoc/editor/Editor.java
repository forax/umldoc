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
    SEARCHCOMMANDLINE,
    READONLY,
    READWRITE
  }

  private final HashMap<String, CommandLineParser> registration;
  private final List<Package> module;
  private State state;
  private CommandLineParser parser;

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
   * @param writer Writer, used to modify file.
   * @param reader BufferedReader, read the file.
   * @throws IOException If the file cannot be opened.
   */
  public void edit(Writer writer, BufferedReader reader) throws IOException {
    var line = "";
    while ((line = reader.readLine()) != null) {
      state = switch (state) {
        case READWRITE -> readWrite(line, writer);
        case SEARCHCOMMANDLINE -> searchCommandLine(line, writer);
        case READONLY -> readOnly(line, writer);
      };
    }
  }

  private Package getScope(List<Package> packages) {
    // TODO : call GeneratorConfig method to get the scope
    throw new IllegalStateException();
  }

  private void getDiagram(Writer writer) {
    // TODO : call Generator method to create the diagram according to the scope
    throw new IllegalStateException();
  }

  State readWrite(String line, Writer writer) throws IOException {
    if (line.matches("```.+")) {
      var type = line.substring("```".length() + 1);
      parser = registration.get(type);
      if(parser != null) {
        return State.SEARCHCOMMANDLINE;
      }
    }
    writer.write(line);
    return State.READWRITE;
  }

  State searchCommandLine(String line, Writer writer) throws IOException {
    // if (parser.isStart(line)) {
    // call get Diagram and set state to READONLY
    // else read until we find the commandLine or backquotes
    // Placeholder
    if (true) {
      getDiagram(writer);
      return State.READONLY;
    }
    if (false) { // if is ending ```
      return State.READWRITE;
    }
    writer.write(line);
    return State.SEARCHCOMMANDLINE;
  }

  State readOnly(String line, Writer writer) {
    // Read until we see the backquotes and then we set to READWRITE
    if (true) { // if is ending ```
      return State.READWRITE;
    }
    return State.READONLY;
  }

}

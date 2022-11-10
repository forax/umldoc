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
   * @param writer File writer, used to modify file.
   * @param reader File reader, read the file.
   * @throws IOException If the file cannot be opened.
   */
  public void edit(Writer writer, BufferedReader reader) throws IOException {
    var line = "";
    while ((line = reader.readLine()) != null) {
      state = switch (state) {
        case READWRITE -> readWrite(line, writer);
        case SEARCHCOMMANDLINE -> searchCommandLine(line, writer);
        case PARSEOUTPUTTYPE -> parseOutputType(line, writer);
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

  private State readWrite(String line, Writer writer) throws IOException {
    if (line.matches("```.*")) {
      var type = line.substring("```".length() + 1);
      parser = registration.get(type);
      if(parser != null) {
        return State.SEARCHCOMMANDLINE;
      }
    }
    writer.write(line);
    return State.READWRITE;
  }

  private State searchCommandLine(String line, Writer writer) {
    // if (parser.isStart(line)) {
    //
    return State.SEARCHCOMMANDLINE;
  }

  private State parseOutputType(String line, Writer writer) {
    return State.PARSEOUTPUTTYPE;
  }

  private State readOnly(String line, Writer writer) {
    return State.READONLY;
  }

}

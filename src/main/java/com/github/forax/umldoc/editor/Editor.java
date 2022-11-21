package com.github.forax.umldoc.editor;

import com.github.forax.umldoc.core.Package;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The class Editor is able to read a file and write lines.
 */
public class Editor {

  /**
   * The enum State corresponds to the type of state the Editor can be
   * while reading lines.
   */
  enum State {
    SEARCHCOMMANDLINE,
    READONLY,
    READWRITE
  }

  /**
   * The enum Extension corresponds to the type of the file.
   * <br>Mermaid, PlantUML and Markdown.
   */
  public enum Extension {
    MARKDOWN,
    PLANTUML,
    MERMAID
  }

  private final Map<String, CommandLineParser> registration;
  private final List<Package> module;
  private State state;
  private CommandLineParser parser;
  private final Extension extension;

  /**
   * The constructor of Editor.
   *
   * @param registration Map, register a String with a CommandLineParser.
   * @param module List, a list of package.
   */
  public Editor(Extension extension, Map<String, CommandLineParser> registration,
                List<Package> module) {
    Objects.requireNonNull(extension);
    Objects.requireNonNull(registration);
    Objects.requireNonNull(module);
    this.registration = Map.copyOf(registration);
    this.module = List.copyOf(module);
    this.state = State.READWRITE;
    this.extension = extension;
  }

  /**
   * Can modify a file by calling methods to generate a diagram.
   *
   * @param writer Writer, used to modify file.
   * @param reader BufferedReader, read the file.
   * @throws IOException If the file cannot be opened.
   */
  public void edit(Writer writer, BufferedReader reader) throws IOException {
    Objects.requireNonNull(writer);
    Objects.requireNonNull(reader);
    String line;
    while ((line = reader.readLine()) != null) {
      state = switch (state) {
        case READWRITE -> readWrite(line, writer);
        case SEARCHCOMMANDLINE -> searchCommandLine(line, writer);
        case READONLY -> readOnly(line, writer);
      };
    }
  }

  private void getDiagram(GeneratorConfiguration generatorConfiguration,
                          Writer writer) throws IOException {
    generatorConfiguration.generate(writer, module);
  }

  /**
   * Method to read and write every line given.
   * <br>If it is the start of the umldoc command "```..."
   * then we change state.
   *
   * @param line String, it is one line of a file.
   * @param writer Writer, write in a temporary file.
   * @return State, SEARCHCOMMANDLINE or READWRITE.
   * @throws IOException If the file couldn't be opened.
   */
  State readWrite(String line, Writer writer) throws IOException {
    if (extension != Extension.MARKDOWN) {
      return searchCommandLine(line, writer);
    }
    writer.write(line + "\n");
    if (line.matches("```.+")) {
      var type = line.substring("```".length());
      parser = registration.get(type);
      if (parser != null) {
        return State.SEARCHCOMMANDLINE;
      }
    }
    return State.READWRITE;
  }

  /**
   * Method to search the command line.
   * <br>Example : "%% umldoc ..." or "' umldoc ...".
   *
   * @param line String, it is one line of a file.
   * @param writer Writer, write in a temporary file.
   * @return State, READONLY, READWRITE or SEARCHCOMMANDLINE.
   * @throws IOException If the file couldn't be opened.
   */
  State searchCommandLine(String line, Writer writer) throws IOException {
    var optional = parser.parseLine(line);
    if (optional.isPresent()) {
      getDiagram(optional.get(), writer);
      return State.READONLY;
    }
    writer.write(line + "\n");
    if (line.matches("```") || parser.endLine(line)) {
      return State.READWRITE;
    }
    return State.SEARCHCOMMANDLINE;
  }

  /**
   * Method to read and not write until we find the end.
   * <br>Example : "```".
   *
   * @param line String, it is one line of a file.
   * @param writer Writer, write in a temporary file.
   * @return State, READWRITE or READONLY.
   */
  State readOnly(String line, Writer writer) throws IOException {
    if (line.matches("```") || parser.endLine(line)) {
      writer.write(line + "\n");
      return State.READWRITE;
    }
    return State.READONLY;
  }
}

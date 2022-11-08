package com.github.forax.umldoc.classfile;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The interface used to differentiate multiple file reader.
 */
public sealed interface Reader permits MarkdownReader {

  /**
   * Create a reader to read
   *
   * @param extension File's extension
   *         File's path (in absolute or relative)
   * @return Reader A MarkdownReader, a PlantReader or a MermaidReader.
   */
  static Reader fromFileExtension(String extension) {
    return switch (extension) {
      case "md" -> new MarkdownReader();
      // TODO : CHANGE TO THE CORRESPONDING CLASS :)
      case "pu", "plantuml" -> new MarkdownReader();
      // TODO : CHANGE TO THE CORRESPONDING CLASS :)
      case "mmd", "mermaid" -> new MarkdownReader();
      default -> throw new IllegalArgumentException("Unknown file extension " + extension);
    };
  }

  /**
   * Used to read the file (.md, .pu, .plantuml, .mms, .mermaid).
   *
   * @throws IOException
   *         If the file is not accessible or doesn't exist
   */
  void readFile(Path path) throws IOException;

}

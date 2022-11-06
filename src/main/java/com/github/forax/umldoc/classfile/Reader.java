package com.github.forax.umldoc.classfile;

import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * The interface used to differentiate multiple file reader.
 *
 */
public sealed interface Reader permits MarkdownReader{

  /**
   *
   * Check the file extension.
   * Note : the existence of the file should be checked before calling this method.
   *
   * @param path File's path (in absolute or relative)
   * @return Reader A MarkdownReader, a PlantReader or a MermaidReader.
   */
  static Reader checkFileExtension(Path path) {

    var file = path.getFileName().toString();
    var extension = file.split(".");

    return new MarkdownReader();
  }

  /**
   *
   * Used to read the file (.md, .pu, .plantuml, .mms, .mermaid).
   *
   * @param path File's path
   * @throws IOException If the file is not accessible or doesn't exist
   */
  void readFile(Path path) throws IOException;

}

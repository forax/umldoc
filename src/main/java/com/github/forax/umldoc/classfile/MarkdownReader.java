package com.github.forax.umldoc.classfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/*
/**
*
* Enum to define what type of uml the user wants.
*

enum UMLType {
  PLANTUML,
  MERMAID
}
*/
/**
 *
 * A class used to read markdown file.
 *
 */
public final class MarkdownReader implements Reader {
  private final Path path;

  public MarkdownReader(Path path) {
    Objects.requireNonNull(path);
    this.path = path;
  }
  /**
   *
   * Read a md file and detect the annotation for the umldoc.
   *
   * @throws IOException If the file is not accessible or doesn't exist.
   */
  public void readFile() throws IOException {
    try (var file = Files.newBufferedReader(path)) {
      System.out.println("allo ?");
      // input the (modified) file content to the StringBuilder "input"
      var inputBuffer = new StringBuilder();
      String line;

      while ((line = file.readLine()) != null) {
        System.out.println(line);
        inputBuffer.append(line);
        inputBuffer.append('\n');
      }
      // write the new string with the replaced line OVER the same file
      var fileOut = Files.newOutputStream(Paths.get("rendu.md"));
      fileOut.write(inputBuffer.toString().getBytes());
      fileOut.close();
    } catch (Exception e) {
      throw new IOException("Problem reading file.");
    }
  }
}

package com.github.forax.umldoc.classfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
 * A class used to read markdown file.
 */
public final class MarkdownReader implements Reader {

  /**
   * Read a md file and detect the annotation for the umldoc.
   *
   * @throws IOException
   *         If the file is not accessible or doesn't exist.
   */
  public void readFile(Path path) throws IOException {
    try (var file = Files.newBufferedReader(path)) {
      // input the (modified) file content to the StringBuilder "input"
      var inputBuffer = new StringBuilder();
      String line;

      while ((line = file.readLine()) != null) {
        inputBuffer.append(line);
        inputBuffer.append('\n');
        if (line.startsWith("```plantuml")) {
          parsePlant(file, inputBuffer);
        } else if (line.startsWith("```mermaid")) {
          parseMermaid(file, inputBuffer);
        }
      }
      // write the new string with the replaced line OVER the same file
      var fileOut = Files.newOutputStream(Paths.get("rendu.md"));
      fileOut.write(inputBuffer.toString()
                               .getBytes());
      fileOut.close();
    } catch (Exception e) {
      throw new IOException("Problem reading file.");
    }
  }

  private void parsePlant(BufferedReader file, StringBuilder inputBuffer) throws IOException {
    var line = file.readLine();
    if (!line.equals("@startuml")) {
      throw new IllegalArgumentException("Bad format. ```plantuml must be followed by @startuml");
    }

    line = file.readLine();
    var diagramme = line;

    line = file.readLine();
    if (!line.equals("@enduml")) {
      throw new IllegalArgumentException("Bad format. ```plantuml must be ended by @enduml");
    }

    inputBuffer.append(diagramme);
    inputBuffer.append('\n');
  }

  private void parseMermaid(BufferedReader file, StringBuilder inputBuffer) throws IOException {
    var line = file.readLine();
    var diagramme = line;

    inputBuffer.append(diagramme);
    inputBuffer.append('\n');
  }
}

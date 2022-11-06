package com.github.forax.umldoc.classfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

enum UMLType {
  PLANTUML,
  MERMAID
}

/**
 *
 * A class used to read markdown file.
 *
 */
public final class MarkdownReader implements Reader {

  /**
   *
   * A main class to test.
   *
   * @deprecated
   * NOTE : USED FOR TESTING ONLY (to be removed !).
   *
   * @param args Arguments for the main.
   */
  @Deprecated
  public static void main (String[] args) {
    try {
      var markdownReader = new MarkdownReader();
      markdownReader.readFile(Paths.get("YOUR_TEST_FILE.md"));
    } catch (IOException e) {
      System.out.println("Be sure that the file is accessible or created !");
    }
  }

  /**
   *
   * Read a md file and detect the annotation umldoc.
   *
   * @param path File's path.
   * @throws IOException If the file is not accessible or doesn't exist.
   */
  public void readFile(Path path) throws IOException {
    Stream<String> stream = Files.lines(path);
    stream.map(String::trim)
      .filter(l -> !l.isEmpty())
      .forEach(System.out::println);
  }
}

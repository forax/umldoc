package com.github.forax.umldoc.classfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

enum UMLType {
  PLANTUML,
  MERMAID
}

public class MarkdownReader {

  public static void main (String[] args) {
    try {
      readFile(Paths.get("YOUR_TEST_FILE.txt"));
    } catch (IOException e) {
      System.out.println("Be sure that the file is accessible or created !");
    }
  }

  public static void readFile(Path path) throws IOException {
    var classLoader = MarkdownReader.class.getClassLoader();
    var inputStream = classLoader.getResourceAsStream("test");
    Stream<String> stream = Files.lines(path);
    stream.map(String::trim)
      .filter(l -> !l.isEmpty())
      .forEach(System.out::println);
  }
}

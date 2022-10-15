package com.github.magickoders;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Package;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This is the main class.
 */
public class Main {

  private static void crash(String errorMessage) {
    System.err.println(errorMessage);
    System.exit(1);
  }

  private static void writeToPath(Path path, String content) throws IOException {
    try (var writer = Files.newBufferedWriter(path)) {
      writer.write(content);
    }
  }

  /**
   * This is the main method. aka the entry point of the program.
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      crash("Only one argument is accepted");
    }

    var testEntity = new Entity(Set.of(), "test", Optional.empty(), List.of(), List.of());
    var testPackage = new Package("test", List.of(testEntity, testEntity), List.of());

    System.out.println("mermaid\n");
    System.out.println(new MermaidParser().parse(testPackage));
    System.out.println();

    System.out.println("plant uml\n");
    System.out.println(new PlantumlParser().parse(testPackage));
    System.out.println();

  }
}

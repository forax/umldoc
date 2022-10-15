package com.github.magickoders;

import com.github.forax.umldoc.core.Entity;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Main class to launch the parser.
 */
public class Hello {

  /**
   * the main method.
   *
   * @param args arguments given from command line
   */
  public static void main(String[] args) throws IOException {
    System.out.println("Hello Adrien et Brian !");

    var entities = JarReader.getEntities();
    try (var writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8)) {
      var text = entities.stream()
              .filter(entity -> entity.name().contains("magickoders")
                      /*|| entity.name().contains("forax")*/)
              .map(Entity::toString)
              .collect(Collectors.joining("\n"));

      writer.append(text);
    }

  }
}

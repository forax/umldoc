package com.github.pereiratostain;

import com.github.forax.umldoc.core.Entity;
import com.github.pereiratostain.generator.MermaidSchemaGenerator;
import com.github.pereiratostain.visitor.ClassParser;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.module.ModuleFinder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

/**
 * Main class.
 */
public class Main {

  /**
   * Entry point of the program.
   *
   * @param args The arguments from the command line.
   */
  public static void main(String[] args) throws IOException {
    var entities = asm();
    try (var writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8)) {
      var associations = new DiagramComputer(entities).buildAssociations();
      var generator = new MermaidSchemaGenerator();
      generator.generate(writer, entities, associations);
    }
  }

  /**
   * Parse the classes in the target folder.
   *
   * @return A list of Entity
   *
   * @throws IOException if an I/O error occurs
   */
  public static List<Entity> asm() throws IOException {
    var path = Path.of("target");
    var finder = ModuleFinder.of(path);
    for (var moduleReference : finder.findAll()) {
      try (var reader = moduleReference.open()) {
        var visitors = new ArrayList<ClassParser>();
        for (var filename : (Iterable<String>) reader.list()::iterator) {
          if (!filename.endsWith(".class") || filename.endsWith("$1.class")) {
            continue;
          }
          try (var inputStream = reader.open(filename).orElseThrow()) {
            var classReader = new ClassReader(inputStream);
            var visitor = new ClassParser(Opcodes.ASM9);

            classReader.accept(visitor, 0);
            visitors.add(visitor);
          }
        }
        return visitors.stream().map(ClassParser::getEntity).toList();
      }
    }
    return List.of();
  }
}

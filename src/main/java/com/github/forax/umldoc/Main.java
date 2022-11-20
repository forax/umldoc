package com.github.forax.umldoc;

import com.github.forax.umldoc.classfile.ModuleScrapper;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.editor.CommandLineParser;
import com.github.forax.umldoc.editor.Editor;
import com.github.forax.umldoc.editor.MermaidCmdLineParser;
import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Contain the main method.
 */
public class Main {
  /**
   * The main method.
   *
   * @param args Contain the path of the jar file from which
   *             the module will be looked after and the
   *             path of the markdown file.
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage : java -jar path_of_jar"
              + " path_of_jar path_of_markdown");
      return;
    }
    var finder = ModuleFinder.of(Path.of(args[0]));
    var modules = finder.findAll().iterator();

    if (!modules.hasNext()) {
      System.err.println("Couldn't find a Module");
      return;
    }
    List<Package> packages;
    try {
      packages = ModuleScrapper.scrapModule(modules.next());
    } catch (IOException e) {
      System.err.println("Couldn't get the list of Packages : " + e.getMessage());
      return;
    }

    try (
            var reader = Files.newBufferedReader(Path.of(args[1]));
            var writer = Files.newBufferedWriter(Path.of("resultFile.md"))
    ) {
      var config = Map.<String, CommandLineParser>of("mermaid", new MermaidCmdLineParser());
      //config.put("plantuml", new PlantCmdLineParser());

      var editor = new Editor(config, packages);
      editor.edit(writer, reader);
    } catch (IOException e) {
      System.err.println("Couldn't read or write in the file : " + e.getMessage());
    }
  }
}

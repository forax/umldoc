package com.github.forax.umldoc;

import com.github.forax.umldoc.classfile.ModuleScrapper;
import com.github.forax.umldoc.editor.CommandLineParser;
import com.github.forax.umldoc.editor.Editor;
import com.github.forax.umldoc.editor.MermaidCmdLineParser;
import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Main {

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      throw new IllegalArgumentException("Usage : java -jar path_of_jar"
              + " path_of_jar path_of_markdown");
    }
    var finder = ModuleFinder.of(Path.of(args[0]));
    var module = finder.find("com.github.forax.umldoc");

    if (module.isEmpty()) {
      System.err.println("Couldn't find the Module");
      return;
    }
    var packages = ModuleScrapper.scrapModule(module.get());

    try (
            var reader = Files.newBufferedReader(Path.of(args[1]));
            var writer = Files.newBufferedWriter(Path.of("resultFile.md"))
    ) {
      Map<String, CommandLineParser> config = Map.of("mermaid", new MermaidCmdLineParser());
      //config.put("plantuml", new PlantCmdLineParser());

      var editor = new Editor(config, packages);
      editor.edit(writer, reader);
    }
  }
}

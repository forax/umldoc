package com.github.forax.umldoc;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import com.github.forax.umldoc.classfile.ModuleScrapper;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.editor.CommandLineParser;
import com.github.forax.umldoc.editor.Editor;
import com.github.forax.umldoc.editor.Editor.Extension;
import com.github.forax.umldoc.editor.MermaidCmdLineParser;
import com.github.forax.umldoc.editor.PlantCmdLineParser;
import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

/**
 * Contain the main method.
 */
public class Main {

  /**
   * The main method.
   * <br>
   *
   * @param args Contain the path of the jar file from which
   *             the module will be looked after and the
   *             path of the markdown file.
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage : java -jar path_of_jar"
              + " path_of_repository_module-info.class path_of_markdown");
      System.exit(-1);
      return;
    }
    var finder = ModuleFinder.of(Path.of(args[0]));
    var modules = finder.findAll().iterator();

    if (!modules.hasNext()) {
      System.err.println("Couldn't find a Module");
      System.exit(-1);
      return;
    }
    List<Package> packages;
    try {
      packages = ModuleScrapper.scrapModule(modules.next());
    } catch (IOException e) {
      System.err.println("Couldn't get the list of Packages : " + e.getMessage());
      System.exit(-1);
      return;
    }

    var index = args[1].lastIndexOf(".");
    if (index == -1) {
      System.err.println("Couldn't find an extension in " + args[1]);
      System.exit(-1);
      return;
    }
    var fileName = "resultFile.md";
    var inputPath = Path.of(args[1]);
    var outputPath = Path.of(fileName);
    var extension = args[1].substring(index + 1);
    try (
            var reader = Files.newBufferedReader(Path.of(args[1]));
            var writer = Files.newBufferedWriter(Path.of("resultFile.md"))
    ) {
      var config = new HashMap<String, CommandLineParser>();
      config.put("mermaid", new MermaidCmdLineParser());
      config.put("plantuml", new PlantCmdLineParser());

      var editor = new Editor(getExtension(extension), config, packages);
      editor.edit(writer, reader);
      Files.move(outputPath, inputPath, REPLACE_EXISTING, ATOMIC_MOVE);
    } catch (IOException e) {
      System.err.println("Couldn't read or write in the file : " + e.getMessage());
      System.exit(-1);
    } catch (IllegalArgumentException e) {
      System.err.println(e + " - Supported file type : pu, mmd, md");
      System.exit(-1);
    }
  }

  private static Extension getExtension(String extension) {
    return switch (extension) {
      case "md" -> Extension.MARKDOWN;
      case "pu" -> Extension.PLANTUML;
      case "mmd" -> Extension.MERMAID;
      default -> throw new IllegalArgumentException("unsupported file type");
    };
  }

}

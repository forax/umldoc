package com.github.forax.umldoc;

import com.github.forax.umldoc.classfile.ModuleScrapper;
import com.github.forax.umldoc.editor.Editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Javadoc for Checkstyle.
 */
public class Main {
  /**
   * Javadoc for checkstyle.
   *
   * @param args lol
   * @throws IOException dcscsqw
   */
  public static void main(String[] args) throws IOException {
    var reader = Files.newBufferedReader(Path.of("design/test.md"));
    var writer = Files.newBufferedWriter(Path.of("design/test.md"));
    var config = new HashMap<String, CommandLineParser>();
    config.put("mermaid", new MermaidCommandLineParser());
    config.put("plantuml", new PlantCommandLineParser());
    var editor = new Editor(config);
    editor.edit(writer, reader);
  }
}

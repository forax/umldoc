package com.github.forax.umldoc;

import com.github.forax.umldoc.classfile.ModuleScrapper;
import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;

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
    var path = Path.of("target/classes");
    var finder = ModuleFinder.of(path);
    for (var moduleReference : finder.findAll()) {
      ModuleScrapper.scrapModule(moduleReference);
    }
  }
}

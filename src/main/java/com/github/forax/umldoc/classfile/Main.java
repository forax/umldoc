package com.github.forax.umldoc.classfile;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
  public static void main (String[] args) throws IOException {
    var pathToFile = Paths.get("EntityBuilder.java");
    System.out.println(pathToFile.toAbsolutePath());
    var file = Reader.checkFileExtension(Paths.get("design/test.md"));
    file.readFile();
  }
}


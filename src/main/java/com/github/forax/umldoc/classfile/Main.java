package com.github.forax.umldoc.classfile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

  public static String getFileExtension(Path path) {
    var fileName = path.getFileName()
                       .toString();
    var dotPos = fileName.lastIndexOf('.');
    if (dotPos < 0) {
      throw new IllegalArgumentException(
              "The file does not contain an extension in the name " + fileName);
    }
    return fileName.substring(dotPos + 1);
  }

  public static void main(String[] args) throws IOException {
    var file = Reader.checkFileExtension(Paths.get("design/test.md"));
    file.readFile();
  }
  
}


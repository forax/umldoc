package com.github.forax.umldoc.classfile;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
  public static void main (String[] args) {
    var file = Reader.checkFileExtension(Paths.get("TEST_PATH.md"));
  }
}


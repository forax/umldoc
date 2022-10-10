package com.github.forax.umldoc.classfile;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ClassFileParserTest {
  static class DemoClass { }

  @Test
  public void parseClass() throws IOException {
    try(var inputStream = ClassFileParser.class.getResourceAsStream("ClassFileParserTest$DemoClass.class")) {
      var entity = ClassFileParser.parseClass(inputStream);
      assertEquals(DemoClass.class.getName(), entity.name());
    }
  }
}
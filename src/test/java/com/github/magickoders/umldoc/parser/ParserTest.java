package com.github.magickoders.umldoc.parser;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Modifier;
import com.github.magickoders.MermaidParser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;

public class ParserTest {
  /*
  @Nested
  public class TestEntity {
    @Test
    public void precondition() {
      assertThrows(NullPointerException.class, () -> MermaidParser.parseEntity(null));
      assertThrows(IllegalArgumentException.class, () -> MermaidParser.parseField(new Entity(Set.of(),"Test" , Optional.empty(), "Test")));
    }
    @Test
    public void parse() {
      var field = new Field(Set.of(Modifier.PRIVATE), "test", "Test");
      var result = MermaidParser.parseField(field);
      assertEquals("-test Test", result);
    }
  }

  @Nested
  public class TestField {
    @Test
    public void precondition() {
      assertThrows(NullPointerException.class, () -> MermaidParser.parseField(null));
      assertThrows(IllegalArgumentException.class, () -> MermaidParser.parseField(new Field(Set.of(Modifier.PRIVATE, Modifier.PUBLIC), "test", "Test")));
    }
    @Test
    public void parse() {
      var field = new Field(Set.of(Modifier.PRIVATE), "test", "Test");
      var result = MermaidParser.parseField(field);
      assertEquals("-test Test", result);
    }
  }
  */
}

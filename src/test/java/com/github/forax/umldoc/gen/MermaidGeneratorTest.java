package com.github.forax.umldoc.gen;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Entity.Stereotype;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MermaidGeneratorTest {
  @Test
  public void generate() throws IOException {
    var entity = new Entity(Set.of(), "Entity", Stereotype.CLASS, List.of(), List.of());
    var mermaidGenerator = new MermaidGenerator();
    var writer = new StringWriter();
    mermaidGenerator.generate(true, List.of(entity), List.of(), writer);
    assertEquals("""
        classDiagram
        direction TB
        
        class Entity {
        }
        
        """, writer.toString());
  }
}
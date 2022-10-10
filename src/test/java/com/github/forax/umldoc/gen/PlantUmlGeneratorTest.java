package com.github.forax.umldoc.gen;

import com.github.forax.umldoc.core.Entity;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUmlGeneratorTest {
  @Test
  public void generate() throws IOException {
    var entity = new Entity(Set.of(), "Entity", Optional.empty(), List.of(), List.of());
    var mermaidGenerator = new PlantUmlGenerator();
    var writer = new StringWriter();
    mermaidGenerator.generate(true, List.of(entity), List.of(), writer);
    assertEquals("""
        @startuml
        
            class Entity {
            }
        
        @enduml
        """, writer.toString());
  }
}
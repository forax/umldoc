package com.github.forax.umldoc.gen;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Entity.Stereotype;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUmlGeneratorTest {
  @Test
  public void generate() throws IOException {
    var entity = new Entity(Set.of(), "Entity", Stereotype.CLASS, List.of(), List.of());
    var mermaidGenerator = new ClassDiagramPlantUmlGenerator();
    var writer = new StringWriter();
    mermaidGenerator.generate(true, List.of(entity), List.of(), writer);
    assertEquals("""
        @startuml

        class Entity {
        }

        @enduml
        """, writer.toString());
  }

  @Test
  public void generateEnum() throws IOException {
    var modifier = new Entity(Set.of(), "Modifier", Entity.Stereotype.ENUM,
                              List.of(new Field(Set.of(), "PUBLIC", "Modifier")),
                              List.of());
    var mermaidGenerator = new ClassDiagramPlantUmlGenerator();
    var writer = new StringWriter();
    mermaidGenerator.generate(true, List.of(modifier), List.of(), writer);
    assertEquals("""
        @startuml
        
        enum Modifier {
          PUBLIC
        }

        @enduml
        """, writer.toString());
  }

  @Test
  public void generateSequenceDiagramMethodsPlantUml() throws IOException {
    var helloGroup = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall(
                    new TypeInfo(Optional.empty(),
                            "Alice",
                            List.of()
                    ),
                    "helloAlice",
                    new TypeInfo(
                            Optional.empty(),
                            "void",
                            List.of()),
                    List.of()
            ),
            new Call.MethodCall(
                    new TypeInfo(Optional.empty(),
                            "Bob",
                            List.of()
                    ),
                    "helloBob",
                    new TypeInfo(
                            Optional.empty(),
                            "void",
                            List.of()),
                    List.of()
            )
    ));
    var helloAlice = new Method(Set.of(Modifier.PUBLIC), "helloAlice", new TypeInfo(Optional.empty(), "void", List.of()), List.of(), helloGroup);
    var helloBob = new Method(Set.of(Modifier.PUBLIC), "helloBob", new TypeInfo(Optional.empty(), "void", List.of()), List.of(), helloGroup);
    var alice = new Entity(Set.of(), new TypeInfo(Optional.empty(), "Alice", List.of()), Stereotype.CLASS, List.of(), List.of(helloBob));
    var bob = new Entity(Set.of(), new TypeInfo(Optional.empty(), "Bob", List.of()), Stereotype.CLASS, List.of(), List.of(helloAlice));

    var writer = new StringWriter();
    var generator = new SequenceDiagramPlantUmlGenerator();
    generator.generate(true, List.of(alice, bob), List.of(), writer);
    assertEquals("""
        @startuml
        
        Alice -> Bob: helloBob()
        Bob -> Alice: helloAlice()

        @enduml
        """, writer.toString());
  }
}

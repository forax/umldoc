package com.github.forax.umldoc.gen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Entity.Stereotype;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.core.TypeInfo;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class PlantUmlGeneratorTest {
  @Test
  public void generate() throws IOException {
    var entity = new Entity(Set.of(), "Entity", Stereotype.CLASS, List.of(), List.of());
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

  @Test
  public void generateEnum() throws IOException {
    var modifier = new Entity(Set.of(), "Modifier", Entity.Stereotype.ENUM,
                              List.of(new Field(Set.of(), "PUBLIC", "Modifier")),
                              List.of());
    var mermaidGenerator = new PlantUmlGenerator();
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
            new Call.MethodCall("Alice", "main", "()V"),
            new Call.MethodCall("Bob", "helloBob", "()V"),
            new Call.MethodCall("Alice", "helloAlice","()V")
    ));
    var voidType = new TypeInfo(Optional.empty(), "void", List.of());
    var main = new Method(Set.of(Modifier.PUBLIC), "main", voidType, List.of(), "()V", helloGroup);
//    var helloAlice = new Method(Set.of(Modifier.PUBLIC), "helloAlice", voidType, List.of(), "()V", helloGroup);
    var helloBob = new Method(Set.of(Modifier.PUBLIC), "helloBob", voidType, List.of(), "()V", helloGroup);
    var alice = new Entity(Set.of(), new TypeInfo(Optional.empty(), "Alice", List.of()), Stereotype.CLASS, List.of(), List.of(main, helloBob));
//    var bob = new Entity(Set.of(), new TypeInfo(Optional.empty(), "Bob", List.of()), Stereotype.CLASS, List.of(), List.of(helloAlice));

    var writer = new StringWriter();
    var generator = new PlantUmlGenerator();
    var p = new Package("", List.of(), List.of());
    generator.generateSequenceDiagram(true, alice, main, p, writer);
    assertEquals("""
        @startuml
        
        Alice -> Alice: main()
        Alice -> Bob: helloBob()
        Bob -> Alice: helloAlice()
        @enduml
        """, writer.toString());
  }
}

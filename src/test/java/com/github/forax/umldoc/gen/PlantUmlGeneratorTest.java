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

  /*
      class Alice {
        void main() {
            var bob = new Bob();

          first group : None
            bob.helloBob();
            bob.toto();
        }

        void helloAlice() {
          empty group
            println("hello");
        }
      }

      class Bob {
        void helloBob() {
          var alice = new Alice();

          group : None
            alice.helloAlice();
        }

        void toto() {
          empty group
            println("toto");
        }
      }
   */
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
            @startuml

            Alice -> Bob: helloBob()
            Bob -> Alice: helloAlice()
            Alice -> Bob: toto()
            @enduml
            """, writer.toString());
  }

  /*
      class Alice {
        void main() {
            var bob = new Bob();

          first group : None
            bob.helloBob();
        }

        void helloAlice() {
          empty group
            println("hello");
        }
      }

      class Bob {
        void helloBob() {
          var alice = new Alice();
          var c = new Charlie();

          group : None
            c.toto();
            alice.helloAlice();
        }

        void tata() {
          empty group
            println("tata");
        }
      }

      class Charlie {
        void toto() {
          var bob = new Bob();

          empty : None
            bob.tata();
        }
      }
   */
  @Test
  public void generateSequenceDiagramMethodsPlantUmlWith3Entities() throws IOException {
    var charlieGroup = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall(
                    new TypeInfo(Optional.empty(),
                            "Bob",
                            List.of()
                    ),
                    "tata",
                    new TypeInfo(
                            Optional.empty(),
                            "void",
                            List.of()),
                    List.of()
            ),
            Call.Group.EMPTY_GROUP
    ));

    var bobGroup = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall(
                    new TypeInfo(Optional.empty(),
                            "Charlie",
                            List.of()
                    ),
                    "toto",
                    new TypeInfo(
                            Optional.empty(),
                            "void",
                            List.of()),
                    List.of()
            ),
            charlieGroup,
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
            )
    ));

    var mainGroup = new Call.Group(Call.Group.Kind.NONE, List.of(
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
            ),
            bobGroup,
            Call.Group.EMPTY_GROUP
    ));

    var helloBob = new Method(Set.of(Modifier.PUBLIC), "helloBob", new TypeInfo(Optional.empty(), "void", List.of()), List.of(), mainGroup);
    var helloAlice = new Method(Set.of(Modifier.PUBLIC), "helloAlice", new TypeInfo(Optional.empty(), "void", List.of()), List.of(), bobGroup);
    var alice = new Entity(Set.of(), new TypeInfo(Optional.empty(), "Alice", List.of()), Stereotype.CLASS, List.of(), List.of(helloAlice));
    var writer = new StringWriter();
    var generator = new PlantUmlGenerator();
    generator.generateSequenceDiagram(true, alice, helloBob, new Package("", List.of(), List.of()), writer);
    assertEquals("""
            @startuml

            Alice -> Bob: helloBob()
            Bob -> Charlie: toto()
            Charlie -> Bob: tata()
            Bob -> Alice: helloAlice()
            @enduml
            """, writer.toString());
  }

    /*
      class Alice {
        void main() {
            var bob = new Bob();

            bob.method1();
        }

        void method2() {
          // some stuff...
        }

        void method3() {
          var bob = new Bob();
          var charlie = new Charlie();

          charlie.method4();

          for(...) {
            bob.method5();
          }

          charlie.method6();
        }

        void method7() {
          // some stuff...
        }
      }

      class Bob {
        void method1() {
          var alice = new Alice();

          if(...) {
            alice.method2();
          } else if(...) {
            alice.method3();
          } else {
            alice.method7();
          }
        }

        void method5() {
          // some stuff...
        }
      }

      class Charlie {
        void method4() {
          // some stuff...
        }

        void method6() {
          // some stuff...
        }
      }
   */
  @Test
  public void generateSequenceDiagramMethodsPlantUmlWith3EntitiesAndDifferentGroups() throws IOException {
    var method3LoopGroup = new Call.Group(Call.Group.Kind.LOOP, List.of(
            new Call.MethodCall(new TypeInfo(Optional.empty(), "Bob", List.of()),
                    "method5",
                    new TypeInfo(Optional.empty(), "void", List.of()), List.of()
            ),
            Call.Group.EMPTY_GROUP
    ));

    var method3Group = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall(new TypeInfo(Optional.empty(), "Charlie", List.of()),
                    "method4",
                    new TypeInfo(Optional.empty(), "void", List.of()),
                    List.of()
            ),
            Call.Group.EMPTY_GROUP,
            method3LoopGroup,
            new Call.MethodCall(new TypeInfo(Optional.empty(), "Charlie", List.of()),
                    "method6",
                    new TypeInfo(Optional.empty(), "void", List.of()),
                    List.of()
            ),
            Call.Group.EMPTY_GROUP
    ));

    // ICICICIIC
    var bobAltGroup = new Call.Group(Call.Group.Kind.ALTERNATE, List.of(
            new Call.MethodCall(new TypeInfo(Optional.empty(), "Alice", List.of()),
                    "method2",
                    new TypeInfo(Optional.empty(), "void", List.of()),
                    List.of()
            ),
            Call.Group.EMPTY_GROUP,
            new Call.MethodCall(new TypeInfo(Optional.empty(), "Alice", List.of()),
                    "method3",
                    new TypeInfo(Optional.empty(), "void", List.of()),
                    List.of()
            ),
            method3Group,
            new Call.MethodCall(new TypeInfo(Optional.empty(), "Alice", List.of()),
                    "method7",
                    new TypeInfo(Optional.empty(), "void", List.of()),
                    List.of()
            ),
            Call.Group.EMPTY_GROUP
    ));

    var mainGroup = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall(new TypeInfo(Optional.empty(), "Bob", List.of()),
                    "method1",
                    new TypeInfo(Optional.empty(), "void", List.of()),
                    List.of()
            ),
            bobAltGroup
    ));

    var method1 = new Method(Set.of(Modifier.PUBLIC), "method1", new TypeInfo(Optional.empty(), "void", List.of()), List.of(), mainGroup);
    var method2 = new Method(Set.of(Modifier.PUBLIC), "method2", new TypeInfo(Optional.empty(), "void", List.of()), List.of(), bobAltGroup);
    var method3 = new Method(Set.of(Modifier.PUBLIC), "method3", new TypeInfo(Optional.empty(), "void", List.of()), List.of(), bobAltGroup);
    var method7 = new Method(Set.of(Modifier.PUBLIC), "method7", new TypeInfo(Optional.empty(), "void", List.of()), List.of(), bobAltGroup);
    var alice = new Entity(Set.of(), new TypeInfo(Optional.empty(), "Alice", List.of()), Stereotype.CLASS, List.of(),
            List.of(method2, method3, method7)
    );
    var writer = new StringWriter();
    var generator = new PlantUmlGenerator();
    generator.generateSequenceDiagram(true, alice, method1, new Package("", List.of(), List.of()), writer);
    assertEquals("""
            @startuml

            Alice -> Bob: method1()
            alt
            Bob -> Alice: method2()
            else
            Bob -> Alice: method3()
            Alice -> Charlie: method4()
            loop
            Alice -> Bob: method5()
            end
            Alice -> Charlie: method6()
            else
            Bob -> Alice: method7()
            end
            @enduml
            """, writer.toString());
  }
}

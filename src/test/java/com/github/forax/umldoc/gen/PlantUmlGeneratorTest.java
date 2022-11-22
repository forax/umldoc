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
import org.objectweb.asm.Type;

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
  public void generateSequenceDiagramSimple() throws IOException {
    var descriptor = Type.getMethodDescriptor(Type.getType(void.class));
    var voidType = TypeInfo.of("void");
    var publicModifier = Set.of(Modifier.PUBLIC);

    var mainGroup = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall("A", "m1", descriptor),
            new Call.MethodCall("A", "m2", descriptor),
            new Call.MethodCall("A", "m3", descriptor),
            new Call.MethodCall("A", "m4", descriptor)
    ));
    var m1 = new Method(publicModifier, "m1", voidType, List.of(), descriptor, mainGroup);
    var m2 = new Method(publicModifier, "m2", voidType, List.of(), descriptor, Call.Group.EMPTY_GROUP);
    var m3 = new Method(publicModifier, "m3", voidType, List.of(), descriptor, Call.Group.EMPTY_GROUP);
    var m4 = new Method(publicModifier, "m4", voidType, List.of(), descriptor, Call.Group.EMPTY_GROUP);
    var a = new Entity(Set.of(), new TypeInfo(Optional.empty(), "A", List.of()), Stereotype.CLASS, List.of(), List.of());
    var b = new Entity(Set.of(), new TypeInfo(Optional.empty(), "B", List.of()), Stereotype.CLASS, List.of(), List.of(m1, m2, m3, m4));
    var p = new Package("", List.of(a, b), List.of());

    var writer = new StringWriter();
    var generator = new PlantUmlGenerator();
    generator.generateSequenceDiagram(true, m1, p, writer);
    assertEquals("""
            @startuml
            
            A -> B: m1()
            A -> B: m2()
            A -> B: m3()
            A -> B: m4()
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
    var descriptor = Type.getMethodDescriptor(Type.getType(void.class));
    var voidType = TypeInfo.of("void");
    var publicModifier = Set.of(Modifier.PUBLIC);

    var helloGroup = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall("Alice", "toto", descriptor),
            new Call.MethodCall("Alice", "helloBob", descriptor),
            new Call.MethodCall("Bob", "helloAlice", descriptor) // ambiguity
    ));

    var toto = new Method(
            publicModifier,
            "toto",
            voidType,
            List.of(),
            descriptor,
            helloGroup
    );
    var helloBob = new Method(
            publicModifier,
            "helloBob",
            voidType,
            List.of(),
            descriptor,
            Call.Group.EMPTY_GROUP
    );
    var helloAlice = new Method(
            publicModifier,
            "helloAlice",
            voidType,
            List.of(),
            descriptor,
            Call.Group.EMPTY_GROUP
    );
    var alice = new Entity(Set.of(), new TypeInfo(Optional.empty(), "Alice", List.of()), Stereotype.CLASS, List.of(), List.of(toto, helloAlice));
    var bob = new Entity(Set.of(), new TypeInfo(Optional.empty(), "Bob", List.of()), Stereotype.CLASS, List.of(), List.of(helloBob));
    var p = new Package("", List.of(alice, bob), List.of());

    var writer = new StringWriter();
    var generator = new PlantUmlGenerator();
    generator.generateSequenceDiagram(true, toto, p, writer);
    assertEquals("""
        @startuml
        
        Alice -> Alice: toto()
        Alice -> Bob: helloBob()
        Bob -> Alice: helloAlice()
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
    var descriptor = Type.getMethodDescriptor(Type.getType(void.class));
    var voidType = TypeInfo.of("void");
    var publicModifier = Set.of(Modifier.PUBLIC);

    var charlieGroup = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall("Charlie", "tata", descriptor)
    ));

    var bobGroup = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall("Bob", "toto", descriptor),
            charlieGroup,
            new Call.MethodCall("Bob", "helloAlice", descriptor)
    ));

    var mainGroup = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall("Alice", "helloBob", descriptor),
            bobGroup
    ));

    var helloBob = new Method(
            publicModifier,
            "helloBob",
            voidType, List.of(),
            descriptor,
            mainGroup
    );
    var helloAlice = new Method(
            publicModifier,
            "helloAlice",
            voidType,
            List.of(),
            descriptor,
            Call.Group.EMPTY_GROUP
    );
    var toto = new Method(
            publicModifier,
            "toto",
            voidType,
            List.of(),
            descriptor,
            Call.Group.EMPTY_GROUP
    );
    var tata = new Method(
            publicModifier,
            "tata",
            voidType,
            List.of(),
            descriptor,
            Call.Group.EMPTY_GROUP
    );
    var alice = new Entity(
            Set.of(),
            new TypeInfo(Optional.empty(), "Alice", List.of()),
            Stereotype.CLASS,
            List.of(),
            List.of(helloAlice)
    );
    var bob = new Entity(
            Set.of(),
            new TypeInfo(Optional.empty(), "Bob", List.of()),
            Stereotype.CLASS,
            List.of(),
            List.of(helloBob, tata)
    );
    var charlie = new Entity(
            Set.of(),
            new TypeInfo(Optional.empty(), "Charlie", List.of()),
            Stereotype.CLASS,
            List.of(),
            List.of(toto)
    );
    var writer = new StringWriter();
    var generator = new PlantUmlGenerator();
    generator.generateSequenceDiagram(true, helloBob,
            new Package("", List.of(alice, bob, charlie),
                    List.of()),
            writer
    );
    assertEquals("""
            @startuml

            Alice -> Bob: helloBob()
            Bob -> Charlie: toto()
            Charlie -> Bob: tata()
            Bob -> Alice: helloAlice()
            @enduml
            """, writer.toString());
  }

  @Test
  public void generateSequenceDiagramMethodsWithOptionalGroupKind() throws IOException {
    var descriptor = Type.getMethodDescriptor(Type.getType(void.class));
    var voidType = TypeInfo.of("void");
    var publicModifier = Set.of(Modifier.PUBLIC);

    var mainGroup = new Call.Group(Call.Group.Kind.OPTIONAL, List.of(
            new Call.MethodCall("Alice", "helloBob", descriptor)
    ));
    var helloBob = new Method(
            publicModifier,
            "helloBob",
            voidType,
            List.of(),
            descriptor,
            mainGroup
    );
    var alice = new Entity(
            Set.of(),
            new TypeInfo(Optional.empty(), "Alice", List.of()),
            Stereotype.CLASS,
            List.of(),
            List.of()
    );
    var bob = new Entity(
            Set.of(),
            new TypeInfo(Optional.empty(), "Bob", List.of()),
            Stereotype.CLASS,
            List.of(),
            List.of(helloBob)
    );
    var writer = new StringWriter();
    var generator = new PlantUmlGenerator();
    generator.generateSequenceDiagram(true, helloBob,
            new Package("", List.of(alice, bob), List.of()),
            writer
    );
    assertEquals("""
            @startuml
            
            opt
            Alice -> Bob: helloBob()
            end
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
    var descriptor = Type.getMethodDescriptor(Type.getType(void.class));
    var voidType = TypeInfo.of("void");
    var publicModifier = Set.of(Modifier.PUBLIC);

    var method3LoopGroup = new Call.Group(Call.Group.Kind.LOOP, List.of(
            new Call.MethodCall("Alice", "method5", descriptor)
    ));
    var method3Group = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall("Alice", "method4", descriptor),
            method3LoopGroup,
            new Call.MethodCall("Alice", "method6", descriptor)
    ));
    var bobAltElse1Group = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall("Bob", "method3", descriptor),
            method3Group
    ));
    var bobAltElse2Group = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall("Bob", "method7", descriptor),
            new Call.MethodCall("Bob", "method3Bis", descriptor)
    ));
    var bobAltGroup = new Call.Group(Call.Group.Kind.ALTERNATE, List.of(
            new Call.Group(Call.Group.Kind.NONE, List.of(
                    new Call.MethodCall("Bob", "method2", descriptor)
            )),
            bobAltElse1Group,
            bobAltElse2Group
    ));
    var mainGroup = new Call.Group(Call.Group.Kind.NONE, List.of(
            new Call.MethodCall("Alice", "method1", descriptor),
            bobAltGroup
    ));
    var method1 = new Method(publicModifier, "method1", voidType, List.of(), descriptor, mainGroup);
    var method2 = new Method(publicModifier, "method2", voidType, List.of(), descriptor, Call.Group.EMPTY_GROUP);
    var method3 = new Method(publicModifier, "method3", voidType, List.of(), descriptor, Call.Group.EMPTY_GROUP);
    var method3Bis = new Method(publicModifier, "method3Bis", voidType, List.of(), descriptor, Call.Group.EMPTY_GROUP);
    var method4 = new Method(publicModifier, "method4", voidType, List.of(), descriptor, Call.Group.EMPTY_GROUP);
    var method5 = new Method(publicModifier, "method5", voidType, List.of(), descriptor, Call.Group.EMPTY_GROUP);
    var method6 = new Method(publicModifier, "method6", voidType, List.of(), descriptor, Call.Group.EMPTY_GROUP);
    var method7 = new Method(publicModifier, "method7", voidType, List.of(), descriptor, Call.Group.EMPTY_GROUP);
    var alice = new Entity(
            Set.of(),
            new TypeInfo(Optional.empty(), "Alice", List.of()),
            Stereotype.CLASS,
            List.of(),
            List.of(method2, method3, method3Bis, method7)
    );
    var bob = new Entity(
            Set.of(),
            new TypeInfo(Optional.empty(), "Bob", List.of()),
            Stereotype.CLASS,
            List.of(),
            List.of(method1, method5)
    );
    var charlie = new Entity(
            Set.of(),
            new TypeInfo(Optional.empty(), "Charlie", List.of()),
            Stereotype.CLASS,
            List.of(),
            List.of(method4, method6)
    );
    var writer = new StringWriter();
    var generator = new PlantUmlGenerator();
    generator.generateSequenceDiagram(true, method1,
            new Package("", List.of(alice, bob, charlie), List.of()),
            writer
    );
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
            Bob -> Alice: method3Bis()
            end
            @enduml
            """, writer.toString());
  }
}

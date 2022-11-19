package com.github.forax.umldoc.integration;

import com.github.forax.umldoc.classfile.ModuleScrapper;
import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.gen.MermaidGenerator;
import com.github.forax.umldoc.gen.PlantUmlGenerator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.Comparator;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UmlDocIT {
  @Test
  public void generateMermaid() throws IOException {
    var path = Path.of("target/classes");
    var finder = ModuleFinder.of(path);
    var moduleRef = finder.find(Entity.class.getModule().getName()).orElseThrow();
    var packageMap = ModuleScrapper.scrapModule(moduleRef).stream()
        .collect(toMap(Package::name, identity()));
    System.out.println(packageMap.keySet());
    var core = packageMap.get(Entity.class.getPackageName());
    assertNotNull(core);
    assertEquals(Entity.class.getPackageName(), core.name());

    var entities = core.entities().stream()
        .sorted(Comparator.comparing(e -> e.type().name()))
        .toList();
    var dependencies = core.dependencies().stream()
        .sorted(Comparator.comparing(Dependency::toString))  // FIXME
        .toList();
    var writer = new StringWriter();
    new MermaidGenerator().generate(true, entities, dependencies, writer);
    assertEquals(
        """
        classDiagram
        direction TB

        class AssociationDependency {
          <<record>>
        }

        class AssociationDependency_Cardinality {
          <<enumeration>>
          ONLY_ONE
          ZERO_OR_ONE
          MANY
        }

        class AssociationDependency_Side {
          <<record>>
          java.util.Optional<java.lang.String> label
          boolean navigability
        }

        class Dependency {
          <<interface>>
        }

        class Entity {
          <<record>>
        }

        class Entity_Stereotype {
          <<enumeration>>
          CLASS
          INTERFACE
          ANNOTATION
          ENUM
          RECORD
          ABSTRACT
        }

        class Field {
          <<record>>
          java.lang.String name
        }

        class Method {
          <<record>>
          java.lang.String name
        }

        class Method_Parameter {
          <<record>>
          java.lang.String name
        }

        class Modifier {
          <<enumeration>>
          PRIVATE
          PACKAGE
          PROTECTED
          PUBLIC
          STATIC
          FINAL
        }

        class Package {
          <<record>>
          java.lang.String name
        }

        class SubtypeDependency {
          <<record>>
        }

        class TypeInfo {
          <<record>>
          java.lang.String name
        }

        AssociationDependency_Side --> "1" AssociationDependency_Cardinality : cardinality
        AssociationDependency_Side --> "1" Entity : entity
        AssociationDependency --> "1" AssociationDependency_Side : left
        AssociationDependency --> "1" AssociationDependency_Side : right
        Entity --> "1" Entity_Stereotype : stereotype
        Entity --> "*" Field : fields
        Entity --> "*" Method : methods
        Entity --> "*" Modifier : modifiers
        Entity --> "1" TypeInfo : type
        Field --> "*" Modifier : modifiers
        Field --> "1" TypeInfo : typeInfo
        Method_Parameter --> "1" TypeInfo : typeInfo
        Method --> "*" Method_Parameter : parameters
        Method --> "*" Modifier : modifiers
        Method --> "1" TypeInfo : returnTypeInfo
        Package --> "*" Dependency : dependencies
        Package --> "*" Entity : entities
        SubtypeDependency --> "1" Entity : subtype
        SubtypeDependency --> "1" Entity : supertype
        TypeInfo --> "0..1" TypeInfo : outer
        TypeInfo --> "*" TypeInfo : typeParameters
        """,
        writer.toString());
  }

  @Test
  public void generatePlantUml() throws IOException {
    var path = Path.of("target/classes");
    var finder = ModuleFinder.of(path);
    var moduleRef = finder.find(Entity.class.getModule().getName()).orElseThrow();
    var packageMap = ModuleScrapper.scrapModule(moduleRef).stream()
        .collect(toMap(Package::name, identity()));
    System.out.println(packageMap.keySet());
    var core = packageMap.get(Entity.class.getPackageName());
    assertNotNull(core);
    assertEquals(Entity.class.getPackageName(), core.name());

    var entities = core.entities().stream()
        .sorted(Comparator.comparing(e -> e.type().name()))
        .toList();
    var dependencies = core.dependencies().stream()
        .sorted(Comparator.comparing(Dependency::toString))  // FIXME
        .toList();
    var writer = new StringWriter();
    new PlantUmlGenerator().generate(true, entities, dependencies, writer);
    assertEquals(
        """
        @startuml

        class com.github.forax.umldoc.core.AssociationDependency {
        }

        enum com.github.forax.umldoc.core.AssociationDependency$Cardinality {
          ONLY_ONE
          ZERO_OR_ONE
          MANY
        }

        class com.github.forax.umldoc.core.AssociationDependency$Side {
          java.util.Optional<java.lang.String> label
          boolean navigability
        }

        interface com.github.forax.umldoc.core.Dependency {
        }

        class com.github.forax.umldoc.core.Entity {
        }

        enum com.github.forax.umldoc.core.Entity$Stereotype {
          CLASS
          INTERFACE
          ANNOTATION
          ENUM
          RECORD
          ABSTRACT
        }

        class com.github.forax.umldoc.core.Field {
          java.lang.String name
        }

        class com.github.forax.umldoc.core.Method {
          java.lang.String name
        }

        class com.github.forax.umldoc.core.Method$Parameter {
          java.lang.String name
        }

        enum com.github.forax.umldoc.core.Modifier {
          PRIVATE
          PACKAGE
          PROTECTED
          PUBLIC
          STATIC
          FINAL
        }

        class com.github.forax.umldoc.core.Package {
          java.lang.String name
        }

        class com.github.forax.umldoc.core.SubtypeDependency {
        }

        class com.github.forax.umldoc.core.TypeInfo {
          java.lang.String name
        }

        com.github.forax.umldoc.core.AssociationDependency$Side --> "1" com.github.forax.umldoc.core.AssociationDependency$Cardinality : cardinality
        com.github.forax.umldoc.core.AssociationDependency$Side --> "1" com.github.forax.umldoc.core.Entity : entity
        com.github.forax.umldoc.core.AssociationDependency --> "1" com.github.forax.umldoc.core.AssociationDependency$Side : left
        com.github.forax.umldoc.core.AssociationDependency --> "1" com.github.forax.umldoc.core.AssociationDependency$Side : right
        com.github.forax.umldoc.core.Entity --> "1" com.github.forax.umldoc.core.Entity$Stereotype : stereotype
        com.github.forax.umldoc.core.Entity --> "*" com.github.forax.umldoc.core.Field : fields
        com.github.forax.umldoc.core.Entity --> "*" com.github.forax.umldoc.core.Method : methods
        com.github.forax.umldoc.core.Entity --> "*" com.github.forax.umldoc.core.Modifier : modifiers
        com.github.forax.umldoc.core.Entity --> "1" com.github.forax.umldoc.core.TypeInfo : type
        com.github.forax.umldoc.core.Field --> "*" com.github.forax.umldoc.core.Modifier : modifiers
        com.github.forax.umldoc.core.Field --> "1" com.github.forax.umldoc.core.TypeInfo : typeInfo
        com.github.forax.umldoc.core.Method$Parameter --> "1" com.github.forax.umldoc.core.TypeInfo : typeInfo
        com.github.forax.umldoc.core.Method --> "*" com.github.forax.umldoc.core.Method$Parameter : parameters
        com.github.forax.umldoc.core.Method --> "*" com.github.forax.umldoc.core.Modifier : modifiers
        com.github.forax.umldoc.core.Method --> "1" com.github.forax.umldoc.core.TypeInfo : returnTypeInfo
        com.github.forax.umldoc.core.Package --> "*" com.github.forax.umldoc.core.Dependency : dependencies
        com.github.forax.umldoc.core.Package --> "*" com.github.forax.umldoc.core.Entity : entities
        com.github.forax.umldoc.core.SubtypeDependency --> "1" com.github.forax.umldoc.core.Entity : subtype
        com.github.forax.umldoc.core.SubtypeDependency --> "1" com.github.forax.umldoc.core.Entity : supertype
        com.github.forax.umldoc.core.TypeInfo --> "0..1" com.github.forax.umldoc.core.TypeInfo : outer
        com.github.forax.umldoc.core.TypeInfo --> "*" com.github.forax.umldoc.core.TypeInfo : typeParameters
        @enduml
        """,
        writer.toString());
  }
}
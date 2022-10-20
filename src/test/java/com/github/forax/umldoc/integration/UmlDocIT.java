package com.github.forax.umldoc.integration;

import com.github.forax.umldoc.classfile.ModuleScrapper;
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
import java.util.List;

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
    var writer = new StringWriter();
    new MermaidGenerator().generate(true, entities, List.of(), writer);
    assertEquals(
        """
        classDiagram
            direction TB

            class com.github.forax.umldoc.core.AssociationDependency {
            }

            class com.github.forax.umldoc.core.AssociationDependency$Cardinality {
            }

            class com.github.forax.umldoc.core.AssociationDependency$Side {
            }

            class com.github.forax.umldoc.core.Dependency {
            }

            class com.github.forax.umldoc.core.Entity {
            }

            class com.github.forax.umldoc.core.Entity$Stereotype {
            }

            class com.github.forax.umldoc.core.Field {
            }

            class com.github.forax.umldoc.core.Method {
            }

            class com.github.forax.umldoc.core.Method$Parameter {
            }

            class com.github.forax.umldoc.core.Modifier {
            }

            class com.github.forax.umldoc.core.Package {
            }

            class com.github.forax.umldoc.core.SubtypeDependency {
            }

            class com.github.forax.umldoc.core.TypeInfo {
            }

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
    var writer = new StringWriter();
    new PlantUmlGenerator().generate(true, entities, List.of(), writer);
    assertEquals(
        """
        @startuml

            class com.github.forax.umldoc.core.AssociationDependency {
            }

            class com.github.forax.umldoc.core.AssociationDependency$Cardinality {
            }

            class com.github.forax.umldoc.core.AssociationDependency$Side {
            }

            class com.github.forax.umldoc.core.Dependency {
            }

            class com.github.forax.umldoc.core.Entity {
            }

            class com.github.forax.umldoc.core.Entity$Stereotype {
            }

            class com.github.forax.umldoc.core.Field {
            }

            class com.github.forax.umldoc.core.Method {
            }

            class com.github.forax.umldoc.core.Method$Parameter {
            }

            class com.github.forax.umldoc.core.Modifier {
            }

            class com.github.forax.umldoc.core.Package {
            }

            class com.github.forax.umldoc.core.SubtypeDependency {
            }

            class com.github.forax.umldoc.core.TypeInfo {
            }

        @enduml
        """,
        writer.toString());
  }
}
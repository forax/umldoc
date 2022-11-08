package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.classfile.ModuleScrapper.CardinalityInfo;
import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.AssociationDependency.Cardinality;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.SubtypeDependency;
import com.github.forax.umldoc.core.TypeInfo;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.*;

public class ModuleScrapperTest {
  @Test
  public void scrapModule() throws IOException {
    var path = Path.of("target/classes");
    var finder = ModuleFinder.of(path);
    for(var moduleReference: finder.findAll()) {
      var packageList = ModuleScrapper.scrapModule(moduleReference);
      var classFilePackage = packageList.stream()
          .filter(p -> p.name().equals(ModuleScrapper.class.getPackageName()))
          .findFirst().orElseThrow();

      var moduleScrapperEntity = classFilePackage.entities().stream()
          .filter(e -> e.type().name().equals(ModuleScrapper.class.getName()))
          .findFirst().orElseThrow();
      assertEquals(ModuleScrapper.class.getName(), moduleScrapperEntity.type().name());
    }
  }

  @Test
  public void scrapCore() throws IOException {
    var path = Path.of("target/classes");
    var finder = ModuleFinder.of(path);
    for (var moduleReference: finder.findAll()) {
      var packageList = ModuleScrapper.scrapModule(moduleReference);
      var corePackage = packageList.stream()
          .filter(p -> p.name().equals(SubtypeDependency.class.getPackageName()))
          .findFirst().orElseThrow();

      var entity = corePackage.entities().stream()
          .filter(sd -> sd.type().name().equals(Entity.class.getName()))
          .findFirst().orElseThrow();
      assertEquals(Entity.class.getName(), entity.type().name());
      assertEquals(List.of(), entity.fields().stream().map(Field::name).toList());
      
      var dependency = corePackage.dependencies().stream()
          .flatMap(d -> d instanceof AssociationDependency association ? Stream.of(association) : null)
          .filter(association -> association.left().entity() == entity)
          .toList();
      assertEquals(Set.of("fields", "methods", "modifiers", "stereotype", "type"),
          dependency.stream().map(association -> association.right().label().orElseThrow()).collect(toSet()));
    }
  }

  @Test
  public void canNotBeInstantiated() throws InstantiationException, IllegalAccessException {
    var constructor = ModuleScrapper.class.getDeclaredConstructors()[0];
    constructor.setAccessible(true);
    try {
      constructor.newInstance();
      fail();
    } catch (InvocationTargetException e) {
      assertInstanceOf(AssertionError.class, e.getCause());
    }
  }

  @Test
  public void findCardinality() {
    var string = TypeInfo.of("java.lang.String");
    var e = TypeInfo.of("E");
    var listOfString = TypeInfo.of("java.util.List").withTypeParameter(string);
    var setOfString = TypeInfo.of("java.util.Set").withTypeParameter(string);
    var optionalOfString = TypeInfo.of("java.util.Optional").withTypeParameter(string);
    var streamOfString = TypeInfo.of("java.util.stream.Stream").withTypeParameter(string);
    var mapOfEToString = TypeInfo.of("java.util.Map").withTypeParameter(e).withTypeParameter(string);
    assertAll(
        () -> assertEquals(new CardinalityInfo(Cardinality.MANY, string), ModuleScrapper.findCardinality(listOfString)),
        () -> assertEquals(new CardinalityInfo(Cardinality.MANY, string), ModuleScrapper.findCardinality(setOfString)),
        () -> assertEquals(new CardinalityInfo(Cardinality.ZERO_OR_ONE, string), ModuleScrapper.findCardinality(optionalOfString)),
        () -> assertEquals(new CardinalityInfo(Cardinality.MANY, string), ModuleScrapper.findCardinality(streamOfString)),
        () -> assertEquals(new CardinalityInfo(Cardinality.MANY, string), ModuleScrapper.findCardinality(mapOfEToString))
    );

  }
}
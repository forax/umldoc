package com.github.forax.umldoc.classfile;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleScrapperTest {
  @Test
  public void scrapModule() throws IOException {
    var path = Path.of("target/classes");
    var finder = ModuleFinder.of(path);
    for(var moduleReference: finder.findAll()) {
      var packageList = ModuleScrapper.scrapModule(moduleReference);
      var classFilePackage = packageList.stream()
          .filter(packaze -> packaze.name().equals(ModuleScrapper.class.getPackageName()))
          .findFirst().orElseThrow();
      var moduleScrapperEntity = classFilePackage.entities().stream()
          .filter(entity -> entity.name().equals(ModuleScrapper.class.getName()))
          .findFirst().orElseThrow();
      assertEquals(ModuleScrapper.class.getName(), moduleScrapperEntity.name());
    }
  }
}
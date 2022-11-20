package com.github.forax.umldoc.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.gen.Generator;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.junit.jupiter.api.Test;

public class GeneratorConfigurationTest {

  @Test
  public void filterPackageByName() {
    var module = List.of(
            new Package("A", List.of(), List.of()),
            new Package("B", List.of(), List.of()),
            new Package("C", List.of(), List.of())
    );
    var conf = GeneratorConfiguration.filterPackage("A", DummyGenerator.Instance);
    assertEquals("A", conf.filterPackage(module)
                          .name());
  }

  enum DummyGenerator implements Generator {
    Instance;

    @Override
    public void generate(boolean header, List<Entity> entities, List<Dependency> dependencies,
                         Writer writer) throws IOException {}

    @Override
    public void generateSequenceDiagram(boolean header, Entity entryEntity, Method entryPoint,
                                        Package p, Writer writer) throws IOException {}
  }
}

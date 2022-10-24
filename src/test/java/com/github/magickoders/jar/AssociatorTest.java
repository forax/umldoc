package com.github.magickoders.jar;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

class AssociatorTest {

    @Test
    void getDependencies() throws IOException {
        var entities = JarReader.getEntities(Path.of("target", "classes"));
        var associator = new Associator(entities);
        System.out.println(associator.getDependencies().toString().replace("AssociationDependency[", "\n  AssociationDependency["));
    }
}
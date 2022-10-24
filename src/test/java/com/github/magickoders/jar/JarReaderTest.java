package com.github.magickoders.jar;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JarReaderTest {

    @Test
    public void testGetEntitiesNotNull() {
        try {
            var entities = JarReader.getEntities(Path.of("target", "classes"));
            System.out.println(entities);
            assertNotNull(entities);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

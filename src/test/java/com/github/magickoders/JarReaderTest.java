package com.github.magickoders;

import com.github.forax.umldoc.core.Modifier;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JarReaderTest {

    @Test
    public void listNotNull() throws IOException {
        var entities = JarReader.getEntities().stream()
                .filter(entity -> entity.name().contains("magickoders") /*|| entity.name().contains("forax")*/)
                .toList();

        assertAll(
                () -> assertNotNull(entities),
                () -> assertFalse(entities.isEmpty())
        );
    }

    @Test
    public void entitiesOk() throws IOException {
        var entities = JarReader.getEntities().stream()
                .filter(entity -> entity.name().contains("magickoders") || entity.name().contains("forax"))
                .toList();

        var entity = entities.stream()
                .filter(e -> e.name().substring(e.name().lastIndexOf(".") + 1).equals("Entity"))
                .findFirst().orElseThrow();
        assertEquals(Set.of(Modifier.PUBLIC), entity.modifiers());
        assertEquals(Optional.of("record"), entity.stereotype());
    }
}

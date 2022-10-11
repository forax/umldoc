package com.github.magickoders;

import com.github.forax.umldoc.core.Entity;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello Adrien et Brian !");

        try {
            var entities = JarReader.getEntities();
            try (var writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8)) {
                var text = entities.stream()
                        .filter(entity -> entity.name().contains("magickoders") /*|| entity.name().contains("forax")*/)
                        .map(Entity::toString)
                        .collect(Collectors.joining("\n"));

                writer.append(text);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

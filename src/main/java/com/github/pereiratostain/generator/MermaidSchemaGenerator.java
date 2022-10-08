package com.github.pereiratostain.generator;

import com.github.forax.umldoc.core.Entity;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class MermaidSchemaGenerator implements Generator {

    private final Writer writer;

    public MermaidSchemaGenerator(Writer writer) {
        requireNonNull(writer);

        this.writer = writer;
    }

    @Override
    public void generate(List<Entity> entities) throws IOException {
        requireNonNull(entities);

        generateHeader();
        for (var entity : entities) {
            generateEntity(entity);
        }
    }

    private void generateHeader() throws IOException {
        writer.append("""
            classDiagram
                direction TB

            """);
    }

    private void generateEntity(Entity entity) throws IOException {
        writer.append("    class ")
                .append(entity.name())
                .append(" {")
                .append("\n    }\n\n");
    }
}

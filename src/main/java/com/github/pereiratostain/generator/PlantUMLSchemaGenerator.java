package com.github.pereiratostain.generator;

import com.github.forax.umldoc.core.Entity;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class PlantUMLSchemaGenerator implements Generator {

    private final Writer writer;

    public PlantUMLSchemaGenerator(Writer writer) {
        requireNonNull(writer);

        this.writer = writer;
    }

    @Override
    public void generate(List<Entity> entities) throws IOException {
        writer.append("@startuml\n\n");
        for (var entity : entities) {
            generateEntity(entity);
        }
        writer.append("@enduml");
    }

    private void generateEntity(Entity entity) throws IOException {
        writer.append("    class ")
                .append(entity.name())
                .append(" {")
                .append("\n    }\n\n");
    }
}

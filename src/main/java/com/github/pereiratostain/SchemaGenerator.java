package com.github.pereiratostain;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

public final class SchemaGenerator {

    public void writeLines(Writer writer, List<String> lines) throws IOException {
        Objects.requireNonNull(writer);
        Objects.requireNonNull(lines);

        for (var line : lines) {
            writer.append(line);
        }
        writer.flush();
    }
}

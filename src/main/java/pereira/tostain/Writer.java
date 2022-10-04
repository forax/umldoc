package pereira.tostain;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class Writer {

    public void writeLines(Writer writer, List<String> lines) throws IOException {
        Objects.requireNonNull(writer);
        Objects.requireNonNull(lines);

        writer.writeLines(writer, lines);
    }
}

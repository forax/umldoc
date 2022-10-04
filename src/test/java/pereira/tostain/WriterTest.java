package pereira.tostain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WriterTest {

    @Test
    public void writerPreconditions() {
        var writer = new Writer();
        assertAll(
            () -> assertThrows(NullPointerException.class, () -> writer.writeLines(null, List.of("test")))
        );
    }
}

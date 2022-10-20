package com.github.magickoders;

import com.github.forax.umldoc.core.Package;
import com.github.magickoders.jar.JarReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * This is the main class.
 */
public class Main {

  private static void exitWithError(String errorMessage) {
    System.err.println(errorMessage);
    System.exit(1);
  }

  private static void writeToPath(Path path, String content) throws IOException {
    try (var writer = Files.newBufferedWriter(path)) {
      writer.write(content);
    }
  }

  private static void convertFile(Parser parser, Path jarPath, Path outputPath) throws IOException {
    var entities = JarReader.getEntities(jarPath);
    var result = parser.parse(new Package("no_name_yet", entities, List.of()));
    writeToPath(outputPath, result);
  }

  /**
   * The entry point of the program.
   *
   * @param args
   *         Three arguments are required, the format of the output (either MERMAID or PLANT_UML),
   *         the path of the jar to convert, and the path for the output.
   */
  public static void main(String[] args) throws IOException {
    if (args.length != 3) {
      exitWithError("exactly three argument are needed");
    }

    var parser = OutputFormat.valueOf(args[0])
                             .getParser();
    var jarPath = Path.of(args[1]);
    var outputPath = Path.of(args[2]);

    convertFile(parser, jarPath, outputPath);
  }

  /**
   * Entry point for mermaid only conversion.
   *
   * @param args
   *         two arguments are expected: the path of the jar to convert and the path for the
   *         output.
   */
  public static void mermaid(String[] args) throws IOException {
    if (args.length != 2) {
      exitWithError("Only two argument are accepted");
    }

    var jarPath = Path.of(args[0]);
    var outputPath = Path.of(args[1]);
    var parser = new MermaidParser();
    convertFile(parser, jarPath, outputPath);
  }

  /**
   * Entry point for plant uml only conversion.
   *
   * @param args
   *         two arguments are expected: the path of the jar to convert and the path for the
   *         output.
   */
  public static void plantUml(String[] args) throws IOException {
    if (args.length != 2) {
      exitWithError("Only two argument are accepted");
    }

    var jarPath = Path.of(args[0]);
    var outputPath = Path.of(args[1]);
    var parser = new PlantumlParser();
    convertFile(parser, jarPath, outputPath);
  }


  private enum OutputFormat {
    MERMAID(MermaidParser::new), PLANT_UML(PlantumlParser::new);

    private final Supplier<Parser> parserSupplier;

    OutputFormat(Supplier<Parser> parserSupplier) {
      Objects.requireNonNull(parserSupplier);
      this.parserSupplier = parserSupplier;
    }

    public Parser getParser() {
      return parserSupplier.get();
    }
  }

}
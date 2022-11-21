package com.github.forax.umldoc.editor;

import com.github.forax.umldoc.gen.PlantUmlGenerator;
import java.util.Optional;
import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * This class is used to parse the command line for the PlantUML export.
 *
 */
public class PlantCmdLineParser implements CommandLineParser {

  private boolean isStartUmlAnnotationPresent = false;


  /**
   * This class is used to filter the options given in the command line.
   */
  public static class OptionsCmd implements Runnable {
    @Option(names = "-p", description = "package Name")
    public String packageName;

    @Option(names = "-t", description = "test Name")
    public String test;


    @Override
    public void run() {

    }
  }

  /**
   * This method parse each line, by detecting if it starts with the begin sequence :
   *  --> "` umldoc" for the PlantUML.
   *
   * @param line the line to parse
   * @return an Optional of GeneratorConfiguration.
   */
  public Optional<GeneratorConfiguration> parseLine(String line) {
    if (!isStartUmlAnnotationPresent) {
      isStartUmlAnnotationPresent = isStartAnnotationPresent(line);
    } else if (isStartingLine(line) && isStartUmlAnnotationPresent) {
      var options = new OptionsCmd();
      var args = line.substring("` umldoc".length() + 1).split(" ");
      new CommandLine(options).execute(args);
      var generator = GeneratorConfiguration
              .filterPackage(options.packageName, new PlantUmlGenerator());
      return Optional.of(generator);
    }
    return Optional.empty();
  }


  public boolean endLine(String line) {
    return line.equals("@enduml");
  }

  private boolean isStartingLine(String line) {
    return line.startsWith("' umldoc");
  }

  private boolean isStartAnnotationPresent(String line) {
    return line.startsWith("@startuml");
  }

  public static void main(String[] args) {
    var test = new PlantCmdLineParser();
    test.parseLine("` umldoc -p nameThomas -t test");
  }


}

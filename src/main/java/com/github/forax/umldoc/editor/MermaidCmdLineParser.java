package com.github.forax.umldoc.editor;

import com.github.forax.umldoc.gen.MermaidGenerator;
import java.util.Optional;
import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * This class is used to parse the command line for the mermaid export.
 *
 */
public class MermaidCmdLineParser implements CommandLineParser {

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
   * This method parse each line, by detecting if it start with the begin sequence :
   *  --> "%% umldoc" for the Mermaid.
   *
   * @param line the line to parse
   * @return an Optional of GeneratorConfiguration.
   */
  public Optional<GeneratorConfiguration> parseLine(String line) {
    if (isStartingLine(line)) {


      var options = new OptionsCmd();
      var args = line.substring("%% umldoc".length() + 1).split(" ");
      new CommandLine(options).execute(args);
      var generator = GeneratorConfiguration
              .filterPackage(options.packageName, new MermaidGenerator());
      return Optional.of(generator);
    }

    return Optional.empty();
  }


  public boolean endline(String line) {
    return line.equals("```"); //to replace with "@enduml" for PlantUML
  }

  private boolean isStartingLine(String line) {
    return line.startsWith("%% umldoc");
  }

  public static void main(String[] args) {
    var test = new MermaidCmdLineParser();
    test.parseLine("%% umldoc -p nameThomas -t test");
  }


}

package com.github.forax.umldoc.editor;

import com.github.forax.umldoc.gen.MermaidGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import picocli.CommandLine;
import picocli.CommandLine.Option;

public class MermaidCmdLineParser implements CommandLineParser{
  private boolean hasGenerationStarted = false;


  public static class OptionsCmd implements Runnable {
    @Option(names = "-p", description = "package Name" )
    public String packageName;

    @Option(names = "-t", description = "test Name" )
    public String test;


    @Override
    public void run() {

    }
  }






  public Optional<GeneratorConfiguration> parseLine(String line){
    if (isStartingLine(line)){
      this.hasGenerationStarted = true;

      var options = new OptionsCmd();
      var args = line.substring("%% umldoc ".length()).split(" ");
      new CommandLine(options).execute(args);
      GeneratorConfiguration.filterPackage(options.packageName, new MermaidGenerator());
      return Optional.empty();
    }

    return Optional.empty();
  }


  public boolean endline (String line){
    return line.equals("```") && hasGenerationStarted; //to replace with "@enduml" for PlantUML
  }

  private boolean isStartingLine(String line){
    return line.startsWith("%% umldoc");
  }

  public static void main(String[] args) {
    var test = new MermaidCmdLineParser();
    test.parseLine("%% umldoc -p nameThomas -t test");
  }


}

package com.github.forax.umldoc.editor;

import java.util.Optional;

public class MermaidCmdLineParser implements CommandLineParser{
  private boolean hasGenerationStarted = false;

  public MermaidCmdLineParser(){

  }

  public Optional<GeneratorConfiguration> parseLine (String line){
    if (isStartingLine(line)){
      this.hasGenerationStarted = true;


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


}

package com.github.veluvexiau.umldoc;

import static com.github.veluvexiau.umldoc.classfile.ClassFileParser.readJarFile;

import com.github.veluvexiau.umldoc.core.MermaidExtract;
import com.github.veluvexiau.umldoc.core.PlantUmlExtract;
import java.io.IOException;
import java.nio.file.Path;

/**
 * The main class to export UML in Mermaid and PlantUml.
 */
public class Main {
  /**
   * The main method to export UML in mermaid and PlantUml.
   */
  public static void main(String[] args) throws IOException {
    /* if (args.length<1){
        throw new IllegalStateException("no path specified");
    }*/
    var path = Path.of("target/classes");
    var entityFromJar = readJarFile(path);

    MermaidExtract mermaid = new MermaidExtract();
    PlantUmlExtract plantUml = new PlantUmlExtract();

    mermaid.generate(entityFromJar);
    plantUml.generate(entityFromJar);
  }
}

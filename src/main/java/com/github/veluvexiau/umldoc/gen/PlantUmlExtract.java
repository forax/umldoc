package com.github.veluvexiau.umldoc.gen;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * Class to extract to a PlantUml file.
 */
public class PlantUmlExtract {

  /**
   * Method to create the PlantUml.
   *
   * @param entities list of entity do add in Mermaid
   * @throws IOException by the PrintWriter
   */
  public void generate(List<Entity> entities) throws IOException {
    Objects.requireNonNull(entities);
    String pathToString = "./src/main/java/com/github/veluvexiau/umldoc/core/plantExport.md";
    try (PrintWriter writer = new PrintWriter(pathToString, Charset.defaultCharset())) {
      init(writer);
      for (Entity entity : entities) {
        displayEntity(writer, entity);
      }
      end(writer);
    }
  }

  private void init(PrintWriter writer) {
    writer.println("```plantuml");
    writer.println("@startuml");
    writer.println("' umldoc");
  }

  private void end(PrintWriter writer) {
    writer.println("@enduml");
    writer.println("```");
    writer.close();
  }

  private void displayEntity(PrintWriter writer, Entity entity) {
    writer.println("\tclass " + ExtractMethod.getNameFromPath(entity.type().toString()) + "{");
    var stereo = ExtractMethod.getStereotype(entity.stereotype().toString());
    if (!stereo.equals("")) {
      writer.println("\t\t<<" + stereo + ">>");
    }
    for (Field field : entity.fields()) {
      writer.println("\t\t" + field.typeInfo().toString() + " : " + field.name());
    }
    for (Method method : entity.methods()) {
      writer.println("\t\t" + method.name());
    }
    writer.println("\t}\n");
  }

}

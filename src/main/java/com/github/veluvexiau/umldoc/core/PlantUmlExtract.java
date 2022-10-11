package com.github.veluvexiau.umldoc.core;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Class to extract to a PlantUml file.
 */
public class PlantUmlExtract implements Extract {
  @Override
  public void generate(List<Entity> entities) throws IOException {
    String pathToString = "./src/main/java/com/github/veluvexiau/umldoc/core/plantExport.md";
    PrintWriter writer = new PrintWriter(pathToString, Charset.defaultCharset());
    init(writer);
    for (Entity entitie : entities) {
      displayEntity(writer, entitie);
    }
    end(writer);

  }

  @Override
  public void init(PrintWriter writer) {
    writer.println("```plantuml");
    writer.println("@startuml");
    writer.println("' umldoc");
  }

  @Override
  public void end(PrintWriter writer) {
    writer.println("@enduml");
    writer.println("```");
    writer.close();
  }

  @Override
  public void displayEntity(PrintWriter writer, Entity entity) {
    writer.println("\tclass " + getNameFromPath(entity.name()) + "{");
    var stereo = getStereotype(entity.stereotype().toString());
    if (!stereo.equals("")) {
      writer.println("\t\t<<" + stereo + ">>");
    }
    for (Field field : entity.fields()) {
      writer.println("\t\t" + field.type() + " : " + field.name());
    }
    for (Method method : entity.methods()) {
      writer.println("\t\t" + method.name());
    }
    writer.println("\t}\n");
  }

}

package com.github.veluvexiau.umldoc.core;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * Class to extract to a Mermaid File.
 */
public class MermaidExtract {

  /**
   * Method to create the Mermaid.
   *
   * @param entities list of entity do add in Mermaid
   * @throws IOException by the PrintWriter
   */
  public void generate(List<Entity> entities) throws IOException {
    Objects.requireNonNull(entities);
    String pathToString = "./src/main/java/com/github/veluvexiau/umldoc/core/marmaidExport.md";
    PrintWriter writer = new PrintWriter(pathToString, Charset.defaultCharset());
    init(writer);
    for (Entity entitie : entities) {
      displayEntity(writer, entitie);
    }
    end(writer);
  }


  private void init(PrintWriter writer) {
    writer.println("```mermaid");
    writer.println("%% umldoc");
    writer.println("classDiagram\n\tdirection TB");
  }

  private void end(PrintWriter writer) {
    writer.println("```");
    writer.close();
  }

  private void displayEntity(PrintWriter writer, Entity entity) {
    writer.println("\tclass " + ExtractMethods.getNameFromPath(entity.name()) + "{");

    if (entity.stereotype() != Entity.Stereotype.CLASS) {
      writer.println("\t\t<<" + entity.stereotype().toString() + ">>");
    }
    for (Field field : entity.fields()) {
      writer.println("\t\t" + field.type() + " : " + field.name());
    }



    for (Method method : entity.methods()) {
      StringBuilder stringBuilder = new StringBuilder();


      writer.println("\t\t" + method.name()+"("+method.parameters().stream().map(Method.Parameter::name).collect(Collectors.joining(", "))+") : "+method.returnType());
    }
    writer.println("\t}\n");
  }
}


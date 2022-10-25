package com.github.veluvexiau.umldoc.gen;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;


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
    try (PrintWriter writer = new PrintWriter(pathToString, Charset.defaultCharset())) {
      header(writer);
      for (Entity entity : entities) {
        displayEntity(writer, entity);
      }
      end(writer);
    }
  }

  private void header(PrintWriter writer) {
    writer.println("```mermaid");
    writer.println("%% umldoc");
    writer.println("classDiagram\n\tdirection TB");
  }

  private void end(PrintWriter writer) {
    writer.println("```");
    writer.close();
  }

  private void displayEntity(PrintWriter writer, Entity entity) {
    writer.println("\tclass " + ExtractMethod.getNameFromPath(entity.type().toString()) + "{");
    if (entity.stereotype() != Entity.Stereotype.CLASS) {
      writer.println("\t\t<<" + entity.stereotype().toString() + ">>");
    }
    for (Field field : entity.fields()) {
      writer.println("\t\t" + field.typeInfo().toString() + " : " + field.name());
    }
    writer.println(methodsAndParameters(entity) + "\n\t}");
  }

  // TODO : The type is OK, but if it is Set<> or List<>, the type is only Set and not Set<Type>
  // EX : method(String, Set)
  private String methodsAndParameters(Entity entity) {
    var sb = new StringBuilder();
    for (var method : entity.methods()) {
      sb.append("\t\t")
              .append(ExtractMethod.typeOfList(method))
              // TODO : after that, the return type is the Path of the class.
              // EX : ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>
              .append(" : ")
              .append(method.returnTypeInfo())
              .append("\n");
    }
    return sb.toString();
  }
}


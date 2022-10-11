package com.github.veluvexiau.umldoc.core;

import com.github.forax.umldoc.core.Entity;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interface to build a mermaid or plant file in Markdown.
 */
public interface Extract {
  void init(PrintWriter w);

  void end(PrintWriter w);

  void displayEntity(PrintWriter writer, Entity entity);

  void generate(List<Entity> entities) throws IOException;

  /**
   * Get the name from a full path.
   *
   * @param path the full path
   * @return only the name
   */
  default String getNameFromPath(String path) {
    Objects.requireNonNull(path);
    String name = path;
    int temp = name.lastIndexOf('/');
    name = name.substring(temp + 1);
    //In the case of a record inside another record for example.
    Pattern p = Pattern.compile("\\$");
    Matcher m = p.matcher(name);
    if (m.find()) {
      name = m.replaceAll("_");
    }
    return name;
  }

  /**
   * Get the stereotype from the superType.
   *
   * @param entityStereotype the full path of the supertype
   * @return the stereotype
   */
  default String getStereotype(String entityStereotype) {
    Objects.requireNonNull(path);
    String ster = entityStereotype;
    int temp = ster.lastIndexOf('/');
    ster = ster.substring(temp + 1);

    if (ster.contains("Record")) {
      return "Record";
    } else if (ster.contains("Enum")) {
      return "Enum";
    }
    //TODO find a way to detect difference between Interface and Class, to add Interface as
    return "";
  }
}

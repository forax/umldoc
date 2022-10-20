package com.github.veluvexiau.umldoc.gen;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interface to build a mermaid or plant file in Markdown.
 */
public class ExtractMethods {

  /**
   * Get the name from a full path.
   *
   * @param path the full path
   * @return only the name
   */
  public static String getNameFromPath(String path) {
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
  public static String getStereotype(String entityStereotype) {
    Objects.requireNonNull(entityStereotype);
    String ster = entityStereotype;
    int temp = ster.lastIndexOf('/');
    ster = ster.substring(temp + 1);

    if (ster.contains("Record")) {
      return "Record";
    } else if (ster.contains("Enum")) {
      return "Enum";
    }
    //TODO find a way to detect difference between Interface and Class, to add Interface as
    // Test result : class will always have <init> as a method, not for the interface
    return "";
  }
}

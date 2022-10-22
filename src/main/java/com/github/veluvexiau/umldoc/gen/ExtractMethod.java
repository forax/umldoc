package com.github.veluvexiau.umldoc.gen;

import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class to share multiple methods to generate the PlantUml or MermaidUml.
 */
public class ExtractMethod {

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
    String stereotype = entityStereotype;
    int temp = stereotype.lastIndexOf('/');
    stereotype = stereotype.substring(temp + 1);

    if (stereotype.contains("Record")) {
      return "Record";
    } else if (stereotype.contains("Enum")) {
      return "Enum";
    }
    //TODO find a way to detect difference between Interface and Class, to add Interface as
    // Test result : class will always have <init> as a method, not for the interface
    return "";
  }

  /**
   * Compute the type of the List, Set, etc.
   * Returns a string containing all the parameter or nothing
   *
   * @param method a method
   * @return nothing or a formatted list of the types
   */
  public static String typeOfList(Method method) {
    Objects.requireNonNull(method);
    return method.name() +
      method.parameters()
        .stream()
        .map(e -> parameterWithType(e.typeInfo()))
        .collect(Collectors.joining(", ", "(", ")"));
  }

  // TODO : Right now the type is ok, but if it is a List or a Set we don't know the type.

  private static String parameterWithType(TypeInfo info) {
    var sb = new StringBuilder();
    sb.append(info.name());
    var type = info.typeParameters();
    if (type.size() != 0) {
      sb.append(type.stream()
                      .map(TypeInfo::toString)
                      .collect(Collectors.joining(", ", "<", ">")));
    }
    return sb.toString();
  }
}

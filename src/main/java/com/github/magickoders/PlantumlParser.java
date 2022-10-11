package com.github.magickoders;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Package;
import java.util.stream.Collectors;

/**
 * Converts a Package into as a plantuml formatted String.
 */
public class PlantumlParser implements Parser {

  private String parseField(Field f) {
    return f.type() + "" + f.name();
  }

  private String parseEntity(Entity e) {
    var fields = e.fields()
                  .stream()
                  .map(this::parseField)
                  .collect(Collectors.joining("\n\t\t", "\n\t\t", ""));
    return "class " + e.name() + " {" + fields + "\n\t}";
  }

  @Override
  public String parse(Package p) {
    var entities = p.entities()
                    .stream()
                    .map(this::parseEntity)
                    .collect(Collectors.joining("\n\t", "\n\t", ""));
    return "@startuml" + entities + "\n@enduml";
  }

}

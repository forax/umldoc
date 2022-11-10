package com.github.forax.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class SequenceDiagramPlantUmlGenerator implements Generator {
  private static String groupKind(Call.Group.Kind groupKind) {
    return switch (groupKind) {
      case LOOP -> "loop";
      case OPTIONAL -> "opt";
      case ALTERNATE -> "alt";
      case NONE, PARALLEL -> "group";
    };
  }

  private static void generateMethodCalls(List<Call.MethodCall> methodCalls, Writer writer)
          throws IOException {
//    System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
//    System.out.println("source : " + source.type().name());
//    System.out.println("dest : " + methodCall.type().name());

    for(var i = 0; i < methodCalls.size() - 1; i++) {
      var source = methodCalls.get(i);
      var dest = methodCalls.get(i + 1);
      writer.append(source.type().name())
              .append(" -> ")
              .append(dest.type().name())
              .append(": ")
              .append(source.name());
    }


    // Alice -> Bob: method name
    // TODO activate Bob

    // Bob -> Alice: method name
    // TODO deactivate Bob
  }

  private static void generateCall(Call.Group group, Writer writer) throws IOException {
    var methods = new ArrayList<Call.MethodCall>();
    for(var call: group.calls()) {
      switch (call) {
        case Call.MethodCall methodCall -> methods.add(methodCall);
        default -> {}
//        case Call.Group groupCall -> {
//          writer.append(groupKind(group.kind())).append('\n');
//          generateCall(source, groupCall, writer);
//          writer.append('\n').append("end");
//        }
      }
    }
    generateMethodCalls(methods, writer);


    //    writer.append("""
//            %s
//                %s
//            end
//            """.formatted(groupKind(group.kind()), generateMethodCall(methodCall)));
  }

  @Override
  public void generate(boolean header, List<Entity> entities, List<Dependency> dependencies,
                       Writer writer) throws IOException {
    entities = List.copyOf(entities);
    requireNonNull(writer);
    Generator.addHeader(header, writer);

    for(var entity: entities) {
      for(var method: entity.methods()) {
        generateCall(method.callGroup(), writer);
      }
    }

    Generator.addFooter(header, writer);
  }
}

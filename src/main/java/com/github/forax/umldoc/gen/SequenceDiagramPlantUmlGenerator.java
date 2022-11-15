package com.github.forax.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Method;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class SequenceDiagramPlantUmlGenerator implements Generator {
//  private static String groupKind(Call.Group.Kind groupKind) {
//    return switch (groupKind) {
//      case LOOP -> "loop";
//      case OPTIONAL -> "opt";
//      case ALTERNATE -> "alt";
//      case NONE, PARALLEL -> "group";
//    };
//  }

  private static void generateMethodCallForEntity(Entity sourceEntity, Call.MethodCall methodCall, Writer writer)
          throws IOException {

    writer.append(sourceEntity.type().name())
            .append(" -> ")
            .append(methodCall.type().name())
            .append(": ")
            .append(methodCall.name())
            .append("()")
            .append('\n');

    // Alice -> Bob: method name
    // activate Bob

    // Bob -> Alice: method name
    // deactivate Bob
  }

  private static void generateCallsForEntity(Entity sourceEntity, List<Method> methods, Writer writer) throws IOException {
    for(var method: methods) {
      for(var call: method.callGroup().calls()) {
        if(call instanceof Call.MethodCall methodCall && !methodCall.type().equals(sourceEntity.type())) {
          generateMethodCallForEntity(sourceEntity, methodCall, writer);
        } // else if(call instanceof Call.Group groupCall) {
//        writer.append(groupKind(group.kind())).append('\n');
//        generateCall(source, groupCall, writer);
//        writer.append('\n').append("end");
//      } else {
//        throw new IllegalStateException();
//      }
      }
    }


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
      generateCallsForEntity(entity, entity.methods(), writer);
    }

    Generator.addFooter(header, writer);
  }
}

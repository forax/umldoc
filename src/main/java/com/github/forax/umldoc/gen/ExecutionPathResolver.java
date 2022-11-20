package com.github.forax.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.objectweb.asm.Type;

/**
 * Class that compute the execution path (method calls and subsequent method calls).
 */
public class ExecutionPathResolver {
  record ExecutionItem(Entity source, Entity target, Method method) {
    ExecutionItem {
      requireNonNull(source);
      requireNonNull(target);
      requireNonNull(method);
    }
  }

  /**
   * Generate the execution path from a given entry in the program.
   *
   * @param entryEntity the entity defining the entry method
   * @param entryPoint the entry method
   * @param p the package
   * @return a list of {@link ExecutionItem} that represents the execution path
   */
  public static List<ExecutionItem> generateExecutionPath(Entity entryEntity,
                                                          Method entryPoint,
                                                          Package p) {
    requireNonNull(entryEntity);
    requireNonNull(entryPoint);
    requireNonNull(p);
    var entities = p.entities();
    var entryGroup = entryPoint.callGroup();
    var calls = new ArrayList<ExecutionItem>();
    for (var call : entryGroup.calls()) {
      calls.addAll(resolveCallExecution(call, entryEntity, entities));
    }
    return calls;
  }

  static List<ExecutionItem> resolveCallExecution(Call call,
                                                  Entity sourceEntity,
                                                  List<Entity> entities) {
    var callExecution = new ArrayList<ExecutionItem>();
    if (call instanceof Call.MethodCall methodCall) {
      var targetEntity = findEntity(methodCall.ownerName(), entities);
      var targetMethod = findMethodInEntity(methodCall, targetEntity);
      var executionItem = new ExecutionItem(sourceEntity, targetEntity, targetMethod);
      callExecution.add(executionItem);
      var targetCalls = targetMethod.callGroup().calls();
      for (var subCall : targetCalls) {
        callExecution.addAll(resolveCallExecution(subCall, targetEntity, entities));
      }
    } else if (call instanceof Call.Group group) {
      if (group.equals(Call.Group.EMPTY_GROUP)) {
        return List.of();
      }
      var calls = group.calls();
      for (var subCall : calls) {
        callExecution.addAll(resolveCallExecution(subCall, sourceEntity, entities));
      }
    }
    return callExecution;
  }

  private static Entity findEntity(String entityName, List<Entity> entities) {
    return entities.stream()
            .filter(entity -> entity.type().name().equals(entityName))
            .findFirst()
            .orElseThrow();
  }

  private static Method findMethodInEntity(Call.MethodCall methodCall, Entity entity) {
    var descriptor = methodCall.descriptor();
    var methodReturnType = TypeInfo.of(Type.getReturnType(descriptor).getClassName());
    var methodName = methodCall.name();
    var methodParametersType = Arrays.stream(Type.getArgumentTypes(descriptor))
            .map(parameter -> TypeInfo.of(parameter.getClassName()))
            .toList();
    var methods = entity.methods();
    return methods.stream().filter(method -> {
      var returnType = method.returnTypeInfo();
      var parameters = method.parameters().stream()
              .map(Method.Parameter::typeInfo)
              .toList();
      var name = method.name();
      return returnType.equals(methodReturnType)
              && parameters.equals(methodParametersType)
              && name.equals(methodName);
    })
    .findFirst()
    .orElseThrow();
  }
}

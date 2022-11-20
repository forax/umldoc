package com.github.forax.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.ArrayList;
import java.util.List;

public class ExecutionPathResolver {
  record ExecutionItem(Entity source, Entity target, Method method) { }

  public static List<ExecutionItem> generateExecutionPath(Entity entryEntity, Method entryPoint, Package p) {
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

  private static List<ExecutionItem> resolveCallExecution(Call call, Entity sourceEntity, List<Entity> entities) {
    var callExecution = new ArrayList<ExecutionItem>();
    if (call instanceof Call.MethodCall methodCall) {
      var targetType = methodCall.type();
      var targetEntity = findEntity(targetType, entities);
      var targetMethod = findMethodInEntity(methodCall, targetEntity);
      var executionItem = new ExecutionItem(sourceEntity, targetEntity, targetMethod);
      resolveCallExecution(methodCall, targetEntity, entities);
      callExecution.add(executionItem);
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

  private static Entity findEntity(TypeInfo entityType, List<Entity> entities) {
    return entities.stream()
            .filter(entity -> entity.type().equals(entityType))
            .findFirst()
            .orElseThrow();
  }

  private static Method findMethodInEntity(Call.MethodCall methodCall, Entity entity) {
    var methodReturnType = methodCall.returnType();
    var methodName = methodCall.name();
    var methodParametersType = methodCall.parameterTypes();
    var methods = entity.methods();
    return methods.stream().filter(method -> {
              var returnType = method.returnTypeInfo();
              var parameters = method.parameters().stream().map(Method.Parameter::typeInfo).toList();
              var name = method.name();
              return returnType.equals(methodReturnType) &&
                      parameters.equals(methodParametersType) &&
                      name.equals(methodName);
            })
            .findFirst()
            .orElseThrow();
  }
}

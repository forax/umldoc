package com.github.forax.umldoc.gen;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.Package;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
   * @param p           the package that scopes the execution path
   * @param entryEntity the entity which contains the entry method
   * @param entryMethod the method from which the execution path start
   * @return a list of {@link ExecutionItem} that represents the execution path
   */
  public static List<ExecutionItem> generateExecutionPath(Package p,
                                                          Entity entryEntity,
                                                          Method entryMethod) {
    var entityRegistry = fillEntityRegistry(p.entities());
    var entryGroup = entryMethod.callGroup();
    var calls = new ArrayList<ExecutionItem>();
    for (var call : entryGroup.calls()) {
      calls.addAll(resolve(call, entryEntity, entityRegistry));
    }
    return calls;
  }

  private static boolean isPublic(Method method) {
    return method.modifiers().contains(Modifier.PUBLIC);
  }

  static List<ExecutionItem> resolve(Call call,
                                     Entity sourceEntity,
                                     Map<String, Entity> entityRegistry) {
    var executionItems = new ArrayList<ExecutionItem>();
    if (call instanceof Call.MethodCall methodCall) {
      var targetEntity = findEntity(methodCall.ownerName(), entityRegistry);
      var targetMethod = findMethodInEntity(methodCall, targetEntity);
      var executionItem = new ExecutionItem(sourceEntity, targetEntity, targetMethod);
      executionItems.add(executionItem);
      var targetCalls = targetMethod.callGroup().calls();
      for (var subCall : targetCalls) {
        executionItems.addAll(resolve(subCall, targetEntity, entityRegistry));
      }
    } else if (call instanceof Call.Group group) {
      if (group.equals(Call.Group.EMPTY_GROUP)) {
        return List.of();
      }
      var calls = group.calls();
      for (var subCall : calls) {
        executionItems.addAll(resolve(subCall, sourceEntity, entityRegistry));
      }
    }
    return executionItems.stream()
            .filter(executionItem -> isPublic(executionItem.method()))
            .toList();
  }

  static Entity findEntity(String entityName, Map<String, Entity> entityRegistry) {
    return Optional.ofNullable(entityRegistry.get(entityName))
            .orElseThrow();
  }

  //Find match ?
  static Method findMethodInEntity(Call.MethodCall methodCall, Entity entity) {

    var descriptor = methodCall.descriptor();
    var name = methodCall.name();
    var methods = entity.methods();
    return methods.stream().filter(method -> {
      var methodDescriptor = method.descriptor();
      var methodName = method.name();
      return descriptor.equals(methodDescriptor)
              && name.equals(methodName);
    })
    .findFirst()
    .orElseThrow();
  }

  static HashMap<String, Entity> fillEntityRegistry(List<Entity> entities) {
    var map = new HashMap<String, Entity>();
    for (var entity : entities) {
      map.put(entity.type().name(), entity);
    }
    return map;
  }
}

package com.github.forax.umldoc.gen;

import static com.github.forax.umldoc.gen.ExecutionPathResolver.resolveCallExecution;
import static com.github.forax.umldoc.gen.ExecutionPathResolver.ExecutionItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ExecutionPathResolverTest {


  @Test
  void ExecutionItemTest() {
    var entity = new Entity(Set.of(), TypeInfo.of("test"), Entity.Stereotype.CLASS, List.of(), List.of());
    var method = new Method(Set.of(), "test", TypeInfo.of("test"), List.of(), Call.Group.EMPTY_GROUP);
    assertThrows(NullPointerException.class, () -> new ExecutionItem(null, entity, method));
    assertThrows(NullPointerException.class, () -> new ExecutionItem(entity, null, method));
    assertThrows(NullPointerException.class, () -> new ExecutionItem(entity, entity, null));
  }

  @Nested
  class GenerateExecutionPathTest {
  }

  @Nested
  class ResolveCallExecutionTest {

    @Test
    void methodWithEmptyGroupCallReturnsOneItemExecutionList() {
      var method = new Method(Set.of(), "test", TypeInfo.of("void"), List.of(), Call.Group.EMPTY_GROUP);
      var methodCall = new Call.MethodCall(TypeInfo.of("StartEntity"), "test", TypeInfo.of("void"), List.of());
      var entity = new Entity(Set.of(), TypeInfo.of("StartEntity"), Entity.Stereotype.CLASS, List.of(), List.of(method));

      assertEquals(List.of(new ExecutionItem(entity, entity, method)),
              resolveCallExecution(methodCall, entity, List.of(entity)));
    }

    @Test
    void methodThatCallsAnotherMethod() {
      //Final method
      var finalMethod = new Method(Set.of(), "end", TypeInfo.of("void"), List.of(), Call.Group.EMPTY_GROUP);
      var finalMethodCall = new Call.MethodCall(TypeInfo.of("entity2"), "end", TypeInfo.of("void"), List.of());
      //Starting method
      var startingMethod = new Method(Set.of(), "start", TypeInfo.of("void"), List.of(),
              new Call.Group(Call.Group.Kind.NONE, List.of(finalMethodCall)));
      var startingMethodCall = new Call.MethodCall(TypeInfo.of("entity1"), "start", TypeInfo.of("void"), List.of());

      var entity1 = new Entity(Set.of(), TypeInfo.of("entity1"), Entity.Stereotype.CLASS, List.of(), List.of(startingMethod));
      var entity2 = new Entity(Set.of(), TypeInfo.of("entity2"), Entity.Stereotype.CLASS, List.of(), List.of(finalMethod));
      assertEquals(List.of(new ExecutionItem(entity1, entity1, startingMethod), new ExecutionItem(entity1, entity2, finalMethod)),
              resolveCallExecution(startingMethodCall, entity1, List.of(entity1, entity2)));
    }
  }
}

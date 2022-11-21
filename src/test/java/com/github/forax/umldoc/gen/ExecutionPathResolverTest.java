package com.github.forax.umldoc.gen;

import com.github.forax.umldoc.core.Modifier;
import static com.github.forax.umldoc.gen.ExecutionPathResolver.resolve;
import static com.github.forax.umldoc.gen.ExecutionPathResolver.ExecutionItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Type;

public class ExecutionPathResolverTest {

  @Test
  void ExecutionItemTest() {
    var entity = new Entity(Set.of(), TypeInfo.of("test"), Entity.Stereotype.CLASS, List.of(), List.of());
    var descriptor = Type.getMethodDescriptor(Type.getType(void.class));
    var method = new Method(Set.of(), "test", TypeInfo.of("test"), List.of(), descriptor, Call.Group.EMPTY_GROUP);
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
      var descriptor = Type.getMethodDescriptor(Type.getType(void.class));
      var method = new Method(Set.of(Modifier.PUBLIC), "test", TypeInfo.of("void"), List.of(), descriptor,
              Call.Group.EMPTY_GROUP);
      var methodCall = new Call.MethodCall("StartEntity", "test", Type.getMethodDescriptor(Type.getType(void.class)));
      var entity = new Entity(Set.of(), TypeInfo.of("StartEntity"), Entity.Stereotype.CLASS, List.of(), List.of(method));

      assertEquals(List.of(new ExecutionItem(entity, entity, method)),
              resolve(methodCall, entity, Map.of("StartEntity", entity)));
    }

    @Test
    void methodThatCallsAnotherMethod() {
      var descriptor = Type.getMethodDescriptor(Type.getType(void.class));
      //Final method
      var finalMethod = new Method(Set.of(Modifier.PUBLIC), "end", TypeInfo.of("void"), List.of(), descriptor,
              Call.Group.EMPTY_GROUP);
      var finalMethodCall = new Call.MethodCall("entity2", "end", Type.getMethodDescriptor(Type.getType(void.class)));
      //Starting method
      var startingMethod = new Method(Set.of(Modifier.PUBLIC), "start", TypeInfo.of("void"), List.of(),
              descriptor,
              new Call.Group(Call.Group.Kind.NONE, List.of(finalMethodCall)));
      var startingMethodCall = new Call.MethodCall("entity1", "start", Type.getMethodDescriptor(Type.getType(void.class)));

      var entity1 = new Entity(Set.of(), TypeInfo.of("entity1"), Entity.Stereotype.CLASS, List.of(), List.of(startingMethod));
      var entity2 = new Entity(Set.of(), TypeInfo.of("entity2"), Entity.Stereotype.CLASS, List.of(), List.of(finalMethod));
      assertEquals(List.of(new ExecutionItem(entity1, entity1, startingMethod), new ExecutionItem(entity1, entity2, finalMethod)),
              resolve(startingMethodCall, entity1, Map.of("entity1", entity1, "entity2", entity2)));
    }


    @Test
    void methodThatCallsAnotherMethodThatIsPrivate() {
      var descriptor = Type.getMethodDescriptor(Type.getType(void.class));
      //Final method
      var finalMethod = new Method(Set.of(Modifier.PRIVATE), "end", TypeInfo.of("void"), List.of(), descriptor,
              Call.Group.EMPTY_GROUP);
      var finalMethodCall = new Call.MethodCall("entity2", "end", Type.getMethodDescriptor(Type.getType(void.class)));
      //Starting method
      var startingMethod = new Method(Set.of(Modifier.PUBLIC), "start", TypeInfo.of("void"), List.of(),
              descriptor,
              new Call.Group(Call.Group.Kind.NONE, List.of(finalMethodCall)));
      var startingMethodCall = new Call.MethodCall("entity1", "start", Type.getMethodDescriptor(Type.getType(void.class)));

      var entity1 = new Entity(Set.of(), TypeInfo.of("entity1"), Entity.Stereotype.CLASS, List.of(), List.of(startingMethod));
      var entity2 = new Entity(Set.of(), TypeInfo.of("entity2"), Entity.Stereotype.CLASS, List.of(), List.of(finalMethod));
      assertEquals(List.of(new ExecutionItem(entity1, entity1, startingMethod)),
              resolve(startingMethodCall, entity1,
                      Map.of("entity1", entity1, "entity2", entity2)));
    }

    @Test
    void privateMethodThatCallPublicMethod() {
      var descriptor = Type.getMethodDescriptor(Type.getType(void.class));
      //Final method
      var finalMethod = new Method(Set.of(Modifier.PUBLIC), "end", TypeInfo.of("void"), List.of(),
              descriptor,
              Call.Group.EMPTY_GROUP);
      var finalMethodCall = new Call.MethodCall("entity2", "end", Type.getMethodDescriptor(Type.getType(void.class)));
      //Starting method
      var startingMethod = new Method(Set.of(Modifier.PRIVATE), "start", TypeInfo.of("void"), List.of(),
              descriptor,
              new Call.Group(Call.Group.Kind.NONE, List.of(finalMethodCall)));
      var startingMethodCall = new Call.MethodCall("entity1", "start", Type.getMethodDescriptor(Type.getType(void.class)));

      var entity1 = new Entity(Set.of(), TypeInfo.of("entity1"), Entity.Stereotype.CLASS, List.of(), List.of(startingMethod));
      var entity2 = new Entity(Set.of(), TypeInfo.of("entity2"), Entity.Stereotype.CLASS, List.of(), List.of(finalMethod));
      assertEquals(List.of(new ExecutionItem(entity1, entity2, finalMethod)),
              resolve(startingMethodCall, entity1,
                      Map.of("entity1", entity1, "entity2", entity2)));
    }
  }


}
package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.TypeInfo;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Type;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodBuilderTest {

   static class TestHelper{
     static MethodBuilder createMethodBuilder(String name, String descriptor) {
      var modifiers = Utils.toModifiers(1);
      var returnType = TypeInfo.of(Type.getReturnType(descriptor).getClassName());
      var parameters = Arrays.stream(Type.getArgumentTypes(descriptor))
              .map(parameter -> new Method.Parameter("", TypeInfo.of(parameter.getClassName())))
              .toList();
      return new MethodBuilder(modifiers, name, returnType, parameters);
    }

     static Call.MethodCall createMethodCall(String name, String owner, String descriptor) {
       var type = TypeInfo.of(owner);
       var parametersType = Arrays.stream(Type.getArgumentTypes(descriptor))
               .map(parameter -> TypeInfo.of(parameter.getClassName()))
               .toList();
       var returnType = TypeInfo.of(Type.getReturnType(descriptor).getClassName());
      return new Call.MethodCall(type, name, returnType, parametersType);
    }
  }

  @Test
  public void buildSimpleMethod() {
    var builder = TestHelper.createMethodBuilder("test1", "(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L001");
    var methodCall = TestHelper.createMethodCall("hello2","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addMethod("L001",methodCall);
    var method = builder.build();
    assertEquals(1, method.callGroup().calls().size());
  }

  @Test
  public void buildMethodWithLoopOptional() {
    var builder = TestHelper.createMethodBuilder("test1", "(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L001");
    var methodCall = TestHelper.createMethodCall("hello1","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall2 = TestHelper.createMethodCall("hello2","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall3 = TestHelper.createMethodCall("hello3","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addMethod("L001",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L002");
    builder.addMethod("L002",methodCall2);
    builder.addMethod("L002",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.IF, "L003");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L004");
    builder.addMethod("L004",methodCall3);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L005");
    builder.addInstruction(MethodBuilder.InstructionType.GOTO, "L002");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L006");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L007");
    builder.addMethod("L007",methodCall);
    var method = builder.build();
    assertEquals("Group[kind=NONE, calls=[MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object," +
            " parameterTypes=[java.lang.Object]], Group[kind=LOOP, calls=[MethodCall[type=java/lang/Object, name=hello2, returnType=java.lang.Object," +
            " parameterTypes=[java.lang.Object]], MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]," +
            " Group[kind=OPTIONAL, calls=[MethodCall[type=java/lang/Object, name=hello3, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]]]]]," +
            " MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]]]", method.callGroup());
  }

  @Test
  public void buildMethodWithOptional() {
    var builder = TestHelper.createMethodBuilder("test1", "(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L001");
    var methodCall = TestHelper.createMethodCall("hello1","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall2 = TestHelper.createMethodCall("hello2","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall3 = TestHelper.createMethodCall("hello3","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addMethod("L001",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L002");
    builder.addMethod("L002",methodCall2);
    builder.addMethod("L002",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.IF, "L003");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L004");
    builder.addMethod("L004",methodCall3);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L005");
    builder.addInstruction(MethodBuilder.InstructionType.GOTO, "L002");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L006");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L007");
    builder.addMethod("L007",methodCall);
    var method = builder.build();
    assertEquals("<Group[kind=NONE, calls=[MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]," +
            " MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]]]>", method.callGroup());
  }

  @Test
  public void buildMethodWithAlternate() {
    var builder = TestHelper.createMethodBuilder("test1", "(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L001");
    var methodCall = TestHelper.createMethodCall("hello1","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall2 = TestHelper.createMethodCall("hello2","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall3 = TestHelper.createMethodCall("hello3","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addMethod("L001",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L002");
    builder.addMethod("L002",methodCall2);
    builder.addMethod("L002",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.IF, "L003");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L004");
    builder.addMethod("L004",methodCall3);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L005");
    builder.addInstruction(MethodBuilder.InstructionType.GOTO, "L002");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L006");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L007");
    builder.addMethod("L007",methodCall);
    var method = builder.build();
    assertEquals("<Group[kind=NONE, calls=[MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]," +
            " MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]]]>", method.callGroup());
  }

  @Test
  public void buildMethodWithNoMethodCall() {
    var builder = TestHelper.createMethodBuilder("test1", "(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L001");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L002");
    builder.addInstruction(MethodBuilder.InstructionType.IF, "L003");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L004");
    builder.addInstruction(MethodBuilder.InstructionType.GOTO, "L002");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L007");
    var method = builder.build();
    assertEquals("Group[kind=NONE, calls=[]]", method.callGroup());
  }
}

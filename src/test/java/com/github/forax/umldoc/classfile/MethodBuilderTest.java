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
    var methodCall = TestHelper.createMethodCall("hello1","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L001");
    builder.addMethod("L001",methodCall);
    var method = builder.build();
    assertEquals("Group[kind=NONE, calls=[MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object," +
            " parameterTypes=[java.lang.Object]]]]", method.callGroup().toString());
  }

  @Test
  public void buildMethodWithLoopOptional() {
    var builder = TestHelper.createMethodBuilder("test1", "(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall = TestHelper.createMethodCall("hello1","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall2 = TestHelper.createMethodCall("hello2","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall3 = TestHelper.createMethodCall("hello3","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L001");
    builder.addMethod("L001",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L002");
    builder.addMethod("L002",methodCall2);
    builder.addMethod("L002",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.IF, "L003");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L004");
    builder.addMethod("L004",methodCall3);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L005");
    builder.addInstruction(MethodBuilder.InstructionType.GOTO, "L002");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L003");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L006");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L007");
    builder.addMethod("L007",methodCall);
    var method = builder.build();
    assertEquals("Group[kind=NONE, calls=[MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, " +
            "parameterTypes=[java.lang.Object]], Group[kind=LOOP, calls=[MethodCall[type=java/lang/Object, name=hello2, returnType=java.lang.Object," +
            " parameterTypes=[java.lang.Object]], MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, " +
            "parameterTypes=[java.lang.Object]], Group[kind=OPTIONAL, calls=[MethodCall[type=java/lang/Object, name=hello3, returnType=java.lang.Object, " +
            "parameterTypes=[java.lang.Object]]]]]], MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object," +
            " parameterTypes=[java.lang.Object]]]]", method.callGroup().toString());
  }

  @Test
  public void buildMethodWithOptional() {
    var builder = TestHelper.createMethodBuilder("test1", "(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall = TestHelper.createMethodCall("hello1","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall2 = TestHelper.createMethodCall("hello2","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L001");
    builder.addMethod("L001",methodCall);
    builder.addMethod("L001",methodCall2);
    builder.addInstruction(MethodBuilder.InstructionType.IF, "L002");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L003");
    builder.addMethod("L003",methodCall2);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L002");
    var method = builder.build();
    assertEquals("Group[kind=NONE, calls=[MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]," +
            " MethodCall[type=java/lang/Object, name=hello2, returnType=java.lang.Object, parameterTypes=[java.lang.Object]], Group[kind=OPTIONAL," +
            " calls=[MethodCall[type=java/lang/Object, name=hello2, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]]]]]", method.callGroup().toString());
  }

  @Test
  public void buildMethodWithAlternate() {
    var builder = TestHelper.createMethodBuilder("test1", "(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall = TestHelper.createMethodCall("hello1","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall2 = TestHelper.createMethodCall("hello2","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall3 = TestHelper.createMethodCall("hello3","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L001");
    builder.addMethod("L001",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.IF, "L002");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L003");
    builder.addMethod("L003",methodCall2);
    builder.addInstruction(MethodBuilder.InstructionType.GOTO, "L008");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L002");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L006");
    builder.addMethod("L006",methodCall3);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L007");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L008");
    var method = builder.build();
    assertEquals("Group[kind=NONE, calls=[MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, parameterTypes=[java.lang.Object]], Group[kind=ALTERNATE, calls=[MethodCall[type=java/lang/Object, name=hello2, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]]], Group[kind=ALTERNATE, calls=[MethodCall[type=java/lang/Object, name=hello3, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]]]]]", method.callGroup().toString());
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
    assertEquals("Group[kind=NONE, calls=[]]", method.callGroup().toString());
  }

  @Test
  public void buildMethodWithMultipleStatement() {
    var builder = TestHelper.createMethodBuilder("test1", "(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall = TestHelper.createMethodCall("hello1","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall2 = TestHelper.createMethodCall("hello2","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    var methodCall3 = TestHelper.createMethodCall("hello3","java/lang/Object" ,"(Ljava/lang/Object;)Ljava/lang/Object;");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L001");
    builder.addMethod("L001",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L002");
    builder.addMethod("L002",methodCall2);
    builder.addMethod("L002",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.IF, "L003");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L004");
    builder.addMethod("L004",methodCall3);
    builder.addInstruction(MethodBuilder.InstructionType.IF, "L009");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L010");
    builder.addMethod("L010",methodCall2);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L020");
    builder.addMethod("L020",methodCall);
    builder.addInstruction(MethodBuilder.InstructionType.IF, "L021");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L022");
    builder.addMethod("L022",methodCall2);
    builder.addInstruction(MethodBuilder.InstructionType.GOTO, "L023");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L021");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L024");
    builder.addMethod("L024",methodCall3);
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L025");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L023");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L009");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L005");
    builder.addInstruction(MethodBuilder.InstructionType.GOTO, "L002");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L003");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L006");
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L007");
    var method = builder.build();
    assertEquals("Group[kind=NONE, calls=[MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]" +
            ", Group[kind=LOOP, calls=[MethodCall[type=java/lang/Object, name=hello2, returnType=java.lang.Object, parameterTypes=[java.lang.Object]], " +
            "MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, parameterTypes=[java.lang.Object]], Group[kind=OPTIONAL, " +
            "calls=[MethodCall[type=java/lang/Object, name=hello3, returnType=java.lang.Object, parameterTypes=[java.lang.Object]], Group[kind=OPTIONAL," +
            " calls=[MethodCall[type=java/lang/Object, name=hello2, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]," +
            " MethodCall[type=java/lang/Object, name=hello1, returnType=java.lang.Object, parameterTypes=[java.lang.Object]], Group[kind=ALTERNATE, " +
            "calls=[MethodCall[type=java/lang/Object, name=hello2, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]]], " +
            "Group[kind=ALTERNATE, calls=[MethodCall[type=java/lang/Object, name=hello3, returnType=java.lang.Object, parameterTypes=[java.lang.Object]]]]]" +
            "]]]]]]]", method.callGroup().toString());
  }

}

package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.TypeInfo;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodBuilderTest {
  @Test
  public void buildSimpleMethod() {
    var builder = new MethodBuilder();
    builder.addInstruction(MethodBuilder.InstructionType.NONE, "L001");
    var type = TypeInfo.of("java/util/Objects");
    var parametersType = Arrays.stream(Type.getArgumentTypes("(Ljava/lang/Object;)Ljava/lang/Object;"))
            .map(parameter -> TypeInfo.of(parameter.getClassName()))
            .toList();
    var returnType = TypeInfo.of(Type.getReturnType("(Ljava/lang/Object;)Ljava/lang/Object;").getClassName());
    var methodCall = new Call.MethodCall(type, "testMethod1" , returnType, parametersType);
    builder.addMethod("L001",methodCall);
  }
}

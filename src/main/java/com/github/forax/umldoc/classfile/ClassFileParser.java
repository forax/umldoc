package com.github.forax.umldoc.classfile;

import static java.util.Objects.requireNonNull;
import static org.objectweb.asm.Opcodes.ASM9;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.TypeInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

final class ClassFileParser {
  /**
   * Result of a call to {@link ClassFileParser#parseClass(InputStream)}.
   */
  record ParsingResult(EntityBuilder entityBuilder, List<Delegation> delegations,
                       List<TypeInfo> superTypes) {
    ParsingResult {
      requireNonNull(entityBuilder);
      requireNonNull(delegations);
      requireNonNull(superTypes);
    }
  }

  private ClassFileParser() {
    throw new AssertionError();
  }

  public static ParsingResult parseClass(InputStream inputStream) throws IOException {
    var entityBuilder = new EntityBuilder();
    var delegations = new ArrayList<Delegation>();
    var superTypes = new ArrayList<TypeInfo>();
    var classReader = new ClassReader(inputStream);
    classReader.accept(new ClassVisitor(ASM9) {
      @Override
      public void visit(int version, int access, String name, String signature,
                        String superName, String[] interfaces) {
        entityBuilder.stereotype(Utils.toStereotype(access));
        entityBuilder.type(TypeInfo.of(name.replace('/', '.')));
        if (superName != null) {
          superTypes.add(TypeInfo.of(superName.replace('/', '.')));
        }
        for (var anInterface : interfaces) {
          var typeInfo = TypeInfo.of(anInterface.replace('/', '.'));
          superTypes.add(typeInfo);
        }
      }

      @Override
      public RecordComponentVisitor visitRecordComponent(String name, String descriptor,
                                                         String signature) {
        return null;
      }

      @Override
      public FieldVisitor visitField(int access, String name, String descriptor,
                                     String signature, Object value) {
        // skip synthetic fields
        if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
          return null;
        }

        var fieldType = signature == null ? decodeField(descriptor) : decodeField(signature);
        var delegation = new Delegation(Utils.toModifiers(access), fieldType, name);
        delegations.add(delegation);
        return null;
      }

      @Override
      public MethodVisitor visitMethod(int access, String name, String descriptor,
                                       String signature, String[] exceptions) {
        if (name.equals("<init>")) {
          return null;
        }
        var modifiers = Utils.toModifiers(access);
        TypeInfo returnType;
        if (signature != null) {
          returnType = decodeMethod(signature);
        } else {
          returnType = TypeInfo.of(Type.getReturnType(descriptor).getClassName());
        }
        var parameters = Arrays.stream(Type.getArgumentTypes(descriptor))
                .map(parameter -> new Method.Parameter("", TypeInfo.of(parameter.getClassName())))
                .toList();

        var methodBuilder = entityBuilder.addMethod(modifiers, name, returnType,
                parameters);


        return new MethodVisitor(ASM9) {
          @Override
          public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
                                      boolean isInterface) {

            var ownerName = owner.replace("/", ".");
            var call = new Call.MethodCall(ownerName, name, descriptor);
            methodBuilder.addCallToGroup(call);
          }
        };
      }
    }, 0);
    return new ParsingResult(entityBuilder, delegations, superTypes);
  }

  private static TypeInfo decodeField(String signature) {
    var reader = new SignatureReader(signature);
    var visitor = new SignatureVisitorImpl(type -> {});
    reader.acceptType(visitor);
    return visitor.result;
  }

  private static TypeInfo decodeMethod(String signature) {
    var reader = new SignatureReader(signature);
    var visitor = new SignatureVisitorImpl(type -> {});
    reader.accept(visitor);
    return visitor.result;
  }

  private static class SignatureVisitorImpl extends SignatureVisitor {
    private final Consumer<TypeInfo> finish;
    private TypeInfo result;

    SignatureVisitorImpl(Consumer<TypeInfo> finish) {
      super(ASM9);
      this.finish = finish;
    }

    // TypeSignature = visitBaseType | visitTypeVariable | visitArrayType |
    // ( visitClassType visitTypeArgument* ( visitInnerClassType visitTypeArgument* )* visitEnd ) )

    @Override
    public void visitBaseType(char descriptor) {
      var name = Type.getType("" + descriptor).getClassName();
      result = new TypeInfo(Optional.empty(), name, List.of());
    }

    @Override
    public void visitTypeVariable(String name) {
      result = new TypeInfo(Optional.empty(), name, List.of());
    }

    @Override
    public SignatureVisitor visitArrayType() {
      return new SignatureVisitorImpl(type -> result =
          new TypeInfo(Optional.empty(), "array", List.of(type)));
    }

    @Override
    public void visitClassType(String name) {
      result = new TypeInfo(Optional.empty(), name.replace('/', '.'), List.of());
    }

    @Override
    public SignatureVisitor visitTypeArgument(char wildcard) {
      return new SignatureVisitorImpl(type -> result = result.withTypeParameter(type));
    }

    @Override
    public void visitInnerClassType(String name) {
      result = new TypeInfo(Optional.of(result), name, List.of());
    }

    @Override
    public void visitEnd() {
      finish.accept(result);
    }
  }
}

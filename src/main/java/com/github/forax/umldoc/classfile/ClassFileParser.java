package com.github.forax.umldoc.classfile;

import static com.github.forax.umldoc.core.AssociationDependency.Cardinality.MANY;
import static com.github.forax.umldoc.core.AssociationDependency.Cardinality.ONLY_ONE;
import static com.github.forax.umldoc.core.AssociationDependency.Cardinality.ZERO_OR_ONE;
import static java.util.Objects.requireNonNull;
import static org.objectweb.asm.Opcodes.ASM9;

import com.github.forax.umldoc.core.AssociationDependency.Cardinality;
import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.TypeInfo;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.PathMatcher;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;
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
  record ParsingResult(EntityBuilder entityBuilder, List<Delegation> delegations) {
    ParsingResult {
      requireNonNull(entityBuilder);
      requireNonNull(delegations);
    }
  }

  private ClassFileParser() {
    throw new AssertionError();
  }

  public static ParsingResult parseClass(InputStream inputStream) throws IOException {
    var entityBuilder = new EntityBuilder();
    var delegations = new ArrayList<Delegation>();
    var classReader = new ClassReader(inputStream);
    classReader.accept(new ClassVisitor(ASM9) {
      @Override
      public void visit(int version, int access, String name, String signature,
                        String superName, String[] interfaces) {
        entityBuilder.stereotype(Utils.toStereotype(access));
        entityBuilder.type(TypeInfo.of(name.replace('/', '.')));
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
        //System.out.println("FIELD ::: " + "Access: "+ access + " Name: " + name +  "  Descriptor: " + descriptor +
         //       " Signature: " + signature);

        var fieldType = signature == null ? decodeField(descriptor) : decodeField(signature);
        var delegation = new Delegation(Utils.toModifiers(access), fieldType, name);
        delegations.add(delegation);
        return null;
      }

      @Override
      public MethodVisitor visitMethod(int access, String name, String descriptor,
                                       String signature, String[] exceptions) {

        //System.out.println("METHOD ::: " + "Access: "+ access + " Name: " + name +  "  Descriptor: " + descriptor +
       //         " Signature: " + signature);
        //var methodType = TypeInfo.of(signature.replace('/', '.'));
        var parameterList = new ArrayList<String>();

/*
        var stringToFormat = descriptor;
        var res = stringToFormat.substring(1, stringToFormat.length() - 3).split(";");
        System.out.println(Stream.of(res).toList());
        var startIndex = descriptor.indexOf(";");
        /*
        var positionIndex =


        //var regExp = Pattern.compile("<*>*~[>;]*;");
        //regExp.matcher(descriptor).results().forEach((matcher) -> System.out.println(matcher.group()));

        var fieldType = signature == null ? decodeField(descriptor) : decodeField(signature);




        var modifiers = Utils.toModifiers(access);
        var group = new Call.Group(Call.Group.Kind.NONE, new ArrayList<>());
  */
        //System.out.println("Before type = " + descriptor);
        var stringToFormat = descriptor.substring(1).substring(0,descriptor.length() - 3);
        var startIndex = 0;
        var endIndexParam = stringToFormat.indexOf(";") + 1;
        while(endIndexParam>0){
          var param = stringToFormat.substring(startIndex,endIndexParam);
          //System.out.println("Param type = " + param);
          parameterList.add(param);
          startIndex = endIndexParam;
          endIndexParam = stringToFormat.indexOf(";",endIndexParam + 1) + 1;
        }
        if(!parameterList.isEmpty()){
          System.out.println(parameterList);
        }


        //var newMethod = new Method(modifiers, name, methodType, parameters, group);
        return new MethodVisitor(ASM9) {
          private int parameterCount;
          @Override
          public void visitParameter(String name, int access) {
            System.out.println(name + " " + parameterCount);
            parameterCount++;
          }

          @Override
          public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
                                      boolean isInterface) {
            //entityBuilder.addMethodCall();
            //System.out.println("CALL :::" + "Access: " + opcode + " Owner: " + owner + " Name: " + name);
            //System.out.println( "Descriptor: " + descriptor + " isInterface: " + isInterface);
          }

        };
      }
    }, 0);
    return new ParsingResult(entityBuilder, delegations);
  }

  private static TypeInfo decodeField(String signature) {
    var reader = new SignatureReader(signature);
    var visitor = new SignatureVisitorImpl(type -> {});
    reader.acceptType(visitor);
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

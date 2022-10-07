package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.core.Entity;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;

final class ClassFileParser {
  private static final class EntityBuilder {
    private String name;

    public void name(String name) {
      this.name = name;
    }

    public Entity build() {
      return new Entity(Set.of(), name, Optional.empty(), List.of(), List.of());
    }
  }

  private ClassFileParser() {
    throw new AssertionError();
  }

  public static Entity parseClass(InputStream inputStream) throws IOException {
    var entityBuilder = new EntityBuilder();
    var classReader = new ClassReader(inputStream);
    classReader.accept(new ClassVisitor(Opcodes.ASM9) {
      @Override
      public void visit(int version, int access, String name, String signature, String superName,
                        String[] interfaces) {
        entityBuilder.name(name.replace('/', '.'));
      }

      @Override
      public RecordComponentVisitor visitRecordComponent(String name, String descriptor,
                                                         String signature) {
        return null;
      }

      @Override
      public FieldVisitor visitField(int access, String name, String descriptor,
                                     String signature, Object value) {
        return null;
      }

      @Override
      public MethodVisitor visitMethod(int access, String name, String descriptor,
                                       String signature, String[] exceptions) {
        return null;
      }
    }, 0);
    return entityBuilder.build();
  }
}

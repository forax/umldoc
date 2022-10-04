package com.github.forax.umldoc.classfile;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;

import java.io.IOException;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.MethodType;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;

public class ASMTest {
  @Test
  public void asm() throws IOException {
    var path = Path.of("target");
    var finder = ModuleFinder.of(path);
    for(var moduleReference: finder.findAll()) {
      try(var reader = moduleReference.open()) {
        for(var filename: (Iterable<String>) reader.list()::iterator) {
          if (!filename.endsWith(".class")) {
            continue;
          }
          try(var inputStream = reader.open(filename).orElseThrow()) {
            var classReader = new ClassReader(inputStream);
            classReader.accept(new ClassVisitor(Opcodes.ASM9) {

              private static String modifier(int access) {
                if (Modifier.isPublic(access)) {
                  return "+";
                }
                if (Modifier.isPrivate(access)) {
                  return "-";
                }
                if (Modifier.isProtected(access)) {
                  return "#";
                }
                return "";
              }

              @Override
              public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                System.err.println("class " + modifier(access) + " " + name + " " + superName + " " + (interfaces != null? Arrays.toString(interfaces): ""));
              }

              @Override
              public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
                System.err.println("  component " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName());
                return null;
              }

              @Override
              public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                System.err.println("  field " + modifier(access) + " " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName() + " " + signature);
                return null;
              }

              @Override
              public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                System.err.println("  method " + modifier(access) + " " + name + " " + MethodTypeDesc.ofDescriptor(descriptor).displayDescriptor() + " " + signature);
                return null;
              }
            }, 0);
          }
        }
      }
    }
  }
}

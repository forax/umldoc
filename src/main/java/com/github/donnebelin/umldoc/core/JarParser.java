package com.github.donnebelin.umldoc.core;
import com.github.forax.umldoc.core.Entity;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JarParser {
    private final ArrayList<Entity> entities = new ArrayList<>();

    private static String getModifier(int access) {
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

    private void getASMData(ClassReader classReader) {
        classReader.accept(new ClassVisitor(Opcodes.ASM9) {
            private static String modifier(int access) {
                return getModifier(access);
            }

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//                                  + " " + (interfaces != null? Arrays.toString(interfaces): ""));
                var modifier = modifier(access);
                var className = name;


            }
//
//                            @Override
//                            public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
//                                System.err.println("  component " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName());
//                                return null;
//                            }
//
//                            @Override
//                            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
//                                System.err.println("  field " + modifier(access) + " " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName() + " " + signature);
//                                return null;
//                            }
//
//                            @Override
//                            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//                                System.err.println("  method " + modifier(access) + " " + name + " " + MethodTypeDesc.ofDescriptor(descriptor).displayDescriptor() + " " + signature);
//                                return null;
//                            }
        }, 0);
    }

    private void recoverEntitiesFromJar() throws IOException {
        var path = Path.of("target");
        var finder = ModuleFinder.of(path);
        for(var moduleReference: finder.findAll()) {
            try (var reader = moduleReference.open()) {
                for (var filename : (Iterable<String>) reader.list()::iterator) {
                    if (!filename.endsWith(".class")) {
                        continue;
                    }
                    try (var inputStream = reader.open(filename).orElseThrow()) {
                        var classReader = new ClassReader(inputStream);
                        getASMData(classReader);
                    }
                }
            }
        }
    }

    public List<Entity> parse() throws IOException {
        recoverEntitiesFromJar();
        return entities;
    }
}

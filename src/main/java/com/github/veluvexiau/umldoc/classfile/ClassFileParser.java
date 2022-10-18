package com.github.veluvexiau.umldoc.classfile;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Entity.Stereotype;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import java.io.IOException;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;

/**
 * ClassFileParser read a .jar file
 */
public final class ClassFileParser {

  /**
   * Read a jar file given by the parameter Path and return a list of Entity.
   *
   * @param path File's path
   * @return List of Entity List created by the method containing all entities
   * @throws IOException if the file cannot be opened
   */
  public static List<Entity> readJarFile(Path path) throws IOException {
    var entities = new ArrayList<Entity>();
    var finder = ModuleFinder.of(path);
    for (var moduleReference : finder.findAll()) {
      try (var reader = moduleReference.open()) {
        for (var filename : (Iterable<String>) reader.list()::iterator) {
          if (!filename.endsWith(".class")) {
            continue;
          }
          try (var inputStream = reader.open(filename).orElseThrow()) {
            var classReader = new ClassReader(inputStream);
            classReader.accept(new ClassVisitor(Opcodes.ASM9) {

              private static Modifier modifier(int access) {
                if (java.lang.reflect.Modifier.isPublic(access)) {
                  return Modifier.PUBLIC;
                }
                if (java.lang.reflect.Modifier.isPrivate(access)) {
                  return Modifier.PRIVATE;
                }
                if (java.lang.reflect.Modifier.isProtected(access)) {
                  return Modifier.PROTECTED;
                }
                return null;
                //throw new IllegalStateException("Modifier not recognized" + access);
              }

              @Override
              public void visit(int vers,
                                int access,
                                String name,
                                String sign,
                                String supName,
                                String[] inter) {
                Entity entity;
                if (java.lang.reflect.Modifier.isInterface(access)) {
                  entity = new Entity(Set.of(), name,
                          Stereotype.INTERFACE, List.of(), List.of());
                } else if ((access & Opcodes.ACC_RECORD) != 0) {
                  entity = new Entity(Set.of(), name,
                          Stereotype.RECORD, List.of(), List.of());
                } else if ((access & Opcodes.ACC_ENUM) != 0) {
                  entity = new Entity(Set.of(), name,
                          Stereotype.ENUM, List.of(), List.of());
                } else {
                  entity = new Entity(Set.of(), name,
                          Stereotype.CLASS, List.of(), List.of());
                }
                entities.add(entity);
              }

              @Override
              public RecordComponentVisitor visitRecordComponent(String name,
                                                                 String desc,
                                                                 String sign) {
                //TODO check if it is really usefull, because it seams like for records,
                // fields have exactly the same informations as "recordComponent"
                return null;
              }

              @Override
              public FieldVisitor visitField(int access,
                                             String name,
                                             String descriptor,
                                             String signature,
                                             Object value) {
                //TODO create method to get the full type
                // (like String, List<String>, Set<Entity> for exemple -->
                // concatenation of descriptor and signature
                if (modifier(access) != null) {
                  var type = ClassDesc.ofDescriptor(descriptor).displayName();
                  System.out.println(ClassDesc.ofDescriptor(descriptor).displayName() + ClassDesc.ofDescriptor(descriptor).componentType());
                  Field field = new Field(Set.of(modifier(access)), name, type);
                  var oldEntity = entities.get(entities.size() - 1);
                  var listOfFields = new ArrayList<>(oldEntity.fields());
                  listOfFields.add(field);
                  Entity entity = new Entity(oldEntity.modifiers(), oldEntity.name(),
                          oldEntity.stereotype(), listOfFields,
                          oldEntity.methods());
                  entities.set(entities.size() - 1, entity);
                }
                return null;
              }

              @Override
              public MethodVisitor visitMethod(int access,
                                               String name,
                                               String desc,
                                               String signature,
                                               String[] exceptions) {
                var oldEntity = entities.get(entities.size() - 1);
                var listOfMethods = new ArrayList<>(oldEntity.methods());

                if (modifier(access) != null) {
                  List<Method.Parameter> parameters = new ArrayList<>();
                  for (var met : MethodTypeDesc.ofDescriptor(desc).parameterList()) {
                    Method.Parameter p = new Method.Parameter(met.toString(), met.displayName());
                    parameters.add(p);
                  }
                  Method method = new Method(Set.of(modifier(access)), name, "" + signature,
                          parameters);
                  listOfMethods.add(method);
                  Entity entity = new Entity(oldEntity.modifiers(), oldEntity.name(),
                          oldEntity.stereotype(), oldEntity.fields(),
                          listOfMethods);
                  entities.set(entities.size() - 1, entity);
                }
                return null;
              }
            }, 0);
          }
        }
      }
    }
    return entities;
  }
}
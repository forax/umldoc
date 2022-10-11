package com.github.veluvexiau.umldoc.core;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Modifier;
import java.io.IOException;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;

/**
 * The main class to export UML in Mermaid and PlantUml.
 */
public class Main {
  /**
   * The main method to export UML in mermaid and PlantUml.
   */
  public static void main(String[] args) throws IOException {
    /* if (args.length<1){
        throw new IllegalStateException("no path specified");
    }*/
    var path = Path.of("target");
    var entityFromJar = readJarFile(path);

    MermaidExtract mermaid = new MermaidExtract();
    PlantUmlExtract plantUml = new PlantUmlExtract();

    mermaid.generate(entityFromJar);
    plantUml.generate(entityFromJar);
  }


  private static List<Entity> readJarFile(Path path) throws IOException {

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
                // throw new IllegalStateException("Modifier not recognized" + access);
              }

              @Override
              public void visit(int vers,
                                int access,
                                String name,
                                String sign,
                                String supName,
                                String[] inter) {

                var entity = new Entity(Set.of(), name,
                                        Optional.ofNullable(supName), List.of(), List.of());
                System.out.println("class " + modifier(access)
                                    + " " + name + " " + supName + " "
                                    + (inter != null ? Arrays.toString(inter) : ""));
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

                  Field field = new Field(Set.of(modifier(access)), name, type);
                  var oldEntity = entities.get(entities.size() - 1);
                  var listOfFields = new ArrayList<>(oldEntity.fields());
                  listOfFields.add(field);
                  Entity entity = new Entity(oldEntity.modifiers(), oldEntity.name(),
                                      oldEntity.stereotype(), listOfFields,
                                      oldEntity.methods());
                  entities.set(entities.size() - 1, entity);
                }
                System.out.println("  field " + modifier(access) + " " + name
                                  + " " + ClassDesc.ofDescriptor(descriptor).displayName()
                                  + " " + signature);

                return null;
              }

              @Override
              public MethodVisitor visitMethod(int access,
                                               String name,
                                               String desc,
                                               String signature,
                                               String[] exceptions) {
                System.out.println("  method " + modifier(access) + " " + name
                                  + " " + MethodTypeDesc.ofDescriptor(desc).displayDescriptor()
                                  + " " + signature);
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

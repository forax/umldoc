package com.github.donnebelin.umldoc.classfile;

import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;

import com.github.donnebelin.umldoc.builder.TypeInfoBuilder;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Entity.Stereotype;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;
import java.io.IOException;
import java.lang.constant.ClassDesc;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Class to parse a jar file.
 * This class uses ASM library to recover
 * entities, fields and methods
 * from .class files of the given jar.
 */
public final class JarParser {
  private final HashSet<Entity> entities = new HashSet<>();
  private Entity currentEntity = null;

  /**
   * Instantiate a new JarParser to parse all entities from a jar file.
   *
   * @throws IOException if I/O exception
   *                     occurred during parsing .class files
   */
  public JarParser() throws IOException {
    recoverEntitiesFromJar();
  }

  private static Stereotype resolveStereotype(int access) {
    if ((access & Opcodes.ACC_RECORD) != 0) {
      return Stereotype.RECORD;
    }

    if ((access & Opcodes.ACC_ANNOTATION) != 0) {
      return Stereotype.ANNOTATION;
    }

    if ((access & Opcodes.ACC_INTERFACE) != 0) {
      return Stereotype.INTERFACE;
    }

    if ((access & Opcodes.ACC_ENUM) != 0) {
      return Stereotype.ENUM;
    }


    if ((access & Opcodes.ACC_ABSTRACT) != 0) {
      return Stereotype.ABSTRACT;
    }

    return Stereotype.CLASS;
  }

  private static Set<Modifier> modifiers(int access) {
    var modifiers = new HashSet<Modifier>();

    if ((access & Opcodes.ACC_STATIC) != 0) {
      modifiers.add(Modifier.STATIC);
    }

    if ((access & Opcodes.ACC_FINAL) != 0) {
      modifiers.add(Modifier.FINAL);
    }

    if ((access & Opcodes.ACC_PUBLIC) != 0) {
      modifiers.add(Modifier.PUBLIC);
    } else if ((access & Opcodes.ACC_PRIVATE) != 0) {
      modifiers.add(Modifier.PRIVATE);
    } else if ((access & Opcodes.ACC_PROTECTED) != 0) {
      modifiers.add(Modifier.PROTECTED);
    } else {
      modifiers.add(Modifier.PACKAGE);
    }

    return modifiers;
  }

  private static TypeInfo resolveTypeParameter(String typeParameter, String descriptor) {
    if (typeParameter == null) {
      return TypeInfo.of(descriptor);
    }

    return new TypeInfoBuilder().build(typeParameter);
  }

  /**
   * Supplies a list of all Entities parsed from jar file.
   *
   * @return A list of Entity object with all parsed entities discovered in the jar file.
   */
  public List<Entity> entities() {
    return List.copyOf(entities);
  }

  private void recoverEntitiesFromJar() throws IOException {
    var path = Path.of("target");
    var finder = ModuleFinder.of(path);
    for (var moduleReference : finder.findAll()) {
      try (
              var reader = moduleReference.open()
      ) {
        for (var filename :
                (Iterable<String>) reader.list()::iterator) {
          if (!filename.endsWith(".class")) {
            continue;
          }
          try (var inputStream = reader
                  .open(filename).orElseThrow()) {
            var classReader =
                    new ClassReader(inputStream);
            getAsmData(classReader);
          }
        }
      }
    }
  }

  private void addEntityAndFields(int access, String name, String type, String descriptor,
                                  HashSet<Field> currentFields) {
    currentFields.add(new Field(
            modifiers(access),
            name,
            resolveTypeParameter(type, descriptor)
    ));

    currentEntity = new Entity(
            currentEntity.modifiers(),
            currentEntity.type(),
            currentEntity.stereotype(),
            List.copyOf(currentFields),
            currentEntity.methods()
    );

    var oldEntity = entities.stream()
            .filter(entity -> entity.type().name().equals(currentEntity.type().name()))
            .findFirst();
    oldEntity.ifPresent(entities::remove);
    entities.add(currentEntity);
  }

  private void getAsmData(ClassReader classReader) {
    var currentFields = new HashSet<Field>();
    classReader.accept(new ClassVisitor(Opcodes.ASM9) {

      @Override
      public void visit(
              int version,
              int access,
              String name,
              String signature,
              String superName,
              String[] interfaces) {
        if (!name.equals("module-info")) {
          currentEntity = new Entity(
                  Set.of(),
                  TypeInfo.of(name),
                  resolveStereotype(access),
                  List.of(),
                  List.of()
          );
        }
      }

      @Override
      public FieldVisitor visitField(
              int access,
              String name,
              String descriptor,
              String signature,
              Object value) {

        if ((access & ACC_SYNTHETIC) == ACC_SYNTHETIC) {
          return null;
        }

        addEntityAndFields(
                access,
                name,
                signature,
                ClassDesc.ofDescriptor(descriptor).displayName(),
                currentFields
        );

        return null;
      }

    }, 0);
  }
}

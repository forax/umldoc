package com.github.magickoders;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Modifier;
import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;

/**
 * An utils class used to retrieve the content of a jar. which returns the entities contained in the
 * directory "target"
 */
public class JarReader {

  private static final Path DEFAULT_SEARCH_DIRECTORY = Path.of("target/classes");

  private static boolean isPublic(int access) {
    return java.lang.reflect.Modifier.isPublic(access);
  }

  private static boolean isPrivate(int access) {
    return java.lang.reflect.Modifier.isPrivate(access);
  }

  private static boolean isProtected(int access) {
    return java.lang.reflect.Modifier.isProtected(access);
  }

  private static boolean isFinal(int access) {
    return java.lang.reflect.Modifier.isFinal(access);
  }

  private static boolean isStatic(int access) {
    return java.lang.reflect.Modifier.isStatic(access);
  }

  private static Modifier getVisibility(int access) {
    if (isPublic(access)) {
      return Modifier.PUBLIC;
    }
    if (isProtected(access)) {
      return Modifier.PROTECTED;
    }
    if (isPrivate(access)) {
      return Modifier.PRIVATE;
    }
    return Modifier.PACKAGE;
  }

  private static HashSet<Modifier> modifiers(int access) {
    var modifiers = new HashSet<Modifier>();
    if (isStatic(access)) {
      modifiers.add(Modifier.STATIC);
    }
    if (isFinal(access)) {
      modifiers.add(Modifier.FINAL);
    }

    modifiers.add(getVisibility(access));
    return modifiers;
  }

  private static boolean isRecord(int access) {
    return (access & Opcodes.ACC_RECORD) != 0;
  }

  private static boolean isInterface(int access) {
    return (access & Opcodes.ACC_INTERFACE) != 0;
  }

  private static boolean isEnum(int access) {
    return (access & Opcodes.ACC_ENUM) != 0;
  }

  private static boolean isAnnotation(int access) {
    return (access & Opcodes.ACC_ANNOTATION) != 0;
  }

  private static boolean isAbstract(int access) {
    return (access & Opcodes.ACC_ABSTRACT) != 0;
  }

  private static boolean isModule(int access) {
    return (access & Opcodes.ACC_MODULE) != 0;
  }

  private static Entity.Stereotype stereotype(int access) {
    if (isRecord(access)) {
      return Entity.Stereotype.RECORD;
    }
    if (isInterface(access)) {
      return Entity.Stereotype.INTERFACE;
    }
    if (isEnum(access)) {
      return Entity.Stereotype.ENUM;
    }
    if (isAnnotation(access)) {
      return Entity.Stereotype.ANNOTATION;
    }
    if (isAbstract(access)) {
      return Entity.Stereotype.ABSTRACT;
    }
    return Entity.Stereotype.CLASS;
  }

  private static String entityName(String name) {
    return name.substring(name.lastIndexOf("/") + 1);
  }

  /**
   * Searches the in the default search directory (target/classes) for entities.
   *
   * @return the list of entities found.
   * @throws IOException
   *         in case of IOException
   */
  public static List<Entity> getEntities() throws IOException {
    return getEntities(DEFAULT_SEARCH_DIRECTORY);
  }

  /**
   * Searches the searchDirectory for entities.
   *
   * @param searchDirectory
   *         the directory to search for entities.
   * @return the list of entities found.
   * @throws IOException
   *         in case of IOException
   */
  public static List<Entity> getEntities(Path searchDirectory) throws IOException {
    Objects.requireNonNull(searchDirectory);

    var entities = new ArrayList<Entity>();
    var myVisitor = new ClassVisitor(Opcodes.ASM9) {

      @Override
      public void visit(int version, int access, String name, String signature, String superName,
                        String[] interfaces) {
        if (isModule(access)) {
          return;
        }

        var modifiers = modifiers(access);
        var entityName = entityName(name);
        var stereotype = stereotype(access);
        modifiers.add(getVisibility(access));
        var entity = new Entity(modifiers, entityName, stereotype, List.of(), List.of());
        entities.add(entity);
      }

      @Override
      public RecordComponentVisitor visitRecordComponent(String name, String descriptor,
                                                         String signature) {
        return null;
      }

      @Override
      public FieldVisitor visitField(int access, String name, String descriptor, String signature,
                                     Object value) {
        var modifiers = modifiers(access);
        System.out.println(name);
        System.out.println(descriptor);
        System.out.println(signature);
        return null;
      }

      @Override
      public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                       String[] exceptions) {
        return null;
      }
    };

    var finder = ModuleFinder.of(searchDirectory);
    for (var moduleReference : finder.findAll()) {
      try (var reader = moduleReference.open()) {
        for (var filename : (Iterable<String>) reader.list()::iterator) {
          if (!filename.endsWith(".class")) {
            continue;
          }
          try (var inputStream = reader.open(filename)
                                       .orElseThrow()) {
            var classReader = new ClassReader(inputStream);
            classReader.accept(myVisitor, 0);
          }
        }
      }
    }

    return entities;
  }

}



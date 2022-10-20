package com.github.magickoders.jar;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;
import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

  private static String entityName(String name) {
    return name.substring(name.lastIndexOf("/") + 1);
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
    var entityBuilder = new EntityBuilder();
    var myVisitor = new ClassVisitor(Opcodes.ASM9) {

      @Override
      public void visit(int version, int access, String name, String signature, String superName,
                        String[] interfaces) {

        if (AccessReader.isModule(access)) {
          return;
        }

        entityBuilder.setModifiers(AccessReader.modifiers(access))
                     .setTypeInfo(TypeInfo.of(name))
                     .setStereotype(AccessReader.stereotype(access));
      }

      @Override
      public RecordComponentVisitor visitRecordComponent(String name, String descriptor,
                                                         String signature) {
        return null;
      }

      @Override
      public FieldVisitor visitField(int access, String name, String descriptor, String signature,
                                     Object value) {

        if (AccessReader.isSynthetic(access)) {
          return null;
        }
        Set<Modifier> modifiers = AccessReader.modifiers(access);
        // print used for debugging
//        System.out.println(name);
//        System.out.println(descriptor);
//        System.out.println(signature);
//        System.out.println(modifiers);

        // FIXME TypeInfo of field is not set properly. need more documentation
        var field = new Field(modifiers, name, TypeInfo.of("TODO"));
        entityBuilder.addField(field);
        return null;
      }

      @Override
      public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                       String[] exceptions) {
        return null;
      }

      @Override
      public void visitEnd() {
        entityBuilder.build()
                     .ifPresent(entities::add);
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



package com.github.donnebelin.umldoc.classfile;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.AssociationDependency.Cardinality;
import com.github.forax.umldoc.core.AssociationDependency.Side;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Entity.Stereotype;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.Modifier;
import java.io.IOException;
import java.lang.constant.ClassDesc;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
  private final ArrayList<AssociationDependency> associations = new ArrayList<>();

  /**
   * Instantiate a new JarParser to parse all entities from a jar file.
   *
   * @throws IOException if I/O exception
   *                     occurred during parsing .class files
   */
  public JarParser() throws IOException {
    recoverEntitiesFromJar();
  }


  /**
   * Supplies a list of all Associations parsed from jar file.
   *
   * @return A list of AssociationDependency object
   *      with all parsed associations discovered in the jar file.
   */
  public List<AssociationDependency> getAssociationDependencies() {
    return List.copyOf(associations);
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

  private static Stereotype resolveStereotype(String superName) {
    if (superName.contains("Record")) {
      return Stereotype.RECORD;
    }

    if (superName.contains("Enum")) {
      return Stereotype.ENUM;
    }

    if (superName.contains("Interface")) {
      return Stereotype.INTERFACE;
    }

    return Stereotype.CLASS;
  }

  private static Set<Modifier> modifiers(int access) {
    var modifiers = new HashSet<Modifier>();

    if (java.lang.reflect.Modifier.isStatic(access)) {
      modifiers.add(Modifier.STATIC);
    }

    if (java.lang.reflect.Modifier.isFinal(access)) {
      modifiers.add(Modifier.FINAL);
    }

    if (java.lang.reflect.Modifier.isPublic(access)) {
      modifiers.add(Modifier.PUBLIC);
    } else if (java.lang.reflect.Modifier.isPrivate(access)) {
      modifiers.add(Modifier.PRIVATE);
    } else if (java.lang.reflect.Modifier.isProtected(access)) {
      modifiers.add(Modifier.PROTECTED);
    } else {
      modifiers.add(Modifier.PACKAGE);
    }

    return modifiers;
  }

  private void addFieldOrAssociation(
          int access,
          String name,
          String type,
          HashSet<Field> currentFields) {
    var field = new Field(
            modifiers(access),
            name.replace('$', '_'),
            type
    );
    var isAsso = false;
    int startIndex;
    int endIndex;
    while ((startIndex = type.indexOf("<")) >= 0 && (endIndex = type.lastIndexOf(">")) >= 0) {
      type = type.substring(startIndex + 1, endIndex);
      if (!type.contains("<")) {
        var tempType = type;
        var entityRight = entities.stream()
                .filter(entity -> entity.name().contains(tempType))
                .findFirst();

        if (entityRight.isPresent()) {
          isAsso = true;
          var left = new Side(
                  currentEntity,
                  Optional.empty(),
                  true,
                  Cardinality.ONLY_ONE
          );
          var right = new Side(
                  entityRight.get(),
                  Optional.of(field.name()),
                  true,
                  Cardinality.MANY
          );
          associations.add(new AssociationDependency(left, right));
        }
      }
    }

    if (!isAsso) {
      currentFields.add(field);
    }

    currentEntity = new Entity(
            currentEntity.modifiers(),
            currentEntity.name(),
            currentEntity.stereotype(),
            List.copyOf(currentFields),
            currentEntity.methods()
    );
    var oldEntity = entities.stream()
            .filter(entity -> entity.name().equals(currentEntity.name()))
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
                  name.replace('/', '_').replace('$', '_'),
                  resolveStereotype(superName),
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
        if (! name.contains("this")) {
          if (signature != null) {
            signature = signature.replaceAll(";", "").replaceAll("<", "</");
            addFieldOrAssociation(access, name, Arrays.stream(signature.split("/"))
                    .filter(part -> part.contains("<") || part.contains(">"))
                    .collect(Collectors.joining())
                    .replace('$', '_'), currentFields);
          } else {
            addFieldOrAssociation(
                    access,
                    name,
                    ClassDesc.ofDescriptor(descriptor).displayName(),
                    currentFields
            );
          }
        }

        return null;
      }

    }, 0);
  }
}

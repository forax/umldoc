package com.github.magickoders.jar;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Modifier;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.objectweb.asm.Opcodes;


/**
 * Utils class used to translate access code to umldoc.core classes.
 *
 * @see JarReader#getEntities(Path)
 */
public class AccessReader {

  private static final List<ModifierAssociation> modifiers = List.of(
          new ModifierAssociation(java.lang.reflect.Modifier.PUBLIC, Modifier.PUBLIC),
          new ModifierAssociation(java.lang.reflect.Modifier.PROTECTED, Modifier.PROTECTED),
          new ModifierAssociation(java.lang.reflect.Modifier.PRIVATE, Modifier.PRIVATE),
          new ModifierAssociation(java.lang.reflect.Modifier.FINAL, Modifier.FINAL),
          new ModifierAssociation(java.lang.reflect.Modifier.STATIC, Modifier.STATIC)
  );
  /**
   * The order is very important, an Interface is Abstract and an Annotation is an Interface ! An
   * enumeration can be abstract too.
   */
  private static final List<StereotypeAssociation> stereotypes = List.of(
          new StereotypeAssociation(Opcodes.ACC_ENUM, Entity.Stereotype.ENUM),
          new StereotypeAssociation(Opcodes.ACC_RECORD, Entity.Stereotype.RECORD),
          new StereotypeAssociation(Opcodes.ACC_ANNOTATION, Entity.Stereotype.ANNOTATION),
          new StereotypeAssociation(Opcodes.ACC_INTERFACE, Entity.Stereotype.INTERFACE),
          new StereotypeAssociation(Opcodes.ACC_ABSTRACT, Entity.Stereotype.ABSTRACT)
  );

  /**
   * return true the access code belongs to a module.
   */
  public static boolean isModule(int access) {
    return (access & Opcodes.ACC_MODULE) != 0;
  }

  /**
   * return true the access code belongs to a synthetic field (a field created by the compiler).
   */
  public static boolean isSynthetic(int access) {
    return (access & Opcodes.ACC_SYNTHETIC) != 0;
  }

  /**
   * returns the stereotype of a class from it's 'access' information. If nothing is found it
   * assumes it is a class.
   */
  public static Entity.Stereotype stereotype(int access) {
    return stereotypes.stream()
                      .filter(s -> (access & s.access()) != 0)
                      .map(StereotypeAssociation::stereotype)
                      .findFirst()
                      .orElse(Entity.Stereotype.CLASS);
  }

  /**
   * Returns a set of modifiers found in an 'access' code. There is no modifier for private-package
   * visibility.
   */
  public static Set<Modifier> modifiers(int access) {
    return modifiers.stream()
                    .filter(m -> (access & m.access()) != 0)
                    .map(ModifierAssociation::modifier)
                    .collect(Collectors.toUnmodifiableSet());
  }

  private record ModifierAssociation(int access, Modifier modifier) {
    ModifierAssociation {
      Objects.requireNonNull(modifier);
    }
  }

  private record StereotypeAssociation(int access, Entity.Stereotype stereotype) {
    StereotypeAssociation {
      Objects.requireNonNull(stereotype);
    }
  }

}

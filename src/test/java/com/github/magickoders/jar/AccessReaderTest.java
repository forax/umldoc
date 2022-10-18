package com.github.magickoders.jar;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Modifier;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;

public class AccessReaderTest {

  @Test
  public void stereotype() {
    assertEquals(Entity.Stereotype.RECORD, AccessReader.stereotype(Opcodes.ACC_RECORD));
    assertEquals(Entity.Stereotype.ENUM, AccessReader.stereotype(Opcodes.ACC_ENUM));
    assertEquals(Entity.Stereotype.INTERFACE, AccessReader.stereotype(Opcodes.ACC_INTERFACE));
    assertEquals(Entity.Stereotype.ANNOTATION, AccessReader.stereotype(Opcodes.ACC_ANNOTATION));
    assertEquals(Entity.Stereotype.ABSTRACT, AccessReader.stereotype(Opcodes.ACC_ABSTRACT));
  }

  @Test
  public void stereotypeOrdered() {
    var interfaceAccess = Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT;
    var annotationAccess = Opcodes.ACC_ANNOTATION | Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT;
    var abstractEnum = Opcodes.ACC_ABSTRACT | Opcodes.ACC_ENUM;
    assertEquals(Entity.Stereotype.INTERFACE, AccessReader.stereotype(interfaceAccess));
    assertEquals(Entity.Stereotype.ANNOTATION, AccessReader.stereotype(annotationAccess));
    assertEquals(Entity.Stereotype.ENUM, AccessReader.stereotype(abstractEnum));
  }

  @Test
  public void modifiers() {
    assertTrue(AccessReader.modifiers(java.lang.reflect.Modifier.PUBLIC)
                           .contains(Modifier.PUBLIC));
    assertTrue(AccessReader.modifiers(java.lang.reflect.Modifier.PROTECTED)
                           .contains(Modifier.PROTECTED));
    assertTrue(AccessReader.modifiers(java.lang.reflect.Modifier.PRIVATE)
                           .contains(Modifier.PRIVATE));
    assertTrue(AccessReader.modifiers(java.lang.reflect.Modifier.FINAL)
                           .contains(Modifier.FINAL));
    assertTrue(AccessReader.modifiers(java.lang.reflect.Modifier.STATIC)
                           .contains(Modifier.STATIC));
  }

  @Test
  public void multipleModifiers() {
    var access1 =
            java.lang.reflect.Modifier.FINAL | java.lang.reflect.Modifier.STATIC | java.lang.reflect.Modifier.PRIVATE;
    var modifiers1 = AccessReader.modifiers(access1);
    assertAll(
            () -> assertTrue(modifiers1.contains(Modifier.FINAL)),
            () -> assertTrue(modifiers1.contains(Modifier.STATIC)),
            () -> assertTrue(modifiers1.contains(Modifier.PRIVATE))
    );

    var access2 =
            java.lang.reflect.Modifier.STATIC | java.lang.reflect.Modifier.PUBLIC;
    var modifiers2 = AccessReader.modifiers(access2);
    assertAll(
            () -> assertTrue(modifiers2.contains(Modifier.STATIC)),
            () -> assertTrue(modifiers2.contains(Modifier.PUBLIC))
    );

    var access3 =
            java.lang.reflect.Modifier.PRIVATE | java.lang.reflect.Modifier.STATIC;
    var modifiers3 = AccessReader.modifiers(access3);
    assertAll(
            () -> assertTrue(modifiers3.contains(Modifier.PRIVATE)),
            () -> assertTrue(modifiers3.contains(Modifier.STATIC))
    );
  }

  @Test
  public void isSynthetic() {
    var access =
            java.lang.reflect.Modifier.STATIC | java.lang.reflect.Modifier.PRIVATE | java.lang.reflect.Modifier.FINAL | Opcodes.ACC_SYNTHETIC;
    assertTrue(AccessReader.isSynthetic(access));
  }

  @Test
  public void isModule() {
    var access = Opcodes.ACC_MODULE;
    assertTrue(AccessReader.isModule(access));
  }

}

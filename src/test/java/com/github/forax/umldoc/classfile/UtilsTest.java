package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.core.Entity.Stereotype;
import com.github.forax.umldoc.core.Modifier;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.objectweb.asm.Opcodes.*;

public class UtilsTest {
  @Test
  public void toModifiers() {
    assertAll(
        () -> assertEquals(Set.of(Modifier.PRIVATE), Utils.toModifiers(ACC_PRIVATE | ACC_INTERFACE)),
        () -> assertEquals(Set.of(Modifier.PROTECTED, Modifier.STATIC), Utils.toModifiers(ACC_PROTECTED | ACC_STATIC)),
        () -> assertEquals(Set.of(Modifier.PUBLIC, Modifier.FINAL), Utils.toModifiers(ACC_PUBLIC | ACC_FINAL)),
        () -> assertEquals(Set.of(), Utils.toModifiers(0))
    );
  }

  @Test
  public void toStereotype() {
    assertAll(
        () -> assertEquals(Stereotype.INTERFACE, Utils.toStereotype(ACC_PRIVATE | ACC_INTERFACE)),
        () -> assertEquals(Stereotype.ENUM, Utils.toStereotype(ACC_PROTECTED | ACC_ENUM)),
        () -> assertEquals(Stereotype.RECORD, Utils.toStereotype(ACC_PUBLIC | ACC_RECORD)),
        () -> assertEquals(Stereotype.ANNOTATION, Utils.toStereotype(ACC_STATIC | ACC_ANNOTATION)),
        () -> assertEquals(Stereotype.CLASS, Utils.toStereotype(0)),
        () -> assertEquals(Stereotype.CLASS, Utils.toStereotype(ACC_PUBLIC))
    );
  }

  @Test
  public void canNotBeInstantiated() throws InstantiationException, IllegalAccessException {
    var constructor = Utils.class.getDeclaredConstructors()[0];
    constructor.setAccessible(true);
    try {
      constructor.newInstance();
      fail();
    } catch (InvocationTargetException e) {
      assertInstanceOf(AssertionError.class, e.getCause());
    }
  }
}
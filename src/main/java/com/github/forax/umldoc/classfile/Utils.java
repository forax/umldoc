package com.github.forax.umldoc.classfile;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_ANNOTATION;
import static org.objectweb.asm.Opcodes.ACC_ENUM;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_RECORD;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

import com.github.forax.umldoc.core.AssociationDependency.Cardinality;
import com.github.forax.umldoc.core.Entity.Stereotype;
import com.github.forax.umldoc.core.Modifier;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;

class Utils {
  private record Pair<T>(int flag, T value) {
    public Optional<T> match(int access) {
      return Optional.of(value).filter(v -> (access & flag) != 0);
    }
  }

  private static <T> Pair<T> pair(int flag, T value) {
    return new Pair<>(flag, value);
  }

  // The order is important given that an interface is also marked abstract
  // adn an annotation is an interface
  private static final List<Pair<Stereotype>> STEREOTYPE_PAIRS = List.of(
      pair(ACC_RECORD, Stereotype.RECORD),
      pair(ACC_ENUM, Stereotype.ENUM),
      pair(ACC_ANNOTATION, Stereotype.ANNOTATION),
      pair(ACC_INTERFACE, Stereotype.INTERFACE),
      pair(ACC_ABSTRACT, Stereotype.ABSTRACT));

  private static final List<Pair<Modifier>> MODIFIER_PAIRS = List.of(
      pair(ACC_PUBLIC, Modifier.PUBLIC),
      pair(ACC_PRIVATE, Modifier.PRIVATE),
      pair(ACC_PROTECTED, Modifier.PROTECTED),
      pair(ACC_STATIC, Modifier.STATIC),
      pair(ACC_FINAL, Modifier.FINAL));

  private Utils() {
    throw new AssertionError();
  }

  public static Stereotype toStereotype(int access) {
    return STEREOTYPE_PAIRS.stream()
        .flatMap(pair -> pair.match(access).stream())
        .findFirst()
        .orElse(Stereotype.CLASS);
  }

  public static Set<Modifier> toModifiers(int access) {
    return MODIFIER_PAIRS.stream()
        .flatMap(pair -> pair.match(access).stream())
        .collect(Collector.of(() -> EnumSet.noneOf(Modifier.class),
                              EnumSet::add,
                              (s1, s2) -> {
                                throw new AssertionError();
                              }));
  }
}
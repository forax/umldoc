package com.github.pereiratostain;

import com.github.forax.umldoc.core.Entity;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.RecordComponentVisitor;


class Visitor extends ClassVisitor {
  private Entity entity = null;
  private Set<com.github.forax.umldoc.core.Modifier> modif = new HashSet<>();
  private String name;

  public Visitor(int api) {
    super(api);
  }


  private void buildEntity() {
    this.entity = new Entity(modif, name, Optional.empty(), new ArrayList<>(), new ArrayList<>());
  }

  public Entity getEntity() {
    if (entity == null) {
      this.buildEntity();
    }
    return this.entity;
  }

  @Override
  public void visit(int version, int access, String name, String signature,
                    String superName, String[] interfaces) {
    this.modif.add(modifier(access));
    var tmpName = name.substring(name.lastIndexOf("/") + 1);
    tmpName = tmpName.replace('-', '_');
    tmpName = tmpName.replace('$', ' ');
    this.name = tmpName;
  }

  /**
   * Converts an int to a Modifier.
   *
   * @param int The int to convert
   * @return A value of the enum Modifier
   */
  private com.github.forax.umldoc.core.Modifier modifier(int modifier) {
    return switch (modifier) {
      case Modifier.PUBLIC -> com.github.forax.umldoc.core.Modifier.PUBLIC;
      case Modifier.PRIVATE -> com.github.forax.umldoc.core.Modifier.PRIVATE;
      case Modifier.PROTECTED -> com.github.forax.umldoc.core.Modifier.PROTECTED;
      default -> com.github.forax.umldoc.core.Modifier.PACKAGE;
    };
  }

  @Override
  public RecordComponentVisitor visitRecordComponent(String name, String descriptor,
                                                     String signature) {
    return null;
  }

  @Override
  public FieldVisitor visitField(int access, String name, String descriptor, String signature,
                                 Object value) {
    return null;
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                   String[] exceptions) {
    return null;
  }
}

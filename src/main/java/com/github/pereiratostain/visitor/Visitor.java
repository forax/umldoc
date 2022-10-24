package com.github.pereiratostain.visitor;

import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Entity.Stereotype;
import com.github.forax.umldoc.core.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.github.forax.umldoc.core.TypeInfo;
import org.objectweb.asm.*;
import org.objectweb.asm.signature.SignatureReader;


public class Visitor extends ClassVisitor {
  private Entity entity = null;

  public Visitor(int api) {
    super(api);
  }

  static String removePath(String className) {
    return className.substring(className.lastIndexOf('/') + 1);
  }

  public Entity getEntity() {
    return this.entity;
  }

  private Stereotype translateStereotype(String stereotype) {
    return switch (stereotype) {
      case "Enum" -> Stereotype.ENUM;
      case "Record" -> Stereotype.RECORD;
      case "Interface" -> Stereotype.INTERFACE;
      default -> Stereotype.CLASS;
    };
  }

  @Override
  public void visit(int version, int access, String name, String signature,
                    String superName, String[] interfaces) {

    var modif = new HashSet<com.github.forax.umldoc.core.Modifier>();
    modif.add(modifier(access));

    name = removePath(name);
    name = name.replace('-', '_');
    name = name.replace('$', ' ');

    var stereotype = Stereotype.CLASS;
    if (superName != null) {
      superName = removePath(superName);
      stereotype = translateStereotype(superName);
    }
    this.entity = new Entity(modif, TypeInfo.of(name), stereotype,
            List.of(), List.of());
  }

  /**
   * Converts an int to a Modifier.
   *
   * @param modifier The int to convert
   * @return A value of the enum Modifier
   */
  private static com.github.forax.umldoc.core.Modifier modifier(int modifier) {
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
    // Check if the field is Synthetic
    if ((access & ACC_SYNTHETIC) == ACC_SYNTHETIC) {
      return null;
    }
    var modifier = new HashSet<com.github.forax.umldoc.core.Modifier>();
    modifier.add(modifier(access));

    var type = TypeInfo.of(removePath(descriptor).replace(';', ' '));
    if (signature != null) {
      var reader = new SignatureReader(signature);
      var visitor = new Signature(Opcodes.ASM9);
      reader.acceptType(visitor);
      type = visitor.getInfo();
    }
    var fields = new ArrayList<>(this.entity.fields());
    var field = new Field(modifier, name, type);

    fields.add(field);
    this.entity = new Entity(entity.modifiers(), entity.type(), entity.stereotype(), fields,
            List.of());
    return null;
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                   String[] exceptions) {
    return null;
  }
}

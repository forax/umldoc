package com.github.pereiratostain.visitor;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_ANNOTATION;
import static org.objectweb.asm.Opcodes.ACC_ENUM;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_RECORD;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Entity.Stereotype;
import com.github.forax.umldoc.core.Field;
import com.github.forax.umldoc.core.TypeInfo;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.signature.SignatureReader;


/**
 * Class use to parse the .class files into Entity.
 */
public class ClassParser extends ClassVisitor {
  private Entity entity = null;

  public ClassParser(int api) {
    super(api);
  }

  public Entity getEntity() {
    return this.entity;
  }

  private Stereotype translateStereotype(int flag) {
    if ((flag & ACC_ENUM) == ACC_ENUM) {
      return Stereotype.ENUM;
    } else if ((flag & ACC_RECORD) == ACC_RECORD) {
      return Stereotype.RECORD;
    } else if ((flag & ACC_ANNOTATION) == ACC_ANNOTATION) {
      return Stereotype.ANNOTATION;
    } else if ((flag & ACC_INTERFACE) == ACC_INTERFACE) {
      return Stereotype.INTERFACE;
    } else if ((flag & ACC_ABSTRACT) == ACC_ABSTRACT) {
      return Stereotype.ABSTRACT;
    } else {
      return Stereotype.CLASS;
    }
  }

  @Override
  public void visit(int version, int access, String name, String signature,
                    String superName, String[] interfaces) {

    var modif = new HashSet<com.github.forax.umldoc.core.Modifier>();
    modif.add(modifier(access));

    var stereotype = translateStereotype(access);
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

    var type = TypeInfo.of(descriptor);
    if (signature != null) {
      var reader = new SignatureReader(signature);
      var visitor = new SignatureParser(Opcodes.ASM9);
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

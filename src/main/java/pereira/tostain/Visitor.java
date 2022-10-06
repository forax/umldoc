package pereira.tostain;

import com.github.forax.umldoc.core.Entity;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.RecordComponentVisitor;

import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.Modifier;
import java.util.*;

class Visitor extends ClassVisitor {
    private Entity entity = null;
    private Set<com.github.forax.umldoc.core.Modifier> modif = new HashSet<>();
    private String name;
    private Optional<String> stereotype;

    protected Visitor(int api) {
        super(api);
    }
    private static String modifier(int access) {
        if (Modifier.isPublic(access)) {
            return "+";
        }
        if (Modifier.isPrivate(access)) {
            return "-";
        }
        if (Modifier.isProtected(access)) {
            return "#";
        }
        return "";
    }

    private void buildEntity() {
        this.entity = new Entity(modif, name, null, null, null);
    }

    public Entity getEntity() {
        if(entity == null) {
            this.buildEntity();
        }
        return this.entity;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.err.println("class " + modifier(access) + " " + name + " " + superName + " " + (interfaces != null? Arrays.toString(interfaces): ""));
        this.modif.add(Main.modifier(modifier(access)));
        this.name = name.substring(name.lastIndexOf("/") + 1);
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        System.err.println("  component " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName());
        return null;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        System.err.println("  field " + modifier(access) + " " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName() + " " + signature);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.err.println("  method " + modifier(access) + " " + name + " " + MethodTypeDesc.ofDescriptor(descriptor).displayDescriptor() + " " + signature);
        return null;
    }
}

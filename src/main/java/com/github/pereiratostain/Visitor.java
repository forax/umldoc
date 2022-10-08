package com.github.pereiratostain;

import com.github.forax.umldoc.core.Entity;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.RecordComponentVisitor;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
        this.entity = new Entity(modif, name, Optional.empty(), new ArrayList<>(), new ArrayList<>());
    }

    public Entity getEntity() {
        if(entity == null) {
            this.buildEntity();
        }
        return this.entity;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        var modifier = Main.modifier(modifier(access));
        if (modifier != null) {
            this.modif.add(Main.modifier(modifier(access)));
        }
        var tmpName = name.substring(name.lastIndexOf("/") + 1);
        tmpName = tmpName.replace('-', '_');
        tmpName = tmpName.replace('$', ' ');
        this.name = tmpName;
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        return null;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return null;
    }
}

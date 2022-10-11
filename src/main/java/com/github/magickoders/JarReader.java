package com.github.magickoders;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Modifier;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A class used to retrieve the content of a jar.
 * It contains a static method {@link #getEntities()} which returns the entities contained in the directory "target"
 */
public class JarReader {

    /**
     * A class that contains the list of entities by using the ClassVisitor's methods
     */
    private static class MyVisitor extends ClassVisitor { // bwaaa de l'héritage, mais c'est une class abstraite donc ok j'imagine

        private final List<Entity> entities = new ArrayList<>();


        public List<Entity> getEntities() {
            return entities;
        }

        protected MyVisitor(int api) {
            super(api);
        }


        /**
         * visit a class and give its characteristics in parameters.
         * Add the class in the list of Entities to retrieve them later.
         *
         * @param version not used here
         * @param access the flags for the modifiers and the archetype
         * @param name the fully qualified name of the class
         * @param signature not used here
         * @param superName not used here
         * @param interfaces not used here
         */
        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            var entity = new Entity(getModifiers(access), name.replace("/", "."), getStereotype(access), List.of(), List.of());
            entities.add(entity);
        }


        /**
         * method that returns the modifiers from the access obtained by a visit method of {@link MyVisitor}
         *
         * @param access the value obtained by {@link #visit(int, int, String, String, String, String[])}
         * @return a set of modifiers which can contain private, static, public...
         * @see Modifier
         */
        private static Set<Modifier> getModifiers(int access) {
            var modifiers = new HashSet<Modifier>();
            if (java.lang.reflect.Modifier.isPublic(access)) {
                modifiers.add(Modifier.PUBLIC);
            } else if (java.lang.reflect.Modifier.isPrivate(access)) {
                modifiers.add(Modifier.PRIVATE);
            } else if (java.lang.reflect.Modifier.isProtected(access)) {
                modifiers.add(Modifier.PROTECTED);
            } else {
                modifiers.add(Modifier.PACKAGE);
            }

            if ((access & Opcodes.ACC_STATIC) != 0) {
                modifiers.add(Modifier.STATIC);
            }
            // TODO à finir avec les static et sealed...
            return Set.copyOf(modifiers);
        }

        /**
         * method that returns the specific stereotype of a class. If it is a simple class, returns Optional.empty()
         *
         * @param access the value obtained by {@link #visit(int, int, String, String, String, String[])}
         * @return an Optional which contains the stereotype of the class. Optional.empty() if it is a simple class
         */
        private static Optional<String> getStereotype(int access) {
            if ((access & Opcodes.ACC_RECORD) != 0) {
                return Optional.of("record");
            } else if ((access & Opcodes.ACC_INTERFACE) != 0) {
                return Optional.of("interface");
            } else if ((access & Opcodes.ACC_ENUM) != 0) {
                return Optional.of("enum");
            } else if ((access & Opcodes.ACC_ANNOTATION) != 0) {
                return Optional.of("annotation");
            } else {
                return Optional.empty();
            }
        }
    }

    /**
     * This method returns the list of entities in the directory "target"
     *
     * @return the list of entities contained in the directory "target"
     * @throws IOException
     *
     * @see #getEntities(String)
     */
    public static List<Entity> getEntities() throws IOException {
        return getEntities("target");
    }


    /**
     * This method returns the list of entities in the given directory
     *
     * @param directory the directory to explore
     * @return the list of entities contained in the given directory
     *
     * @throws IOException
     */
    public static List<Entity> getEntities(String directory) throws IOException {
        Objects.requireNonNull(directory, "directory cannot be null");
        var path = Path.of(directory);
        var finder = ModuleFinder.of(path);
        var myVisitor = new MyVisitor(Opcodes.ASM9);

        for(var moduleReference: finder.findAll()) {
            try(var reader = moduleReference.open()) {
                for(var filename: (Iterable<String>) reader.list()::iterator) {
                    if (!filename.endsWith(".class")) {
                        continue;
                    }
                    // ouvre la classe
                    try(var inputStream = reader.open(filename).orElseThrow()) {
                        var classReader = new ClassReader(inputStream);
                        classReader.accept(myVisitor, 0);
                    }
                }
            }
        }

        return myVisitor.getEntities();
    }
}



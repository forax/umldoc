package com.github.donnebelin.umldoc.core;

import java.io.IOException;
import java.nio.file.Path;
import java.lang.module.ModuleFinder;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.github.forax.umldoc
        .core.Entity;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Class to parse a jar file.
 * This class uses ASM library to recover entities, fields and methods from .class files of the given jar.
 */
public final class JarParser {
    private static void getASMData(ClassReader classReader, HashSet<Entity> entities) {
        classReader.accept(new ClassVisitor(Opcodes.ASM9) {

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                if(! name.equals("module-info")) {
                    entities.add(new Entity(
                            Set.of(),
                            name.replace('/', '_').replace('$', '_'),
                            Optional.empty(),
                            List.of(),
                            List.of())
                    );
                }
            }
        }, 0);
    }

    private static void recoverEntitiesFromJar(HashSet<Entity> entities) throws IOException {
        var path = Path.of("target");
        var finder = ModuleFinder.of(path);
        for(var moduleReference: finder.findAll()) {
            try (var reader = moduleReference.open()) {
                for (var filename : (Iterable<String>) reader.list()::iterator) {
                    if (!filename.endsWith(".class")) {
                        continue;
                    }
                    try (var inputStream = reader.open(filename).orElseThrow()) {
                        var classReader = new ClassReader(inputStream);
                        getASMData(classReader, entities);
                    }
                }
            }
        }
    }

    /**
     * Recover all Entities from .class files and put them in a List<Entity>
     *
     * @return List<Entity> with Entity instances
     * @throws IOException if I/O exception occurred during parsing .class files
     */
    public static List<Entity> getEntities() throws IOException {
       var entities = new HashSet<Entity>();
        recoverEntitiesFromJar(entities);
        return List.copyOf(entities);
    }
}

package com.github.pereiratostain;

import com.github.forax.umldoc.core.Entity;
import com.github.pereiratostain.generator.MermaidSchemaGenerator;
import org.objectweb.asm.*;
import com.github.forax.umldoc.core.Modifier;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            var entities = asm();
            try (var writer = new OutputStreamWriter(System.out)) {
                var generator = new MermaidSchemaGenerator(writer);
                generator.generate(entities);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Modifier modifier(String string) {
        return switch (string) {
            case "+" -> Modifier.PUBLIC;
            case "-" -> Modifier.PRIVATE;
            case "#" -> Modifier.PACKAGE;
            default -> null;
        };
    }

    public static List<Entity> asm() throws IOException {
        var path = Path.of("target");
        var finder = ModuleFinder.of(path);
        for(var moduleReference: finder.findAll()) {
            try(var reader = moduleReference.open()) {
                var visitors = new ArrayList<Visitor>();
                for(var filename: (Iterable<String>) reader.list()::iterator) {
                    if (!filename.endsWith(".class")) {
                        continue;
                    }
                    try(var inputStream = reader.open(filename).orElseThrow()) {
                        var classReader = new ClassReader(inputStream);
                        var visitor = new Visitor(Opcodes.ASM9);

                        classReader.accept(visitor, 0);
                        visitors.add(visitor);
                    }
                }
                return visitors.stream().map(Visitor::getEntity).toList();
            }
        }
        return null;
    }
}

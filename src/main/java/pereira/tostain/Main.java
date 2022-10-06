package pereira.tostain;

import com.github.forax.umldoc.core.Entity;
import org.objectweb.asm.*;
import com.github.forax.umldoc.core.Modifier;

import java.io.IOException;
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
            var entities = Main.asm();
            for(var entity : entities) {
                System.out.println(entity);
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

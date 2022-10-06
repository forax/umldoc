package com.github.veluvexiau.umldoc.core;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.module.ModuleFinder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
       /* if (args.length<1){
            throw new IllegalStateException("no path spécified");
        }*/
        var path = Path.of("target");
        var entityFromJar = readJarFile(path);
        displayEntitiesMarmaid(entityFromJar);
    }
    private static void initMermaidFile(PrintWriter writer){
        writer.println("```mermaid");
        writer.println("%% umldoc");
        writer.println("classDiagram\n\tdirection TB");
    }
    
    private static String getNameFromPath(String path){
        String name = path;
        int temp = name.lastIndexOf('/');
        name = name.substring(temp+1);

        //In the case of a record inside an other record for exemple.
        Pattern p = Pattern.compile("\\$") ;
        Matcher m = p.matcher(name) ;
        if (m.find()) {
            name = m.replaceAll("_");
        }

        return name;
    }
    
    
    private static void displayEntitiesMarmaid(List<Entity> entities) throws IOException {
        String pathToString = "./src/main/java/com/github/veluvexiau/umldoc/core/marmaidExport.md";
        PrintWriter writer = new PrintWriter(pathToString, Charset.defaultCharset());
        initMermaidFile(writer);
        for (Entity entity : entities){
            writer.println("\tclass "+getNameFromPath(entity.name())+"{");
            for (Method method : entity.methods()){
                writer.println("\t\t"+method.name());
            }
            writer.println("\t}\n");

        }
        writer.println("```");
        writer.close();
    }


    private static List<Entity> readJarFile(Path path) throws IOException {

        var entities = new ArrayList<Entity>();

        var finder = ModuleFinder.of(path);


        for(var moduleReference: finder.findAll()) {
            try(var reader = moduleReference.open()) {
                for(var filename: (Iterable<String>) reader.list()::iterator) {
                    if (!filename.endsWith(".class")) {
                        continue;
                    }
                    try(var inputStream = reader.open(filename).orElseThrow()) {
                        var classReader = new ClassReader(inputStream);
                        classReader.accept(new ClassVisitor(Opcodes.ASM9) {

                            private static Modifier modifier(int access) {
                                if (java.lang.reflect.Modifier.isPublic(access)) {
                                    return Modifier.PUBLIC;
                                }
                                if (java.lang.reflect.Modifier.isPrivate(access)) {
                                    return Modifier.PRIVATE;
                                }
                                if (java.lang.reflect.Modifier.isProtected(access)) {
                                    return Modifier.PROTECTED;
                                }
                                return null;
                               // throw new IllegalStateException("Modifier is neither Public or Private or Protected" + access);
                            }

                            @Override
                            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

                                var entity = new Entity(Set.of(),name, Optional.ofNullable(superName),List.of(),List.of());
                                System.out.println("class " +  modifier(access)+ " " + name+ " " + superName + " " + (interfaces != null? Arrays.toString(interfaces): ""));
                                entities.add(entity);
                            }

                            @Override
                            public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
                                System.out.println("  component " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName());
                                return null;
                            }

                            @Override
                            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                                System.out.println("  field " + modifier(access) + " " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName() + " " + signature);
                                return null;
                            }

                            @Override
                            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                                System.out.println("  method " + modifier(access) + " " + name + " " + MethodTypeDesc.ofDescriptor(descriptor).displayDescriptor() + " " + signature);
                                return null;
                            }
                        }, 0);
                    }
                }
            }
        }
        return entities;

    }
}

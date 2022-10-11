package com.github.donnebelin.umldoc.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import com.github.forax.umldoc.gen.MermaidGenerator;
import com.github.forax.umldoc.gen.PlantUmlGenerator;

/**
 * Contains the main method for this package
 */
public class Main {
    /**
     * main method of this package
     *
     * @param args The path of the file for which the program will write Plant UML and Mermaid UML diagrams
     * @throws IOException if I/O exception occurred during parsing
     */
    public static void main(String[] args) throws IOException {
        var entities = JarParser.getEntities();  // .stream().filter(entity -> ! entity.name().equals("module-info")).toList();
        var filePath = Path.of(args[0]);
        var plantUMLGenerator = new PlantUmlGenerator();
        var mermaidGenerator = new MermaidGenerator();

        try(var writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            plantUMLGenerator.generate(true, entities, List.of(), writer);
            writer.append('\n').append('\n').append("```mermaid").append('\n') ;
            mermaidGenerator.generate(true, entities, List.of(), writer);
            writer.append("```");
        }
    }
}

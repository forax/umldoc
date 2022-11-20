package com.github.forax.umldoc.editor;

import com.github.forax.umldoc.core.Package;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditorTest {

    @Test
    public void readWriteShouldReturnSearchCommandLine() throws IOException {
        var map = new HashMap<String, CommandLineParser>();
        map.put("test", new MermaidCmdLineParser());
        var editor = new Editor(map, List.of());
        var line = "```test";
        var result = editor.readWrite(line, new StringWriter());
        assertEquals(Editor.State.SEARCHCOMMANDLINE, result);
    }

    @Test
    public void readWriteShouldReturnReadWrite() throws IOException {
        var map = new HashMap<String, CommandLineParser>();
        map.put("test", new MermaidCmdLineParser());
        var editor = new Editor(map, List.of());
        var line = "```notest:(";
        var result = editor.readWrite(line, new StringWriter());
        assertEquals(Editor.State.READWRITE, result);
    }

    @Test
    public void searchCommandLineShouldReturnReadOnly() throws IOException {
        var map = new HashMap<String, CommandLineParser>();
        map.put("test", new MermaidCmdLineParser());
        var testPackage = new Package("test", List.of(), List.of());
        var editor = new Editor(map, List.of(testPackage));
        var line = "```test";
        var parserSetter = editor.readWrite(line, new StringWriter());
        assertEquals(Editor.State.SEARCHCOMMANDLINE, parserSetter);
        var secondLine = "%% umldoc -p test -t test";
        var result = editor.searchCommandLine(secondLine, new StringWriter());
        assertEquals(Editor.State.READONLY, result);
    }

    @Test
    public void searchCommandLineShouldReturnReadWrite() throws IOException {
        var map = new HashMap<String, CommandLineParser>();
        map.put("test", new MermaidCmdLineParser());
        var editor = new Editor(map, List.of());
        var line = "```test";
        var parserSetter = editor.readWrite(line, new StringWriter());
        assertEquals(Editor.State.SEARCHCOMMANDLINE, parserSetter);
        var secondLine = "```";
        var result = editor.searchCommandLine(secondLine, new StringWriter());
        assertEquals(Editor.State.READWRITE, result);
    }

    @Test
    public void searchCommandLineShouldReturnSearchCommandLine() throws IOException {
        var map = new HashMap<String, CommandLineParser>();
        map.put("test", new MermaidCmdLineParser());
        var editor = new Editor(map, List.of());
        var line = "```test";
        var parserSetter = editor.readWrite(line, new StringWriter());
        assertEquals(Editor.State.SEARCHCOMMANDLINE, parserSetter);
        var secondLine = "hello :)";
        var result = editor.searchCommandLine(secondLine, new StringWriter());
        assertEquals(Editor.State.SEARCHCOMMANDLINE, result);
    }

    @Test
    public void readOnlyShouldReturnReadWrite() throws IOException {
        var map = new HashMap<String, CommandLineParser>();
        map.put("test", new MermaidCmdLineParser());
        var testPackage = new Package("test", List.of(), List.of());
        var editor = new Editor(map, List.of(testPackage));
        var line = "```test";
        var parserSetter = editor.readWrite(line, new StringWriter());
        assertEquals(Editor.State.SEARCHCOMMANDLINE, parserSetter);
        var secondLine = "%% umldoc -p test -t test";
        var lineParser = editor.searchCommandLine(secondLine, new StringWriter());
        assertEquals(Editor.State.READONLY, lineParser);
        var finalLine = "```";
        var result = editor.readOnly(finalLine, new StringWriter());
        assertEquals(Editor.State.READWRITE, result);
    }

    @Test
    public void readOnlyShouldReturnReadOnly() throws IOException {
        var map = new HashMap<String, CommandLineParser>();
        map.put("test", new MermaidCmdLineParser());
        var testPackage = new Package("test", List.of(), List.of());
        var editor = new Editor(map, List.of(testPackage));
        var line = "```test";
        var parserSetter = editor.readWrite(line, new StringWriter());
        assertEquals(Editor.State.SEARCHCOMMANDLINE, parserSetter);
        var secondLine = "%% umldoc -p test -t test";
        var lineParser = editor.searchCommandLine(secondLine, new StringWriter());
        assertEquals(Editor.State.READONLY, lineParser);
        var finalLine = "Hello this is not the end !!!";
        var result = editor.readOnly(finalLine, new StringWriter());
        assertEquals(Editor.State.READONLY, result);
    }
}

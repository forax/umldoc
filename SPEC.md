
# UMLdoc

Generates different UML diagrams that can be embedded directly in Markdown documents

```
       snippets in markdown files
                  +
             .class files
                  ↓
                models
                  ↓
   diagrams (class, sequence, component)    
```

### Provides
- a command line  (`java -jar ...`), parameters (to be defined)
- a maven plugin

### Supported diagrams
- [class diagram](https://en.wikipedia.org/wiki/Class_diagram)  (class/interface + public/protected methods)
    - interface, class, enum, record, annotation, sealed class?
    - inheritance, implementation, generics?
    - associations (unary, nary(array, list, map?))
- [sequence diagram](https://en.wikipedia.org/wiki/Sequence_diagram)  (method calls)
    - method lifecycle
- [component diagram](https://en.wikipedia.org/wiki/Component_diagram)  (Maven artifact dependencies)
    - declared / implicit

### Output formats
- plantUML (https://plantuml.com/)
- mermaidjs (https://mermaid-js.github.io/mermaid/)
- GraphViz ? (https://graphviz.org/)

### Snippets

Snippets are section of text that are replaced by a diagram by umldoc.

There are 2 kinds of snippets
- a file with extensions ".pu", ".plantuml" for plantuml file (respectively ".mmd", ".mermaid" for mermaidjs)
- a block open/closed with 3 backticks inside a markdown file (".md")

In both cases, the idea is to use a comment line to indicate a section that should be auto-generated,
for plantuml, a line comment starts with a quote (') while for mermaidjs a line comment starts
with a %%. It must be followed by "umldoc" and the command indicating which kind of diagram
should be constructed ("class", "sequence", "component") with some optional parameter

This pattern seems simple enough for a human to understand and a line scanner to find.
When the diagram is generated, the comment stay at the top so it can be generated again.

#### file with an extension

For a plantUML file
```
@startuml
' umldoc ... 
@enduml
```

For a mermaidjs file
```
%% umldoc ...
```

#### backticks block inside a markdown file

For plantUML
````
```plantuml
@startuml
' umldoc ...
@enduml
```
````

For mermaidjs
````
```mermaid
%% umldoc ...
```
````

### Calendar

* September, 27th kickoff meeting
* October, 4th 
* October, 11th Autonomy
* October, 18th
* October, 25th
* November, 8th
* November, 15th

### Resources
- Libraries
    - Class File parsing
      ASM
      https://asm.ow2.io/asm4-guide.pdf

    - POM Parsing
      JDK XML SAX Parser (streaming XML parser)
      https://docs.oracle.com/en/java/javase/17/docs/api/java.xml/module-summary.html
      https://docs.oracle.com/en/java/javase/17/docs/api/java.xml/org/xml/sax/helpers/DefaultHandler.html

    - Maven Artifact Resolver (ex-Aether)
      https://maven.apache.org/resolver/index.html
      https://wiki.eclipse.org/Aether/Resolving_Dependencies
  
    - Command line parsing
      https://picocli.info/

- Articles
    - How to integrate UML diagrams into GitLab or GitHub
      https://stackoverflow.com/questions/32203610/how-to-integrate-uml-diagrams-into-gitlab-or-github

    - A Guide to Java bytecode
      https://www.baeldung.com/java-asm

    - Real-World Bytecode Handling with ASM
      https://blogs.oracle.com/javamagazine/post/real-world-bytecode-handling-with-asm

    - Parsing an XML File Using SAX Parser
      https://www.baeldung.com/java-sax-parser

    - How to create a Maven Plugin
      https://www.baeldung.com/maven-plugin
  
    - Intro to ...
      Jacoco: https://www.baeldung.com/jacoco
      FindBugs: https://www.baeldung.com/intro-to-findbugs
      

- Related GitHub projects
    - UML reverse mapper
      https://github.com/iluwatar/uml-reverse-mapper
      https://github.com/NitorCreations/DomainReverseMapper

    - UML java doclet
      https://github.com/gboersma/uml-java-doclet





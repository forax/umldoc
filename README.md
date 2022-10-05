# umldoc
Automatically generates documentation with UML diagrams using either plantUML or mermaidjs 

What we want, [SPEC.md](SPEC.md).

Sessions
1. Understanding the domain and prototyping library examples
   - try to model the core classes [design/ClassDiagram.md](design/ClassDiagram.md) on a blackboard
   - [src/test/java/com/github/forax/umldoc/classfile/ASMTest.java](ASMTest.java) how to use ASM
2. Creates the skeleton of the applications
   - extract the entities from a jar and generate the corresponding plantuml/mermaid file
   - write tests and static analysis (CheckStyle: [code convention](https://google.github.io/styleguide/javaguide.html) verifier, Jacoco: test code coverage) 
3. Implements the hard stuff first
   - ???


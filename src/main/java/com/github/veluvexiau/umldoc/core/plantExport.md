```plantuml
@startuml
' umldoc
	class module-info{
	}

	class JarParser{
	}

	class Main{
	}

	class JarParser_1{
	}

	class Main{
	}

	class MermaidSchemaGenerator_1{
	}

	class PlantUmlSchemaGenerator{
	}

	class PlantUmlSchemaGenerator_1{
	}

	class Generator{
	}

	class MermaidSchemaGenerator{
	}

	class Visitor{
		Entity : entity
	}

	class Hello{
	}

	class ClassFileParser_1{
	}

	class ModuleScrapper{
	}

	class ClassFileParser{
	}

	class ClassFileParser_EntityBuilder{
		String : name
	}

	class PlantUmlGenerator{
	}

	class Generator{
	}

	class MermaidGenerator{
	}

	class Entity{
		<<Record>>
		Set : modifiers
		String : name
		Optional : stereotype
		List : fields
		List : methods
	}

	class Method{
		<<Record>>
		Set : modifiers
		String : name
		String : returnType
		List : parameters
	}

	class Field{
		<<Record>>
		Set : modifiers
		String : name
		String : type
	}

	class AssociationDependency{
		<<Record>>
		AssociationDependency$Side : left
		AssociationDependency$Side : right
	}

	class AssociationDependency_Side{
		<<Record>>
		Entity : entity
		boolean : navigability
		String : cardinality
	}

	class SubtypeDependency{
		<<Record>>
		Entity : supertype
		Entity : subtype
	}

	class Modifier{
		<<Enum>>
		Modifier : PRIVATE
		Modifier : PACKAGE
		Modifier : PROTECTED
		Modifier : PUBLIC
		Modifier : STATIC
		Modifier : SEALED
		Modifier[] : $VALUES
	}

	class Method_Parameter{
		<<Record>>
		String : name
		String : type
	}

	class Package{
		<<Record>>
		String : name
		List : entities
		List : dependencies
	}

	class Dependency{
	}

	class PlantUMLExtract{
	}

	class MermaidExtract{
	}

	class Main{
	}

	class Extract{
	}

	class Main_1{
	}

@enduml
```

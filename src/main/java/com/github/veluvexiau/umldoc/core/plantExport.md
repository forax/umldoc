```plantuml
@startuml
' umldoc
	class MermaidGenerator{
		<init>
		escapeField
		generate
	}

	class Generator{
		fieldAccessor
		fieldToString
		generate
	}

	class PlantUmlGenerator{
		<init>
		generate
	}

	class Main{
		<init>
		main
		lambda$main$0
	}

	class Helper_1{
	}

	class JarParser_1{
		visit
		visitField
		lambda$visitField$0
	}

	class JarParser{
		HashSet : entities
		Entity : currentEntity
		ArrayList : associations
		<init>
		getAssociationDependencies
		entities
		recoverEntitiesFromJar
		resolveStereotype
		modifiers
		addFieldOrAssociation
		getAsmData
		lambda$addFieldOrAssociation$1
		lambda$addFieldOrAssociation$0
	}

	class Helper{
		<init>
		parseCardinalities
	}

	class Main{
		<init>
		main
		asm
	}

	class MermaidSchemaGenerator_1{
	}

	class Generator{
		generate
	}

	class PlantUmlSchemaGenerator{
		<init>
		generate
		generateEntity
		modifierToString
	}

	class PlantUmlSchemaGenerator_1{
	}

	class MermaidSchemaGenerator{
		<init>
		generate
		generateHeader
		generateEntity
		computeFieldsEnum
		computeFieldsClass
		generateFields
		generateRecordFields
		generateAssociations
		modifierToString
		lambda$generateAssociations$2
		lambda$generateRecordFields$1
		lambda$generateFields$0
	}

	class Visitor{
		Entity : entity
		<init>
		removePath
		getEntity
		translateStereotype
		visit
		modifier
		visitRecordComponent
		visitField
		visitMethod
	}

	class MermaidExtract{
		<init>
		generate
		init
		end
		displayEntity
		methodsAndParameters
	}

	class PlantUmlExtract{
		<init>
		generate
		init
		end
		displayEntity
	}

	class ExtractMethod{
		<init>
		getNameFromPath
		getStereotype
		typeOfList
		parameterWithType
		lambda$parameterWithType$1
		lambda$typeOfList$0
	}

	class Main{
		<init>
		main
	}

	class ClassFileParser{
		<init>
		readJarFile
	}

	class ClassFileParser_1{
		modifier
		visit
		visitRecordComponent
		visitField
		visitMethod
	}

	class MermaidGenerator{
		<init>
		generate
	}

	class Generator{
		generate
	}

	class PlantUmlGenerator{
		<init>
		generate
	}

	class Dependency{
	}

	class Method{
		Set : modifiers
		String : name
		TypeInfo : returnTypeInfo
		List : parameters
		<init>
		<init>
		returnType
		toString
		hashCode
		equals
		modifiers
		name
		returnTypeInfo
		parameters
	}

	class Modifier{
		Modifier : PRIVATE
		Modifier : PACKAGE
		Modifier : PROTECTED
		Modifier : PUBLIC
		Modifier : STATIC
		Modifier : FINAL
		Modifier[] : $VALUES
		values
		valueOf
		<init>
		$values
	}

	class Field{
		Set : modifiers
		String : name
		TypeInfo : typeInfo
		<init>
		<init>
		type
		toString
		hashCode
		equals
		modifiers
		name
		typeInfo
	}

	class Method_Parameter{
		String : name
		TypeInfo : typeInfo
		<init>
		<init>
		type
		toString
		hashCode
		equals
		name
		typeInfo
	}

	class SubtypeDependency{
		Entity : supertype
		Entity : subtype
		<init>
		toString
		hashCode
		equals
		supertype
		subtype
	}

	class Entity{
		Set : modifiers
		TypeInfo : type
		Entity$Stereotype : stereotype
		List : fields
		List : methods
		<init>
		<init>
		name
		toString
		hashCode
		equals
		modifiers
		type
		stereotype
		fields
		methods
	}

	class Entity_Stereotype{
		Entity$Stereotype : CLASS
		Entity$Stereotype : INTERFACE
		Entity$Stereotype : ANNOTATION
		Entity$Stereotype : ENUM
		Entity$Stereotype : RECORD
		Entity$Stereotype : ABSTRACT
		Entity$Stereotype[] : $VALUES
		values
		valueOf
		<init>
		$values
	}

	class AssociationDependency{
		AssociationDependency$Side : left
		AssociationDependency$Side : right
		<init>
		toString
		hashCode
		equals
		left
		right
	}

	class AssociationDependency_Cardinality{
		AssociationDependency$Cardinality : ONLY_ONE
		AssociationDependency$Cardinality : ZERO_OR_ONE
		AssociationDependency$Cardinality : MANY
		AssociationDependency$Cardinality[] : $VALUES
		values
		valueOf
		<init>
		$values
	}

	class Package{
		String : name
		List : entities
		List : dependencies
		<init>
		toString
		hashCode
		equals
		name
		entities
		dependencies
	}

	class TypeInfo{
		Optional : outer
		String : name
		List : typeParameters
		<init>
		of
		withTypeParameter
		toString
		hashCode
		equals
		outer
		name
		typeParameters
		lambda$toString$0
	}

	class AssociationDependency_Side{
		Entity : entity
		Optional : label
		boolean : navigability
		AssociationDependency$Cardinality : cardinality
		<init>
		toString
		hashCode
		equals
		entity
		label
		navigability
		cardinality
	}

	class ModuleScrapper{
		<init>
		packageName
		parseModule
		scrapModule
		lambda$scrapModule$1
		lambda$scrapModule$0
	}

	class ClassFileParser{
		<init>
		parseClass
	}

	class ClassFileParser_1{
		visit
		visitRecordComponent
		visitField
		visitMethod
	}

	class ClassFileParser_EntityBuilder{
		String : name
		<init>
		name
		build
	}

	class Main{
		<init>
		exitWithError
		writeToPath
		convertFile
		main
		mermaid
		plantUml
	}

	class JarReader{
		Path : DEFAULT_SEARCH_DIRECTORY
		<init>
		isPublic
		isPrivate
		isProtected
		isFinal
		isStatic
		getVisibility
		modifiers
		isRecord
		isInterface
		isEnum
		isAnnotation
		isAbstract
		isModule
		stereotype
		entityName
		getEntities
		getEntities
	}

	class Parser{
		parse
	}

	class Main_OutputFormat{
		Main$OutputFormat : MERMAID
		Main$OutputFormat : PLANT_UML
		Supplier : parserSupplier
		Main$OutputFormat[] : $VALUES
		values
		valueOf
		<init>
		getParser
		$values
	}

	class JarReader_1{
		visit
		visitRecordComponent
		visitField
		visitMethod
	}

	class MermaidParser{
		<init>
		parseField
		parseEntity
		parse
	}

	class PlantumlParser{
		<init>
		parseField
		parseEntity
		parse
	}

	class module-info{
	}

@enduml
```

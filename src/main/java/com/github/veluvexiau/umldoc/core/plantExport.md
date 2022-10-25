```plantuml
@startuml
' umldoc
	class module-info{
	}

	class PlantUmlExtract{
		<init>
		generate
		init
		end
		displayEntity
	}

	class MermaidExtract{
		<init>
		generate
		init
		end
		displayEntity
		methodsAndParameters
	}

	class ExtractMethod{
		<init>
		getNameFromPath
		getStereotype
		typeOfList
		parameterWithType
		lambda$typeOfList$0
	}

	class Main{
		<init>
		main
	}

	class ClassFileParser_1{
		modifier
		visit
		visitRecordComponent
		visitField
		visitMethod
	}

	class ClassFileParser{
		<init>
		readJarFile
	}

	class Generator{
		fieldAccessor
		fieldToString
		generate
	}

	class MermaidGenerator{
		<init>
		escapeField
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

	class Helper{
		<init>
		parseCardinalities
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

	class Generator{
		generate
	}

	class MermaidGenerator{
		<init>
		generate
	}

	class PlantUmlGenerator{
		<init>
		generate
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

	class ModuleScrapper{
		<init>
		packageName
		parseModule
		scrapModule
		lambda$scrapModule$1
		lambda$scrapModule$0
	}

	class ClassFileParser_1{
		visit
		visitRecordComponent
		visitField
		visitMethod
	}

	class ClassFileParser{
		<init>
		parseClass
	}

	class ClassFileParser_EntityBuilder{
		String : name
		<init>
		name
		build
	}

	class EntityBuilder{
		ArrayList : methods
		ArrayList : fields
		TypeInfo : type
		Set : modifiers
		Entity$Stereotype : stereotype
		<init>
		addMethod
		addField
		setModifiers
		setTypeInfo
		setStereotype
		build
	}

	class JarReader{
		<init>
		entityName
		getEntities
	}

	class AccessReader_StereotypeAssociation{
		int : access
		Entity$Stereotype : stereotype
		toString
		hashCode
		equals
		access
		stereotype
	}

	class AccessReader_ModifierAssociation{
		int : access
		Modifier : modifier
		toString
		hashCode
		equals
		access
		modifier
	}

	class AccessReader{
		List : modifiers
		List : stereotypes
		<init>
		isModule
		isSynthetic
		stereotype
		modifiers
		lambda$modifiers$1
		lambda$stereotype$0
	}

	class JarReader_EntityListerClassVisitor{
		ArrayList : entities
		EntityBuilder : entityBuilder
		boolean : isEntitySkipped
		<init>
		entities
		visit
		visitRecordComponent
		visitField
		visitMethod
		visitEnd
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

	class Parser{
		parse
	}

	class MermaidParser{
		<init>
		parseField
		parseEntity
		parse
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

	class PlantumlParser{
		<init>
		parseField
		parseEntity
		parse
	}

	class MermaidSchemaGenerator_1{
	}

	class Generator{
		generate
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

	class PlantUmlSchemaGenerator{
		<init>
		generate
		generateEntity
		modifierToString
	}

	class Main{
		<init>
		main
		asm
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

@enduml
```

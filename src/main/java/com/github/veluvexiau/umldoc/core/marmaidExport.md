```mermaid
%% umldoc
classDiagram
	direction TB
	class JarParser_1{
		visit([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String[]], type=String[]]]) : null
	}

	class JarParser{
		<init>([]) : null
		populateEntities([Parameter[name=ClassDesc[ClassReader], type=ClassReader], Parameter[name=ClassDesc[HashSet], type=HashSet]]) : (Lorg/objectweb/asm/ClassReader;Ljava/util/HashSet<Lcom/github/forax/umldoc/core/Entity;>;)V
		recoverEntitiesFromJar([Parameter[name=ClassDesc[HashSet], type=HashSet]]) : (Ljava/util/HashSet<Lcom/github/forax/umldoc/core/Entity;>;)V
		getEntities([]) : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
	}

	class Main{
		<init>([]) : null
		main([Parameter[name=ClassDesc[String[]], type=String[]]]) : null
	}

	class ClassFileParser_1{
		visit([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String[]], type=String[]]]) : null
		visitRecordComponent([Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String]]) : null
		visitField([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[Object], type=Object]]) : null
		visitMethod([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String[]], type=String[]]]) : null
	}

	class ClassFileParser_EntityBuilder{
		String : name
		<init>([]) : null
		name([Parameter[name=ClassDesc[String], type=String]]) : null
		build([]) : null
	}

	class ClassFileParser{
		<init>([]) : null
		parseClass([Parameter[name=ClassDesc[InputStream], type=InputStream]]) : null
	}

	class ModuleScrapper{
		<init>([]) : null
		packageName([Parameter[name=ClassDesc[String], type=String]]) : null
		parseModule([Parameter[name=ClassDesc[ModuleReference], type=ModuleReference], Parameter[name=ClassDesc[Consumer], type=Consumer]]) : (Ljava/lang/module/ModuleReference;Ljava/util/function/Consumer<Lcom/github/forax/umldoc/core/Entity;>;)V
		scrapModule([Parameter[name=ClassDesc[ModuleReference], type=ModuleReference]]) : (Ljava/lang/module/ModuleReference;)Ljava/util/List<Lcom/github/forax/umldoc/core/Package;>;
		lambda$scrapModule$1([Parameter[name=ClassDesc[Map$Entry], type=Map$Entry]]) : null
		lambda$scrapModule$0([Parameter[name=ClassDesc[Entity], type=Entity]]) : null
	}

	class AssociationDependency_Cardinality{
		<<ENUM>>
		AssociationDependency$Cardinality : ONLY_ONE
		AssociationDependency$Cardinality : ZERO_OR_ONE
		AssociationDependency$Cardinality : MANY
		AssociationDependency$Cardinality[] : $VALUES
		values([]) : null
		valueOf([Parameter[name=ClassDesc[String], type=String]]) : null
		<init>([Parameter[name=ClassDesc[String], type=String], Parameter[name=PrimitiveClassDesc[int], type=int]]) : ()V
		$values([]) : null
	}

	class AssociationDependency_Side{
		<<RECORD>>
		Entity : entity
		Optional : label
		boolean : navigability
		AssociationDependency$Cardinality : cardinality
		<init>([Parameter[name=ClassDesc[Entity], type=Entity], Parameter[name=ClassDesc[Optional], type=Optional], Parameter[name=PrimitiveClassDesc[boolean], type=boolean], Parameter[name=ClassDesc[AssociationDependency$Cardinality], type=AssociationDependency$Cardinality]]) : (Lcom/github/forax/umldoc/core/Entity;Ljava/util/Optional<Ljava/lang/String;>;ZLcom/github/forax/umldoc/core/AssociationDependency$Cardinality;)V
		toString([]) : null
		hashCode([]) : null
		equals([Parameter[name=ClassDesc[Object], type=Object]]) : null
		entity([]) : null
		label([]) : ()Ljava/util/Optional<Ljava/lang/String;>;
		navigability([]) : null
		cardinality([]) : null
	}

	class AssociationDependency{
		<<RECORD>>
		AssociationDependency$Side : left
		AssociationDependency$Side : right
		<init>([Parameter[name=ClassDesc[AssociationDependency$Side], type=AssociationDependency$Side], Parameter[name=ClassDesc[AssociationDependency$Side], type=AssociationDependency$Side]]) : null
		toString([]) : null
		hashCode([]) : null
		equals([Parameter[name=ClassDesc[Object], type=Object]]) : null
		left([]) : null
		right([]) : null
	}

	class Dependency{
		<<INTERFACE>>
	}

	class Entity_Stereotype{
		<<ENUM>>
		Entity$Stereotype : CLASS
		Entity$Stereotype : INTERFACE
		Entity$Stereotype : ANNOTATION
		Entity$Stereotype : ENUM
		Entity$Stereotype : RECORD
		Entity$Stereotype : ABSTRACT
		Entity$Stereotype[] : $VALUES
		values([]) : null
		valueOf([Parameter[name=ClassDesc[String], type=String]]) : null
		<init>([Parameter[name=ClassDesc[String], type=String], Parameter[name=PrimitiveClassDesc[int], type=int]]) : ()V
		$values([]) : null
	}

	class Entity{
		<<RECORD>>
		Set : modifiers
		String : name
		Entity$Stereotype : stereotype
		List : fields
		List : methods
		<init>([Parameter[name=ClassDesc[Set], type=Set], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[Entity$Stereotype], type=Entity$Stereotype], Parameter[name=ClassDesc[List], type=List], Parameter[name=ClassDesc[List], type=List]]) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Ljava/lang/String;Lcom/github/forax/umldoc/core/Entity$Stereotype;Ljava/util/List<Lcom/github/forax/umldoc/core/Field;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Method;>;)V
		toString([]) : null
		hashCode([]) : null
		equals([Parameter[name=ClassDesc[Object], type=Object]]) : null
		modifiers([]) : ()Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		name([]) : null
		stereotype([]) : null
		fields([]) : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Field;>;
		methods([]) : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Method;>;
	}

	class Field{
		<<RECORD>>
		Set : modifiers
		String : name
		String : type
		<init>([Parameter[name=ClassDesc[Set], type=Set], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String]]) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Ljava/lang/String;Ljava/lang/String;)V
		toString([]) : null
		hashCode([]) : null
		equals([Parameter[name=ClassDesc[Object], type=Object]]) : null
		modifiers([]) : ()Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		name([]) : null
		type([]) : null
	}

	class Method_Parameter{
		<<RECORD>>
		String : name
		String : type
		<init>([Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String]]) : null
		toString([]) : null
		hashCode([]) : null
		equals([Parameter[name=ClassDesc[Object], type=Object]]) : null
		name([]) : null
		type([]) : null
	}

	class Method{
		<<RECORD>>
		Set : modifiers
		String : name
		String : returnType
		List : parameters
		<init>([Parameter[name=ClassDesc[Set], type=Set], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[List], type=List]]) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Ljava/lang/String;Ljava/lang/String;Ljava/util/List<Lcom/github/forax/umldoc/core/Method$Parameter;>;)V
		toString([]) : null
		hashCode([]) : null
		equals([Parameter[name=ClassDesc[Object], type=Object]]) : null
		modifiers([]) : ()Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		name([]) : null
		returnType([]) : null
		parameters([]) : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Method$Parameter;>;
	}

	class Modifier{
		<<ENUM>>
		Modifier : PRIVATE
		Modifier : PACKAGE
		Modifier : PROTECTED
		Modifier : PUBLIC
		Modifier : STATIC
		Modifier : FINAL
		Modifier[] : $VALUES
		values([]) : null
		valueOf([Parameter[name=ClassDesc[String], type=String]]) : null
		<init>([Parameter[name=ClassDesc[String], type=String], Parameter[name=PrimitiveClassDesc[int], type=int]]) : ()V
		$values([]) : null
	}

	class Package{
		<<RECORD>>
		String : name
		List : entities
		List : dependencies
		<init>([Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[List], type=List], Parameter[name=ClassDesc[List], type=List]]) : (Ljava/lang/String;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;)V
		toString([]) : null
		hashCode([]) : null
		equals([Parameter[name=ClassDesc[Object], type=Object]]) : null
		name([]) : null
		entities([]) : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
		dependencies([]) : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;
	}

	class SubtypeDependency{
		<<RECORD>>
		Entity : supertype
		Entity : subtype
		<init>([Parameter[name=ClassDesc[Entity], type=Entity], Parameter[name=ClassDesc[Entity], type=Entity]]) : null
		toString([]) : null
		hashCode([]) : null
		equals([Parameter[name=ClassDesc[Object], type=Object]]) : null
		supertype([]) : null
		subtype([]) : null
	}

	class Generator{
		<<INTERFACE>>
		generate([Parameter[name=PrimitiveClassDesc[boolean], type=boolean], Parameter[name=ClassDesc[List], type=List], Parameter[name=ClassDesc[List], type=List], Parameter[name=ClassDesc[Writer], type=Writer]]) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V
	}

	class MermaidGenerator{
		<init>([]) : null
		generate([Parameter[name=PrimitiveClassDesc[boolean], type=boolean], Parameter[name=ClassDesc[List], type=List], Parameter[name=ClassDesc[List], type=List], Parameter[name=ClassDesc[Writer], type=Writer]]) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V
	}

	class PlantUmlGenerator{
		<init>([]) : null
		generate([Parameter[name=PrimitiveClassDesc[boolean], type=boolean], Parameter[name=ClassDesc[List], type=List], Parameter[name=ClassDesc[List], type=List], Parameter[name=ClassDesc[Writer], type=Writer]]) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V
	}

	class Hello{
		<init>([]) : null
		main([Parameter[name=ClassDesc[String[]], type=String[]]]) : null
		lambda$main$0([Parameter[name=ClassDesc[Entity], type=Entity]]) : null
	}

	class JarReader_MyVisitor{
		ArrayList : entities
		getEntities([]) : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
		<init>([Parameter[name=PrimitiveClassDesc[int], type=int]]) : null
		visit([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String[]], type=String[]]]) : null
		visitField([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[Object], type=Object]]) : null
		getModifiers([Parameter[name=PrimitiveClassDesc[int], type=int]]) : (I)Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		getStereotype([Parameter[name=PrimitiveClassDesc[int], type=int]]) : null
	}

	class JarReader{
		String : DEFAULT_DIRECTORY
		<init>([]) : null
		isPublic([Parameter[name=PrimitiveClassDesc[int], type=int]]) : null
		isPrivate([Parameter[name=PrimitiveClassDesc[int], type=int]]) : null
		isProtected([Parameter[name=PrimitiveClassDesc[int], type=int]]) : null
		isRecord([Parameter[name=PrimitiveClassDesc[int], type=int]]) : null
		isInterface([Parameter[name=PrimitiveClassDesc[int], type=int]]) : null
		isEnum([Parameter[name=PrimitiveClassDesc[int], type=int]]) : null
		getEntities([]) : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
		getEntities([Parameter[name=ClassDesc[String], type=String]]) : (Ljava/lang/String;)Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
	}

	class Generator{
		<<INTERFACE>>
		generate([Parameter[name=ClassDesc[Writer], type=Writer], Parameter[name=ClassDesc[List], type=List]]) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
	}

	class MermaidSchemaGenerator_1{
	}

	class MermaidSchemaGenerator{
		<init>([]) : null
		generate([Parameter[name=ClassDesc[Writer], type=Writer], Parameter[name=ClassDesc[List], type=List]]) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		generateHeader([Parameter[name=ClassDesc[Writer], type=Writer]]) : null
		generateEntity([Parameter[name=ClassDesc[Writer], type=Writer], Parameter[name=ClassDesc[Entity], type=Entity]]) : null
		modifierToString([Parameter[name=ClassDesc[Modifier], type=Modifier]]) : null
	}

	class PlantUmlSchemaGenerator_1{
	}

	class PlantUmlSchemaGenerator{
		<init>([]) : null
		generate([Parameter[name=ClassDesc[Writer], type=Writer], Parameter[name=ClassDesc[List], type=List]]) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		generateEntity([Parameter[name=ClassDesc[Writer], type=Writer], Parameter[name=ClassDesc[Entity], type=Entity]]) : null
		modifierToString([Parameter[name=ClassDesc[Modifier], type=Modifier]]) : null
	}

	class Main{
		<init>([]) : null
		main([Parameter[name=ClassDesc[String[]], type=String[]]]) : null
		asm([]) : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
	}

	class Visitor{
		Entity : entity
		<init>([Parameter[name=PrimitiveClassDesc[int], type=int]]) : null
		getEntity([]) : null
		visit([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String[]], type=String[]]]) : null
		modifier([Parameter[name=PrimitiveClassDesc[int], type=int]]) : null
		visitRecordComponent([Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String]]) : null
		visitField([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[Object], type=Object]]) : null
		visitMethod([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String[]], type=String[]]]) : null
	}

	class ExtractMethods{
		<init>([]) : null
		getNameFromPath([Parameter[name=ClassDesc[String], type=String]]) : null
		getStereotype([Parameter[name=ClassDesc[String], type=String]]) : null
	}

	class Main_1{
		modifier([Parameter[name=PrimitiveClassDesc[int], type=int]]) : null
		visit([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String[]], type=String[]]]) : null
		visitRecordComponent([Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String]]) : null
		visitField([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[Object], type=Object]]) : null
		visitMethod([Parameter[name=PrimitiveClassDesc[int], type=int], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String], type=String], Parameter[name=ClassDesc[String[]], type=String[]]]) : null
	}

	class Main{
		<init>([]) : null
		main([Parameter[name=ClassDesc[String[]], type=String[]]]) : null
		readJarFile([Parameter[name=ClassDesc[Path], type=Path]]) : (Ljava/nio/file/Path;)Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
	}

	class MermaidExtract{
		<init>([]) : null
		generate([Parameter[name=ClassDesc[List], type=List]]) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		init([Parameter[name=ClassDesc[PrintWriter], type=PrintWriter]]) : null
		end([Parameter[name=ClassDesc[PrintWriter], type=PrintWriter]]) : null
		displayEntity([Parameter[name=ClassDesc[PrintWriter], type=PrintWriter], Parameter[name=ClassDesc[Entity], type=Entity]]) : null
	}

	class PlantUmlExtract{
		<init>([]) : null
		generate([Parameter[name=ClassDesc[List], type=List]]) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		init([Parameter[name=ClassDesc[PrintWriter], type=PrintWriter]]) : null
		end([Parameter[name=ClassDesc[PrintWriter], type=PrintWriter]]) : null
		displayEntity([Parameter[name=ClassDesc[PrintWriter], type=PrintWriter], Parameter[name=ClassDesc[Entity], type=Entity]]) : null
	}

	class module-info{
	}

```

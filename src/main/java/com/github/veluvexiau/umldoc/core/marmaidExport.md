```mermaid
%% umldoc
classDiagram
	direction TB
	class JarParser_1{
		visit(int, int, String, String, String, String[]) : null
	}

	class JarParser{
		<init>() : null
		populateEntities(ClassReader, HashSet) : (Lorg/objectweb/asm/ClassReader;Ljava/util/HashSet<Lcom/github/forax/umldoc/core/Entity;>;)V
		recoverEntitiesFromJar(HashSet) : (Ljava/util/HashSet<Lcom/github/forax/umldoc/core/Entity;>;)V
		getEntities() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
	}

	class Main{
		<init>() : null
		main(String[]) : null
	}

	class ClassFileParser_1{
		visit(int, int, String, String, String, String[]) : null
		visitRecordComponent(String, String, String) : null
		visitField(int, String, String, String, Object) : null
		visitMethod(int, String, String, String, String[]) : null
	}

	class ClassFileParser_EntityBuilder{
		String : name
		<init>() : null
		name(String) : null
		build() : null
	}

	class ClassFileParser{
		<init>() : null
		parseClass(InputStream) : null
	}

	class ModuleScrapper{
		<init>() : null
		packageName(String) : null
		parseModule(ModuleReference, Consumer) : (Ljava/lang/module/ModuleReference;Ljava/util/function/Consumer<Lcom/github/forax/umldoc/core/Entity;>;)V
		scrapModule(ModuleReference) : (Ljava/lang/module/ModuleReference;)Ljava/util/List<Lcom/github/forax/umldoc/core/Package;>;
		lambda$scrapModule$1(Map$Entry) : null
		lambda$scrapModule$0(Entity) : null
	}

	class AssociationDependency_Cardinality{
		<<ENUM>>
		AssociationDependency$Cardinality : ONLY_ONE
		AssociationDependency$Cardinality : ZERO_OR_ONE
		AssociationDependency$Cardinality : MANY
		AssociationDependency$Cardinality[] : $VALUES
		values() : null
		valueOf(String) : null
		<init>(String, int) : ()V
		$values() : null
	}

	class AssociationDependency_Side{
		<<RECORD>>
		Entity : entity
		Optional : label
		boolean : navigability
		AssociationDependency$Cardinality : cardinality
		<init>(Entity, Optional, boolean, AssociationDependency$Cardinality) : (Lcom/github/forax/umldoc/core/Entity;Ljava/util/Optional<Ljava/lang/String;>;ZLcom/github/forax/umldoc/core/AssociationDependency$Cardinality;)V
		toString() : null
		hashCode() : null
		equals(Object) : null
		entity() : null
		label() : ()Ljava/util/Optional<Ljava/lang/String;>;
		navigability() : null
		cardinality() : null
	}

	class AssociationDependency{
		<<RECORD>>
		AssociationDependency$Side : left
		AssociationDependency$Side : right
		<init>(AssociationDependency$Side, AssociationDependency$Side) : null
		toString() : null
		hashCode() : null
		equals(Object) : null
		left() : null
		right() : null
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
		values() : null
		valueOf(String) : null
		<init>(String, int) : ()V
		$values() : null
	}

	class Entity{
		<<RECORD>>
		Set : modifiers
		String : name
		Entity$Stereotype : stereotype
		List : fields
		List : methods
		<init>(Set, String, Entity$Stereotype, List, List) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Ljava/lang/String;Lcom/github/forax/umldoc/core/Entity$Stereotype;Ljava/util/List<Lcom/github/forax/umldoc/core/Field;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Method;>;)V
		toString() : null
		hashCode() : null
		equals(Object) : null
		modifiers() : ()Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		name() : null
		stereotype() : null
		fields() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Field;>;
		methods() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Method;>;
	}

	class Field{
		<<RECORD>>
		Set : modifiers
		String : name
		String : type
		<init>(Set, String, String) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Ljava/lang/String;Ljava/lang/String;)V
		toString() : null
		hashCode() : null
		equals(Object) : null
		modifiers() : ()Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		name() : null
		type() : null
	}

	class Method_Parameter{
		<<RECORD>>
		String : name
		String : type
		<init>(String, String) : null
		toString() : null
		hashCode() : null
		equals(Object) : null
		name() : null
		type() : null
	}

	class Method{
		<<RECORD>>
		Set : modifiers
		String : name
		String : returnType
		List : parameters
		<init>(Set, String, String, List) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Ljava/lang/String;Ljava/lang/String;Ljava/util/List<Lcom/github/forax/umldoc/core/Method$Parameter;>;)V
		toString() : null
		hashCode() : null
		equals(Object) : null
		modifiers() : ()Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		name() : null
		returnType() : null
		parameters() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Method$Parameter;>;
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
		values() : null
		valueOf(String) : null
		<init>(String, int) : ()V
		$values() : null
	}

	class Package{
		<<RECORD>>
		String : name
		List : entities
		List : dependencies
		<init>(String, List, List) : (Ljava/lang/String;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;)V
		toString() : null
		hashCode() : null
		equals(Object) : null
		name() : null
		entities() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
		dependencies() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;
	}

	class SubtypeDependency{
		<<RECORD>>
		Entity : supertype
		Entity : subtype
		<init>(Entity, Entity) : null
		toString() : null
		hashCode() : null
		equals(Object) : null
		supertype() : null
		subtype() : null
	}

	class Generator{
		<<INTERFACE>>
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V
	}

	class MermaidGenerator{
		<init>() : null
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V
	}

	class PlantUmlGenerator{
		<init>() : null
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V
	}

	class Hello{
		<init>() : null
		main(String[]) : null
		lambda$main$0(Entity) : null
	}

	class JarReader_MyVisitor{
		ArrayList : entities
		getEntities() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
		<init>(int) : null
		visit(int, int, String, String, String, String[]) : null
		visitField(int, String, String, String, Object) : null
		getModifiers(int) : (I)Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		getStereotype(int) : null
	}

	class JarReader{
		String : DEFAULT_DIRECTORY
		<init>() : null
		isPublic(int) : null
		isPrivate(int) : null
		isProtected(int) : null
		isRecord(int) : null
		isInterface(int) : null
		isEnum(int) : null
		getEntities() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
		getEntities(String) : (Ljava/lang/String;)Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
	}

	class Generator{
		<<INTERFACE>>
		generate(Writer, List) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
	}

	class MermaidSchemaGenerator_1{
	}

	class MermaidSchemaGenerator{
		<init>() : null
		generate(Writer, List) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		generateHeader(Writer) : null
		generateEntity(Writer, Entity) : null
		modifierToString(Modifier) : null
	}

	class PlantUmlSchemaGenerator_1{
	}

	class PlantUmlSchemaGenerator{
		<init>() : null
		generate(Writer, List) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		generateEntity(Writer, Entity) : null
		modifierToString(Modifier) : null
	}

	class Main{
		<init>() : null
		main(String[]) : null
		asm() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
	}

	class Visitor{
		Entity : entity
		<init>(int) : null
		getEntity() : null
		visit(int, int, String, String, String, String[]) : null
		modifier(int) : null
		visitRecordComponent(String, String, String) : null
		visitField(int, String, String, String, Object) : null
		visitMethod(int, String, String, String, String[]) : null
	}

	class ExtractMethods{
		<init>() : null
		getNameFromPath(String) : null
		getStereotype(String) : null
	}

	class Main_1{
		modifier(int) : null
		visit(int, int, String, String, String, String[]) : null
		visitRecordComponent(String, String, String) : null
		visitField(int, String, String, String, Object) : null
		visitMethod(int, String, String, String, String[]) : null
	}

	class Main{
		<init>() : null
		main(String[]) : null
		readJarFile(Path) : (Ljava/nio/file/Path;)Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
	}

	class MermaidExtract{
		<init>() : null
		generate(List) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		init(PrintWriter) : null
		end(PrintWriter) : null
		displayEntity(PrintWriter, Entity) : null
	}

	class PlantUmlExtract{
		<init>() : null
		generate(List) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		init(PrintWriter) : null
		end(PrintWriter) : null
		displayEntity(PrintWriter, Entity) : null
	}

	class module-info{
	}

```

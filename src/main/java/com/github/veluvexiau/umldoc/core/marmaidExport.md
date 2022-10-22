```mermaid
%% umldoc
classDiagram
	direction TB
	class module-info{

	}
	class PlantUmlExtract{
		<init>() : 
		generate(List) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		init(PrintWriter) : 
		end(PrintWriter) : 
		displayEntity(PrintWriter, Entity) : 

	}
	class MermaidExtract{
		<init>() : 
		generate(List) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		init(PrintWriter) : 
		end(PrintWriter) : 
		displayEntity(PrintWriter, Entity) : 
		methodsAndParameters(Entity) : 

	}
	class ExtractMethod{
		<init>() : 
		getNameFromPath(String) : 
		getStereotype(String) : 
		typeOfList(Method) : 
		parameterWithType(TypeInfo) : 
		lambda$typeOfList$0(Method$Parameter) : 

	}
	class Main{
		<init>() : 
		main(String[]) : 

	}
	class ClassFileParser_1{
		modifier(int) : 
		visit(int, int, String, String, String, String[]) : 
		visitRecordComponent(String, String, String) : 
		visitField(int, String, String, String, Object) : 
		visitMethod(int, String, String, String, String[]) : 

	}
	class ClassFileParser{
		<init>() : 
		readJarFile(Path) : (Ljava/nio/file/Path;)Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;

	}
	class Generator{
		<<INTERFACE>>
		fieldAccessor(Set) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;)C
		fieldToString(Field) : 
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/AssociationDependency;>;Ljava/io/Writer;)V

	}
	class MermaidGenerator{
		<init>() : 
		escapeField(Field) : 
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/AssociationDependency;>;Ljava/io/Writer;)V

	}
	class PlantUmlGenerator{
		<init>() : 
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/AssociationDependency;>;Ljava/io/Writer;)V

	}
	class Main{
		<init>() : 
		main(String[]) : 
		lambda$main$0(Entity) : 

	}
	class Helper{
		<init>() : 
		parseCardinalities(AssociationDependency$Cardinality) : 

	}
	class Helper_1{

	}
	class JarParser_1{
		visit(int, int, String, String, String, String[]) : 
		visitField(int, String, String, String, Object) : 
		lambda$visitField$0(String) : 

	}
	class JarParser{
		HashSet : entities
		Entity : currentEntity
		ArrayList : associations
		<init>() : 
		getAssociationDependencies() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/AssociationDependency;>;
		entities() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
		recoverEntitiesFromJar() : 
		resolveStereotype(String) : 
		modifiers(int) : (I)Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		addFieldOrAssociation(int, String, String, HashSet) : (ILjava/lang/String;Ljava/lang/String;Ljava/util/HashSet<Lcom/github/forax/umldoc/core/Field;>;)V
		getAsmData(ClassReader) : 
		lambda$addFieldOrAssociation$1(Entity) : 
		lambda$addFieldOrAssociation$0(String, Entity) : 

	}
	class Generator{
		<<INTERFACE>>
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V

	}
	class MermaidGenerator{
		<init>() : 
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V

	}
	class PlantUmlGenerator{
		<init>() : 
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V

	}
	class AssociationDependency_Side{
		<<RECORD>>
		Entity : entity
		Optional : label
		boolean : navigability
		AssociationDependency$Cardinality : cardinality
		<init>(Entity, Optional, boolean, AssociationDependency$Cardinality) : (Lcom/github/forax/umldoc/core/Entity;Ljava/util/Optional<Ljava/lang/String;>;ZLcom/github/forax/umldoc/core/AssociationDependency$Cardinality;)V
		toString() : 
		hashCode() : 
		equals(Object) : 
		entity() : 
		label() : ()Ljava/util/Optional<Ljava/lang/String;>;
		navigability() : 
		cardinality() : 

	}
	class Package{
		<<RECORD>>
		String : name
		List : entities
		List : dependencies
		<init>(String, List, List) : (Ljava/lang/String;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;)V
		toString() : 
		hashCode() : 
		equals(Object) : 
		name() : 
		entities() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
		dependencies() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;

	}
	class Method_Parameter{
		<<RECORD>>
		String : name
		TypeInfo : typeInfo
		<init>(String, TypeInfo) : 
		<init>(String, String) : 
		type() : 
		toString() : 
		hashCode() : 
		equals(Object) : 
		name() : 
		typeInfo() : 

	}
	class SubtypeDependency{
		<<RECORD>>
		Entity : supertype
		Entity : subtype
		<init>(Entity, Entity) : 
		toString() : 
		hashCode() : 
		equals(Object) : 
		supertype() : 
		subtype() : 

	}
	class TypeInfo{
		<<RECORD>>
		Optional : outer
		String : name
		List : typeParameters
		<init>(Optional, String, List) : (Ljava/util/Optional<Lcom/github/forax/umldoc/core/TypeInfo;>;Ljava/lang/String;Ljava/util/List<Lcom/github/forax/umldoc/core/TypeInfo;>;)V
		of(String) : 
		withTypeParameter(TypeInfo) : 
		toString() : 
		hashCode() : 
		equals(Object) : 
		outer() : ()Ljava/util/Optional<Lcom/github/forax/umldoc/core/TypeInfo;>;
		name() : 
		typeParameters() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/TypeInfo;>;
		lambda$toString$0(TypeInfo) : 

	}
	class AssociationDependency{
		<<RECORD>>
		AssociationDependency$Side : left
		AssociationDependency$Side : right
		<init>(AssociationDependency$Side, AssociationDependency$Side) : 
		toString() : 
		hashCode() : 
		equals(Object) : 
		left() : 
		right() : 

	}
	class Field{
		<<RECORD>>
		Set : modifiers
		String : name
		TypeInfo : typeInfo
		<init>(Set, String, TypeInfo) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Ljava/lang/String;Lcom/github/forax/umldoc/core/TypeInfo;)V
		<init>(Set, String, String) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Ljava/lang/String;Ljava/lang/String;)V
		type() : 
		toString() : 
		hashCode() : 
		equals(Object) : 
		modifiers() : ()Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		name() : 
		typeInfo() : 

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
		values() : 
		valueOf(String) : 
		<init>(String, int) : ()V
		$values() : 

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
		values() : 
		valueOf(String) : 
		<init>(String, int) : ()V
		$values() : 

	}
	class Dependency{
		<<INTERFACE>>

	}
	class Method{
		<<RECORD>>
		Set : modifiers
		String : name
		TypeInfo : returnTypeInfo
		List : parameters
		<init>(Set, String, TypeInfo, List) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Ljava/lang/String;Lcom/github/forax/umldoc/core/TypeInfo;Ljava/util/List<Lcom/github/forax/umldoc/core/Method$Parameter;>;)V
		<init>(Set, String, String, List) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Ljava/lang/String;Ljava/lang/String;Ljava/util/List<Lcom/github/forax/umldoc/core/Method$Parameter;>;)V
		returnType() : 
		toString() : 
		hashCode() : 
		equals(Object) : 
		modifiers() : ()Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		name() : 
		returnTypeInfo() : 
		parameters() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Method$Parameter;>;

	}
	class AssociationDependency_Cardinality{
		<<ENUM>>
		AssociationDependency$Cardinality : ONLY_ONE
		AssociationDependency$Cardinality : ZERO_OR_ONE
		AssociationDependency$Cardinality : MANY
		AssociationDependency$Cardinality[] : $VALUES
		values() : 
		valueOf(String) : 
		<init>(String, int) : ()V
		$values() : 

	}
	class Entity{
		<<RECORD>>
		Set : modifiers
		TypeInfo : type
		Entity$Stereotype : stereotype
		List : fields
		List : methods
		<init>(Set, TypeInfo, Entity$Stereotype, List, List) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Lcom/github/forax/umldoc/core/TypeInfo;Lcom/github/forax/umldoc/core/Entity$Stereotype;Ljava/util/List<Lcom/github/forax/umldoc/core/Field;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Method;>;)V
		<init>(Set, String, Entity$Stereotype, List, List) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;Ljava/lang/String;Lcom/github/forax/umldoc/core/Entity$Stereotype;Ljava/util/List<Lcom/github/forax/umldoc/core/Field;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Method;>;)V
		name() : 
		toString() : 
		hashCode() : 
		equals(Object) : 
		modifiers() : ()Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		type() : 
		stereotype() : 
		fields() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Field;>;
		methods() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Method;>;

	}
	class ModuleScrapper{
		<init>() : 
		packageName(String) : 
		parseModule(ModuleReference, Consumer) : (Ljava/lang/module/ModuleReference;Ljava/util/function/Consumer<Lcom/github/forax/umldoc/core/Entity;>;)V
		scrapModule(ModuleReference) : (Ljava/lang/module/ModuleReference;)Ljava/util/List<Lcom/github/forax/umldoc/core/Package;>;
		lambda$scrapModule$1(Map$Entry) : 
		lambda$scrapModule$0(Entity) : 

	}
	class ClassFileParser_1{
		visit(int, int, String, String, String, String[]) : 
		visitRecordComponent(String, String, String) : 
		visitField(int, String, String, String, Object) : 
		visitMethod(int, String, String, String, String[]) : 

	}
	class ClassFileParser{
		<init>() : 
		parseClass(InputStream) : 

	}
	class ClassFileParser_EntityBuilder{
		String : name
		<init>() : 
		name(String) : 
		build() : 

	}
	class EntityBuilder{
		ArrayList : methods
		ArrayList : fields
		TypeInfo : type
		Set : modifiers
		Entity$Stereotype : stereotype
		<init>() : 
		addMethod(Method) : 
		addField(Field) : 
		setModifiers(Set) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;)Lcom/github/magickoders/jar/EntityBuilder;
		setTypeInfo(TypeInfo) : 
		setStereotype(Entity$Stereotype) : 
		build() : 

	}
	class JarReader{
		<init>() : 
		entityName(String) : 
		getEntities(Path) : (Ljava/nio/file/Path;)Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;

	}
	class AccessReader_StereotypeAssociation{
		<<RECORD>>
		int : access
		Entity$Stereotype : stereotype
		toString() : 
		hashCode() : 
		equals(Object) : 
		access() : 
		stereotype() : 

	}
	class AccessReader_ModifierAssociation{
		<<RECORD>>
		int : access
		Modifier : modifier
		toString() : 
		hashCode() : 
		equals(Object) : 
		access() : 
		modifier() : 

	}
	class AccessReader{
		List : modifiers
		List : stereotypes
		<init>() : 
		isModule(int) : 
		isSynthetic(int) : 
		stereotype(int) : 
		modifiers(int) : (I)Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		lambda$modifiers$1(int, AccessReader$ModifierAssociation) : 
		lambda$stereotype$0(int, AccessReader$StereotypeAssociation) : 

	}
	class JarReader_EntityListerClassVisitor{
		ArrayList : entities
		EntityBuilder : entityBuilder
		boolean : isEntitySkipped
		<init>() : 
		entities() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
		visit(int, int, String, String, String, String[]) : 
		visitRecordComponent(String, String, String) : 
		visitField(int, String, String, String, Object) : 
		visitMethod(int, String, String, String, String[]) : 
		visitEnd() : 

	}
	class Main_OutputFormat{
		<<ENUM>>
		Main$OutputFormat : MERMAID
		Main$OutputFormat : PLANT_UML
		Supplier : parserSupplier
		Main$OutputFormat[] : $VALUES
		values() : 
		valueOf(String) : 
		<init>(String, int, Supplier) : (Ljava/util/function/Supplier<Lcom/github/magickoders/Parser;>;)V
		getParser() : 
		$values() : 

	}
	class Parser{
		<<INTERFACE>>
		parse(Package) : 

	}
	class MermaidParser{
		<init>() : 
		parseField(Field) : 
		parseEntity(Entity) : 
		parse(Package) : 

	}
	class Main{
		<init>() : 
		exitWithError(String) : 
		writeToPath(Path, String) : 
		convertFile(Parser, Path, Path) : 
		main(String[]) : 
		mermaid(String[]) : 
		plantUml(String[]) : 

	}
	class PlantumlParser{
		<init>() : 
		parseField(Field) : 
		parseEntity(Entity) : 
		parse(Package) : 

	}
	class MermaidSchemaGenerator_1{

	}
	class Generator{
		<<INTERFACE>>
		generate(Writer, List) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V

	}
	class PlantUmlSchemaGenerator_1{

	}
	class MermaidSchemaGenerator{
		<init>() : 
		generate(Writer, List) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		generateHeader(Writer) : 
		generateEntity(Writer, Entity, Set) : (Ljava/io/Writer;Lcom/github/forax/umldoc/core/Entity;Ljava/util/Set<Ljava/lang/String;>;)V
		computeFieldsEnum(Entity) : 
		computeFieldsClass(Entity, ArrayList, Set) : (Lcom/github/forax/umldoc/core/Entity;Ljava/util/ArrayList<Ljava/lang/String;>;Ljava/util/Set<Ljava/lang/String;>;)Ljava/lang/String;
		generateFields(List) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Field;>;)Ljava/lang/String;
		generateRecordFields(List) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Field;>;)Ljava/lang/String;
		generateAssociations(Entity, List) : (Lcom/github/forax/umldoc/core/Entity;Ljava/util/List<Ljava/lang/String;>;)Ljava/lang/String;
		modifierToString(Modifier) : 
		lambda$generateAssociations$2(Entity, String) : 
		lambda$generateRecordFields$1(Field) : 
		lambda$generateFields$0(Field) : 

	}
	class PlantUmlSchemaGenerator{
		<init>() : 
		generate(Writer, List) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		generateEntity(Writer, Entity) : 
		modifierToString(Modifier) : 

	}
	class Main{
		<init>() : 
		main(String[]) : 
		asm() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;

	}
	class Visitor{
		Entity : entity
		<init>(int) : 
		removePath(String) : 
		getEntity() : 
		translateStereotype(String) : 
		visit(int, int, String, String, String, String[]) : 
		modifier(int) : 
		visitRecordComponent(String, String, String) : 
		visitField(int, String, String, String, Object) : 
		visitMethod(int, String, String, String, String[]) : 

	}
```

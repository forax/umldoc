```mermaid
%% umldoc
classDiagram
	direction TB
	class MermaidGenerator{
		<init>() : null
		escapeField(Field) : null
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/AssociationDependency;>;Ljava/io/Writer;)V

	class Generator{
		<<INTERFACE>>
		fieldAccessor(Set) : (Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;)C
		fieldToString(Field) : null
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/AssociationDependency;>;Ljava/io/Writer;)V

	class PlantUmlGenerator{
		<init>() : null
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/AssociationDependency;>;Ljava/io/Writer;)V

	class Main{
		<init>() : null
		main(String[]) : null
		lambda$main$0(Entity) : null

	class Helper_1{

	class JarParser_1{
		visit(int, int, String, String, String, String[]) : null
		visitField(int, String, String, String, Object) : null
		lambda$visitField$0(String) : null

	class JarParser{
		HashSet : entities
		Entity : currentEntity
		ArrayList : associations
		<init>() : null
		getAssociationDependencies() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/AssociationDependency;>;
		entities() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
		recoverEntitiesFromJar() : null
		resolveStereotype(String) : null
		modifiers(int) : (I)Ljava/util/Set<Lcom/github/forax/umldoc/core/Modifier;>;
		addFieldOrAssociation(int, String, String, HashSet) : (ILjava/lang/String;Ljava/lang/String;Ljava/util/HashSet<Lcom/github/forax/umldoc/core/Field;>;)V
		getAsmData(ClassReader) : null
		lambda$addFieldOrAssociation$1(Entity) : null
		lambda$addFieldOrAssociation$0(String, Entity) : null

	class Helper{
		<init>() : null
		parseCardinalities(AssociationDependency$Cardinality) : null

	class Main{
		<init>() : null
		main(String[]) : null
		asm() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;

	class MermaidSchemaGenerator_1{

	class Generator{
		<<INTERFACE>>
		generate(Writer, List) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V

	class PlantUmlSchemaGenerator{
		<init>() : null
		generate(Writer, List) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		generateEntity(Writer, Entity) : null
		modifierToString(Modifier) : null

	class PlantUmlSchemaGenerator_1{

	class MermaidSchemaGenerator{
		<init>() : null
		generate(Writer, List) : (Ljava/io/Writer;Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		generateHeader(Writer) : null
		generateEntity(Writer, Entity, Set) : (Ljava/io/Writer;Lcom/github/forax/umldoc/core/Entity;Ljava/util/Set<Ljava/lang/String;>;)V
		computeFieldsEnum(Entity) : null
		computeFieldsClass(Entity, ArrayList, Set) : (Lcom/github/forax/umldoc/core/Entity;Ljava/util/ArrayList<Ljava/lang/String;>;Ljava/util/Set<Ljava/lang/String;>;)Ljava/lang/String;
		generateFields(List) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Field;>;)Ljava/lang/String;
		generateRecordFields(List) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Field;>;)Ljava/lang/String;
		generateAssociations(Entity, List) : (Lcom/github/forax/umldoc/core/Entity;Ljava/util/List<Ljava/lang/String;>;)Ljava/lang/String;
		modifierToString(Modifier) : null
		lambda$generateAssociations$2(Entity, String) : null
		lambda$generateRecordFields$1(Field) : null
		lambda$generateFields$0(Field) : null

	class Visitor{
		Entity : entity
		<init>(int) : null
		removePath(String) : null
		getEntity() : null
		translateStereotype(String) : null
		visit(int, int, String, String, String, String[]) : null
		modifier(int) : null
		visitRecordComponent(String, String, String) : null
		visitField(int, String, String, String, Object) : null
		visitMethod(int, String, String, String, String[]) : null

	class Main{
		<init>() : null
		main(String[]) : null

	class ExtractMethods{
		<init>() : null
		getNameFromPath(String) : null
		getStereotype(String) : null

	class MermaidExtract{
		<init>() : null
		generate(List) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		init(PrintWriter) : null
		end(PrintWriter) : null
		displayEntity(PrintWriter, Entity) : null
		methodsAndParameters(Entity) : null
		formatReturnType(String) : null
		lambda$methodsAndParameters$0(Method$Parameter) : null

	class PlantUmlExtract{
		<init>() : null
		generate(List) : (Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;)V
		init(PrintWriter) : null
		end(PrintWriter) : null
		displayEntity(PrintWriter, Entity) : null

	class ClassFileParser{
		<init>() : null
		readJarFile(Path) : (Ljava/nio/file/Path;)Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;

	class ClassFileParser_1{
		modifier(int) : null
		visit(int, int, String, String, String, String[]) : null
		visitRecordComponent(String, String, String) : null
		visitField(int, String, String, String, Object) : null
		visitMethod(int, String, String, String, String[]) : null

	class MermaidGenerator{
		<init>() : null
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V

	class Generator{
		<<INTERFACE>>
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V

	class PlantUmlGenerator{
		<init>() : null
		generate(boolean, List, List, Writer) : (ZLjava/util/List<Lcom/github/forax/umldoc/core/Entity;>;Ljava/util/List<Lcom/github/forax/umldoc/core/Dependency;>;Ljava/io/Writer;)V

	class Dependency{
		<<INTERFACE>>

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

	class ModuleScrapper{
		<init>() : null
		packageName(String) : null
		parseModule(ModuleReference, Consumer) : (Ljava/lang/module/ModuleReference;Ljava/util/function/Consumer<Lcom/github/forax/umldoc/core/Entity;>;)V
		scrapModule(ModuleReference) : (Ljava/lang/module/ModuleReference;)Ljava/util/List<Lcom/github/forax/umldoc/core/Package;>;
		lambda$scrapModule$1(Map$Entry) : null
		lambda$scrapModule$0(Entity) : null

	class ClassFileParser{
		<init>() : null
		parseClass(InputStream) : null

	class ClassFileParser_1{
		visit(int, int, String, String, String, String[]) : null
		visitRecordComponent(String, String, String) : null
		visitField(int, String, String, String, Object) : null
		visitMethod(int, String, String, String, String[]) : null

	class ClassFileParser_EntityBuilder{
		String : name
		<init>() : null
		name(String) : null
		build() : null

	class Main{
		<init>() : null
		exitWithError(String) : null
		writeToPath(Path, String) : null
		convertFile(Parser, Path, Path) : null
		main(String[]) : null
		mermaid(String[]) : null
		plantUml(String[]) : null

	class JarReader{
		Path : DEFAULT_SEARCH_DIRECTORY
		<init>() : null
		isPublic(int) : null
		isPrivate(int) : null
		isProtected(int) : null
		isFinal(int) : null
		isStatic(int) : null
		getVisibility(int) : null
		modifiers(int) : (I)Ljava/util/HashSet<Lcom/github/forax/umldoc/core/Modifier;>;
		isRecord(int) : null
		isInterface(int) : null
		isEnum(int) : null
		isAnnotation(int) : null
		isAbstract(int) : null
		isModule(int) : null
		stereotype(int) : null
		entityName(String) : null
		getEntities() : ()Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;
		getEntities(Path) : (Ljava/nio/file/Path;)Ljava/util/List<Lcom/github/forax/umldoc/core/Entity;>;

	class Parser{
		<<INTERFACE>>
		parse(Package) : null

	class Main_OutputFormat{
		<<ENUM>>
		Main$OutputFormat : MERMAID
		Main$OutputFormat : PLANT_UML
		Supplier : parserSupplier
		Main$OutputFormat[] : $VALUES
		values() : null
		valueOf(String) : null
		<init>(String, int, Supplier) : (Ljava/util/function/Supplier<Lcom/github/magickoders/Parser;>;)V
		getParser() : null
		$values() : null

	class JarReader_1{
		visit(int, int, String, String, String, String[]) : null
		visitRecordComponent(String, String, String) : null
		visitField(int, String, String, String, Object) : null
		visitMethod(int, String, String, String, String[]) : null

	class MermaidParser{
		<init>() : null
		parseField(Field) : null
		parseEntity(Entity) : null
		parse(Package) : null

	class PlantumlParser{
		<init>() : null
		parseField(Field) : null
		parseEntity(Entity) : null
		parse(Package) : null

	class module-info{

```

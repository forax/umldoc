package com.github.forax.umldoc.classfile;

import static com.github.forax.umldoc.core.AssociationDependency.Cardinality.MANY;
import static com.github.forax.umldoc.core.AssociationDependency.Cardinality.ONLY_ONE;
import static com.github.forax.umldoc.core.AssociationDependency.Cardinality.ZERO_OR_ONE;
import static java.util.stream.Collectors.groupingBy;

import com.github.forax.umldoc.classfile.ClassFileParser.ParsingResult;
import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.AssociationDependency.Cardinality;
import com.github.forax.umldoc.core.AssociationDependency.Side;
import com.github.forax.umldoc.core.Dependency;
import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.Package;
import com.github.forax.umldoc.core.TypeInfo;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.module.ModuleReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collect all the entities and dependencies as a list of packages.
 */
public final class ModuleScrapper {
  private ModuleScrapper() {
    throw new AssertionError();
  }

  private static String packageName(String entityName) {
    var index = entityName.lastIndexOf('.');
    return index == -1 ? "" : entityName.substring(0, index);
  }

  private static void parseModule(ModuleReference moduleReference, Consumer<ParsingResult> consumer)
      throws IOException {
    try (var reader = moduleReference.open()) {
      for (var filename : (Iterable<String>) reader.list()::iterator) {
        if (!filename.endsWith(".class") || filename.endsWith("-info.class")) {
          continue;
        }
        try (var inputStream = reader.open(filename).orElseThrow()) {
          var parsingResult = ClassFileParser.parseClass(inputStream);
          consumer.accept(parsingResult);
        }
      }
    }
  }

  private static String javaClassName(TypeInfo typeInfo) {
    return typeInfo.outer().map(outer -> javaClassName(outer) + "$").orElse("") + typeInfo.name();
  }

  // should be a local class inside #resolvePackage, but it's trigger a false positive by SpotBug
  private record AssociationInfo(Set<Modifier> modifiers, String leftClassName,
                                  String rightClassName, String label, Cardinality cardinality) {}

  private static Package resolvePackage(String packageName, List<ParsingResult> parsingResults) {
    var entityNames = parsingResults.stream()
        .map(parsingResult -> javaClassName(parsingResult.entityBuilder().type()))
        .collect(Collectors.toSet());

    var entityMap = new LinkedHashMap<String, Entity>();
    var associations = new ArrayList<AssociationInfo>();
    for (var parsingResult : parsingResults) {
      var entityBuilder = parsingResult.entityBuilder();
      for (var delegation : parsingResult.delegations()) {
        var cardinalityInfo = findCardinality(delegation.type());
        if (!delegation.modifiers().contains(Modifier.STATIC)
            && entityNames.contains(javaClassName(cardinalityInfo.type))) {
          // association
          associations.add(new AssociationInfo(delegation.modifiers(),
              javaClassName(entityBuilder.type()),
              javaClassName(cardinalityInfo.type),
              delegation.label(),
              cardinalityInfo.cardinality));
        } else {
          // field
          entityBuilder.addField(delegation.modifiers(),
              delegation.label(),
              delegation.type());
        }
      }
      var entity = entityBuilder.build();
      entityMap.put(javaClassName(entity.type()), entity);
    }

    //

    var dependencies = associations.stream()
        .<Dependency>map(a -> {
          return new AssociationDependency(
              new Side(entityMap.get(a.leftClassName),
                  Optional.empty(),
                  false,
                  ONLY_ONE),
              new Side(entityMap.get(a.rightClassName),
                  Optional.of(a.label),
                  true,
                  a.cardinality));
        })
        .toList();

    return new Package(packageName, List.copyOf(entityMap.values()), dependencies);
  }


  // package private for testing
  record CardinalityInfo(Cardinality cardinality, TypeInfo type) {}

  // package private for testing
  static CardinalityInfo findCardinality(TypeInfo typeInfo) {
    if (!typeInfo.name().startsWith("java.")) {
      return new CardinalityInfo(ONLY_ONE, typeInfo);
    }
    Class<?> clazz;
    var className = javaClassName(typeInfo);
    try {
      clazz = Class.forName(className, false, ClassFileParser.class.getClassLoader());
    } catch (ClassNotFoundException e) {
      return new CardinalityInfo(ONLY_ONE, typeInfo);
    }

    // TODO: write a real type argument substitution algorithm
    var typeParameters = typeInfo.typeParameters();
    if ((Iterable.class.isAssignableFrom(clazz)
        || Stream.class.isAssignableFrom(clazz)) && typeParameters.size() == 1) {
      return new CardinalityInfo(MANY, typeParameters.get(0));
    }
    if (Map.class.isAssignableFrom(clazz) && typeParameters.size() == 2) {
      return new CardinalityInfo(MANY, typeParameters.get(1));
    }
    if (Optional.class.isAssignableFrom(clazz) && typeParameters.size() == 1) {
      return new CardinalityInfo(ZERO_OR_ONE, typeParameters.get(0));
    }
    return new CardinalityInfo(ONLY_ONE, typeInfo);
  }

  private static String outerMostName(TypeInfo typeInfo) {
    if (typeInfo.outer().isPresent()) {
      return outerMostName(typeInfo.outer().orElseThrow());
    }
    return typeInfo.name();
  }

  /**
   * Returns a list of packages contained in the module reference taken as argument.
   *
   * @param moduleReference the module reference
   * @return a list of packages
   * @throws IOException if an I/O error occurs
   */
  public static List<Package> scrapModule(ModuleReference moduleReference) throws IOException {
    Map<String, List<ParsingResult>> parsingResultMap;
    try {
      parsingResultMap = Stream.of(moduleReference)
          .<ParsingResult>mapMulti((module, consumer) -> {
            try {
              parseModule(module, consumer);
            } catch (IOException e) {
              throw new UncheckedIOException(e);
            }
          })
          .collect(groupingBy(result -> packageName(outerMostName(result.entityBuilder().type()))));
    } catch (UncheckedIOException e) {
      throw e.getCause();
    }
    return parsingResultMap.entrySet().stream()
      .map(entry -> resolvePackage(entry.getKey(), entry.getValue()))
      .toList();
  }
}

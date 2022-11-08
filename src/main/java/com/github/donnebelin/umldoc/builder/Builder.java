package com.github.donnebelin.umldoc.builder;

/**
 * Super-type for builder : it takes a String and returns the corresponding object.
 */
public sealed interface Builder<T> permits
        GeneratorBuilder.MermaidBuilder,
        GeneratorBuilder.PlantBuilder,
        TypeInfoBuilder {
  T build(String signature);
}

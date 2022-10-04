package com.github.forax.umldoc.core;

/**
 * A field on an {@link Entity}.
 *
 * @param modifier the field modifier
 * @param name the name of the field
 * @param type the type of the field
 */
public record Field(Modifier modifier, String name, String type) {
}

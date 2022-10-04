package com.github.forax.umldoc.core;

import java.util.List;
import java.util.Optional;

/**
 * An entity (a class, an interface, a record, etc).
 *
 * @param modifier the entity modifier
 * @param stereotype a stereotype if it exists
 * @param fields the fields of the entity
 * @param methods the methods of the entity
 */
public record Entity(Modifier modifier, Optional<String> stereotype,
                     List<Field> fields, List<Method> methods) { }

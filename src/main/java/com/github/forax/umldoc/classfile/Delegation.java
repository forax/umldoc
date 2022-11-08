package com.github.forax.umldoc.classfile;

import static java.util.Objects.requireNonNull;

import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.Set;

/**
 * Represent either a field or an association.
 *
 * @param modifiers the delegation modifiers
 * @param type the right type of the delegation
 * @param label the delegation label
 */
record Delegation(Set<Modifier> modifiers, TypeInfo type, String label) {
  Delegation {
    requireNonNull(modifiers);
    requireNonNull(type);
    requireNonNull(label);
  }
}
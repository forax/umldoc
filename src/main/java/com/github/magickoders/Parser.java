package com.github.magickoders;

import com.github.forax.umldoc.core.Package;

/**
 * Abstraction of a parser of Package to String.
 */
public sealed interface Parser permits MermaidParser, PlantumlParser {
  String parse(Package p);
}

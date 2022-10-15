package com.github.magickoders;

import com.github.forax.umldoc.core.Package;

/**
 * Abstraction of a parser of Package to String.
 */
public interface Parser {
  String parse(Package p);
}

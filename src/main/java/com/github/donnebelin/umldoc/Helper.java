package com.github.donnebelin.umldoc;

import com.github.forax.umldoc.core.AssociationDependency.Cardinality;

/**
 * Helper class.
 */
public class Helper {

  /**
   * Parse a Cardinality object into a String representation.
   *
   * @param card The cardinality object to parse into a String
   * @return The String representation of card parameter : 1 | * | 0..1
   */
  public static String parseCardinalities(Cardinality card) {
    return switch (card) {
      case ONLY_ONE -> "1";
      case MANY -> "*";
      case ZERO_OR_ONE -> "0..1";
    };
  }
}

package com.github.forax.umldoc.core;

import static java.util.Objects.requireNonNull;

import java.util.List;

/**
 * Either a method call or a group of method calls.
 */
public sealed interface Call {
  /**
   * A method call.
   *
   * @param type type target type of the methid call
   * @param name name of the method call
   * @param returnType return type of the method call
   * @param parameterTypes parameter types of the method call
   */
  record MethodCall(TypeInfo type, String name, TypeInfo returnType,
                    List<TypeInfo> parameterTypes)
      implements Call {

    /**
     * Creates a method call.
     */
    public MethodCall {
      requireNonNull(type);
      requireNonNull(name);
      requireNonNull(returnType);
      parameterTypes = List.copyOf(parameterTypes);
    }
  }

  /**
   * A group of calls, each one can be a method call or a group.
   *
   * @param kind the kind of group
   * @param calls the method calls or groups
   */
  record Group(Kind kind, List<Call> calls) implements Call {
    /**
     * Creates a group of calls.
     */
    public Group {
      requireNonNull(kind);
      calls = List.copyOf(calls);
    }

    /**
     * An empty group of calls.
     */
    public static final Group EMPTY_GROUP = new Group(Kind.NONE, List.of());

    /**
     * The kind of group.
     */
    public enum Kind {
      /**
       * A group of method calls with no special semantics.
       */
      NONE,
      /**
       * A group of method calls inside a loop.
       */
      LOOP,
      /**
       * A group of method calls, called in parallel.
       */
      PARALLEL
    }
  }
}

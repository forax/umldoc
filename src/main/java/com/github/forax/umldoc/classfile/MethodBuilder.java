package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Method;
import java.util.Objects;

/**
 * A builder for a {@link Method}.
 */
public class MethodBuilder {
  private final Method.Signature signature;
  private final CallGroupBuilder callGroupBuilder = new CallGroupBuilder(Call.Group.Kind.NONE);

  /**
   * Constructor of MethodBuilder.
   *
   * @param signature the signature of the method
   */
  public MethodBuilder(Method.Signature signature) {
    this.signature = Objects.requireNonNull(signature);
  }

  /**
   * Add a call to the {@link com.github.forax.umldoc.core.Call.Group} of the method.
   *
   * @param call the call to add to the group
   * @return the instance of {@link MethodBuilder}
   */
  public MethodBuilder addCallToGroup(Call call) {
    Objects.requireNonNull(call);
    this.callGroupBuilder.add(call);
    return this;
  }

  public Method build() {
    return new Method(signature, callGroupBuilder.build());
  }
}

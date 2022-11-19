package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.core.Call;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A {@link com.github.forax.umldoc.core.Call.Group} builder.
 */
public class CallGroupBuilder {
  private final ArrayList<Call> calls = new ArrayList<>();
  private final Call.Group.Kind kind;

  public CallGroupBuilder(Call.Group.Kind kind) {
    Objects.requireNonNull(kind);
    this.kind = kind;
  }


  /**
   * Adds a call to the group.
   *
   * @param call the call to add
   */
  public void add(Call call) {
    Objects.requireNonNull(call);
    calls.add(call);
  }

  /**
   * Builds a {@link com.github.forax.umldoc.core.Call.Group} from the calls added.
   *
   * @return the {@link com.github.forax.umldoc.core.Call.Group}
   */
  public Call.Group build() {
    return new Call.Group(Call.Group.Kind.NONE, calls);
  }
}

package com.github.donnebelin.umldoc.builder;

import com.github.forax.umldoc.core.TypeInfo;
import java.util.Objects;
import org.objectweb.asm.Type;

/**
 * Build a TypeInfo instance from a typeParameter in a String.
 * No exception is thrown if the given String is invalid.
 */
public record TypeInfoBuilder() implements Builder<TypeInfo> {
  @Override
  public TypeInfo build(String typeParameter) {
    Objects.requireNonNull(typeParameter);
    var signature = Type.getType(typeParameter).getInternalName();
    var elements = signature.split("<");
    var finalType = TypeInfo.of(elements[0]);
    for (var i = 1; i < elements.length; i++) {
      finalType = finalType.withTypeParameter(
              TypeInfo.of(Type.getType(elements[i].replace(";", ""))
                      .getInternalName()));
    }

    return finalType;
  }
}

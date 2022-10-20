package com.github.magickoders.jar;

import com.github.forax.umldoc.core.Entity;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class EntityBuilderTest {

  @Test
  public void precondition() {
    var entityBuilder = new EntityBuilder();
    assertAll(
            () -> assertThrows(NullPointerException.class, () -> entityBuilder.setTypeInfo(null)),
            () -> assertThrows(NullPointerException.class, () -> entityBuilder.setStereotype(null)),
            () -> assertThrows(NullPointerException.class, () -> entityBuilder.setModifiers(null)),
            () -> assertThrows(NullPointerException.class, () -> entityBuilder.addField(null)),
            () -> assertThrows(NullPointerException.class, () -> entityBuilder.addMethod(null))
    );
  }

  @Test
  public void noModifiersShouldThrow() {
    var entityBuilder = new EntityBuilder();
    entityBuilder.setStereotype(Entity.Stereotype.CLASS)
                 .setTypeInfo(new TypeInfo(Optional.empty(), "test", List.of()));
    assertThrows(IllegalStateException.class, entityBuilder::build);
  }

  @Test
  public void noTypeShouldThrow() {
    var entityBuilder = new EntityBuilder();
    entityBuilder.setStereotype(Entity.Stereotype.CLASS)
                 .setModifiers(Set.of());
    assertThrows(IllegalStateException.class, entityBuilder::build);
  }

  @Test
  public void noStereotypeShouldThrow() {
    var entityBuilder = new EntityBuilder();
    entityBuilder.setTypeInfo(new TypeInfo(Optional.empty(), "test", List.of()))
                 .setModifiers(Set.of());
    assertThrows(IllegalStateException.class, entityBuilder::build);
  }

  // TODO test integrity of built entities
}

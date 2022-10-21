package com.github.magickoders.jar;

public class JarReaderTest {

  // FIXME
//  @Test
//  public void listNotNull() throws IOException {
//    var entities = JarReader.getEntities(Path.of("target/classes"))
//                            .stream()
//                            .filter(entity -> entity.name()
//                                                    .contains("Dependency"))
//                            .toList();
//
//    assertAll(
//            () -> assertNotNull(entities),
//            () -> assertFalse(entities.isEmpty())
//    );
//  }
//
//  @Test
//  public void entitiesOk() throws IOException {
//    var entities = JarReader.getEntities(Path.of("target/classes"))
//                            .stream()
//                            .filter(entity -> entity.name()
//                                                    .contains("Entity"))
//                            .toList();
//
//    var entity = entities.stream()
//                         .filter(e -> e.name()
//                                       .substring(e.name()
//                                                   .lastIndexOf(".") + 1)
//                                       .contains("Entity"))
//                         .findFirst()
//                         .orElseThrow();
//    assertEquals(Set.of(Modifier.PUBLIC, Modifier.FINAL), entity.modifiers());
//    assertEquals(Stereotype.RECORD, entity.stereotype());
//  }
}

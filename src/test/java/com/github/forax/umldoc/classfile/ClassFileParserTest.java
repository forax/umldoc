package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.classfile.ClassFileParser.ParsingResult;
import com.github.forax.umldoc.core.TypeInfo;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ClassFileParserTest {
  private static ParsingResult parseClass(Class<?> clazz) throws IOException {
    try(var inputStream = ClassFileParserTest.class.getResourceAsStream("/"  + clazz.getName().replace('.', '/') + ".class")) {
      return ClassFileParser.parseClass(inputStream);
    }
  }

  static class EmptyClass { }

  @Test
  public void parseEmptyClass() throws IOException {
    var parsingResult = parseClass(EmptyClass.class);
    assertEquals(EmptyClass.class.getName(), parsingResult.entityBuilder().type().name());
  }

  static class Company {
    Employee employee;
  }
  static class Employee {
    String name;
  }

  @Test
  public void parseCompanyClass() throws IOException {
    var parsingResult = parseClass(Company.class);
    assertEquals(Company.class.getName(), parsingResult.entityBuilder().type().name());
    assertEquals(
            List.of(new Delegation(Set.of(), TypeInfo.of(Employee.class.getName()), "employee")),
            parsingResult.delegations());
  }

  /*
  static class Storage {
    List<Pet> petList;
    Map<String, Pet> petMap;
    Stream<Pet> petStream;
    Optional<Pet> petOptional;
  }
  static class Pet {
    String name;
  }

  @Test
  public void parseStorageClass() throws IOException {
    var parsingResult = parseClass(Storage.class);
    assertEquals(Storage.class.getName(), parsingResult.entityBuilder().name());
    assertEquals(
        Set.of(
            new Delegation(Set.of(), Storage.class.getName(), Employee.class.getName(), "employee", Cardinality.ONLY_ONE, Employee.class.getName()),

            ),
        Set.copyOf(parsingResult.delegations()));
  }*/

  @Test
  public void canNotBeInstantiated() throws InstantiationException, IllegalAccessException {
    var constructor = ClassFileParser.class.getDeclaredConstructors()[0];
    constructor.setAccessible(true);
    try {
      constructor.newInstance();
      fail();
    } catch (InvocationTargetException e) {
      assertInstanceOf(AssertionError.class, e.getCause());
    }
  }
}
package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import static com.github.forax.umldoc.core.Call.Group.EMPTY_GROUP;

final class MethodBuilder {
  private final GroupBuilder builder = new GroupBuilder();
  private final Set<Modifier> modifiers;
  private final String name;
  private final List<Method.Parameter> parameters;
  private final TypeInfo returnType;

  public MethodBuilder(Set<Modifier> modifiers, String name, TypeInfo returnType, List<Method.Parameter> parameters) {
    this.modifiers = Objects.requireNonNull(modifiers);
    this.name = Objects.requireNonNull(name);
    this.returnType = Objects.requireNonNull(returnType);
    this.parameters = Objects.requireNonNull(parameters);
  }

  public void addInstruction(InstructionType type, String instructionName) {
    Objects.requireNonNull(type);
    Objects.requireNonNull(instructionName);
    builder.addInstruction(type, instructionName);
  }

  public void addMethod(String instructionName, Call.MethodCall method) {
    Objects.requireNonNull(instructionName);
    Objects.requireNonNull(method);
    builder.addMethod(instructionName, method);
  }

  public Method build(){
    var groupCall = builder.build();
    return new Method(modifiers, name, returnType, parameters, groupCall);
  }

  static class GroupBuilder {
    private final HashSet<String> allInstructions = new HashSet<>();
    private  final ArrayDeque<Instruction> methodInstructions = new ArrayDeque<>();
    private final HashMap<String, GroupBuilder> groups = new HashMap<>();
    private final HashMap<String, ArrayList<Call.MethodCall>> methods = new HashMap<>();
    private Call.Group.Kind kind = Call.Group.Kind.NONE;

    void addInstruction(InstructionType type, String instructionName) {
      if (allInstructions.contains(instructionName)) {
        resolveGroup(type, instructionName);
        return;
      }
      allInstructions.add(instructionName);
      methodInstructions.add(new Instruction(type, instructionName));
    }

    void addMethod(String instructionName, Call.MethodCall methodCall) {
      var list = methods.computeIfAbsent(instructionName, l -> new ArrayList<>());
      list.add(methodCall);
    }

    void addMethods(String instructionName, List<Call.MethodCall> methodCall) {
      var list = methods.computeIfAbsent(instructionName, l -> new ArrayList<>());
      list.addAll(methodCall);
    }

    Call.Group build() {
      //TODO Optional<Call.Group> if no methods found into GroupBuilder ?
      var callsList = new ArrayList<Call>();
      var instruction = methodInstructions.pollLast();
      while (!(instruction == null)) {
        // If the type of the instruction is IF then add the associated Call.Group otherwise add methods associated to the instruction
        if(instruction.type == InstructionType.IF){
          callsList.add(groups.get(instruction.instructionName).build());
        } else {
          var instructionMethods = methods.get(instruction.instructionName);
          if(instructionMethods != null){
            callsList.addAll(instructionMethods);
          }
        }
      }
      return new Call.Group(kind, callsList);
    }

    void setKind(Call.Group.Kind kind) {
      this.kind = kind;
    }

    void addGroup(String instructionName, GroupBuilder group) {
      groups.put(instructionName, group);
    }

    private boolean containsInstruction(String instructionName) {
      /*
      if (isInsideGroup) {
        return groups.getLast().containsInstruction(instructionName);
      }
      */
      return allInstructions.contains(instructionName);
    }

    private void resolveGroup(InstructionType type, String instructionName) {
      var newGroup = new GroupBuilder();
      var instruction = methodInstructions.pollLast();
      while (!(instruction == null) && !instruction.instructionName.equals(instructionName)) {
        if (instruction.type == InstructionType.IF) {
          // If loop is conditional create a new group and adding the current one
          if (type == InstructionType.GOTO) {
            newGroup.setKind(Call.Group.Kind.OPTIONAL);
            var lastGroup = newGroup;
            newGroup = new GroupBuilder();
            newGroup.addInstruction(InstructionType.IF, instruction.instructionName);
            newGroup.addGroup(instruction.instructionName, lastGroup);
          }
          //newGroup.addInstruction();
        } else if (instruction.type == InstructionType.GOTO) {
          //Should be if/else
        } else {
          //Add current instruction methods to the new group
          var instructionMethods = methods.remove(instructionName);
          if (instructionMethods != null) {
            newGroup.addMethods(instruction.instructionName, instructionMethods);
          }
          addGroup(instructionName, newGroup);
          allInstructions.remove(instruction.instructionName);
        }
        instruction = methodInstructions.pollLast();
      }
      if (instruction == null) {
        // Instruction should never being null (maybe it should be replaced by NullPointerException)
        throw new AssertionError("Instruction can't be null");
      }
      // Set kind of the new group
      if (type == InstructionType.GOTO) {
        newGroup.setKind(Call.Group.Kind.LOOP);
      } else if (type == InstructionType.IF) {
        newGroup.setKind(Call.Group.Kind.OPTIONAL);
      } else {
        // This should never being called
        throw new AssertionError("We should never resolve a group type of NONE");
      }
      //TODO check if we should re-add the current instruction
      methodInstructions.add(instruction);
      groups.put(instructionName, newGroup);
    }
  }

  record Instruction(InstructionType type, String instructionName) {}

  public enum InstructionType { GOTO, IF, NONE }
}

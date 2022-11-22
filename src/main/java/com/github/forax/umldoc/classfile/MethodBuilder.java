package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A builder for a {@link Method}.
 */
public class MethodBuilder {
  private final GroupBuilder builder = new GroupBuilder();
  private final Set<Modifier> modifiers;
  private final String name;
  private final TypeInfo returnTypeInfo;
  private final List<Method.Parameter> parameters;
  private final String descriptor;

  /**
   * Constructor of MethodBuilder.
   *
   * @param modifiers the modifiers of the method
   * @param name the name of the method
   * @param returnTypeInfo the return type of the method
   * @param parameters the list of parameters of the method
   * @param descriptor the descriptor
   */
  public MethodBuilder(Set<Modifier> modifiers, String name, TypeInfo returnTypeInfo,
                       List<Method.Parameter> parameters, String descriptor) {
    Objects.requireNonNull(modifiers);
    Objects.requireNonNull(name);
    Objects.requireNonNull(returnTypeInfo);
    Objects.requireNonNull(descriptor);
    Objects.requireNonNull(parameters);
    this.modifiers = Set.copyOf(modifiers);
    this.name = name;
    this.returnTypeInfo = returnTypeInfo;
    this.descriptor = descriptor;
    this.parameters = List.copyOf(parameters);
  }

  /**
   * Add instruction to the GroupBuilder.
   *
   * @param type instruction type
   * @param instructionName instruction label name
   */
  public void addInstruction(InstructionType type, String instructionName) {
    Objects.requireNonNull(type);
    Objects.requireNonNull(instructionName);
    builder.addInstruction(type, instructionName);
  }

  /**
   * Add a method call to the GroupBuilder.
   *
   * @param instructionName instruction label name
   * @param method method call associated to the instruction
   */
  public void addMethod(String instructionName, Call.MethodCall method) {
    Objects.requireNonNull(instructionName);
    Objects.requireNonNull(method);
    builder.addMethod(instructionName, method);
  }


  /**
   * Build the method.
   *
   * @return a new method
   */
  public Method build() {
    return new Method(modifiers,
            name,
            returnTypeInfo,
            parameters,
            descriptor,
            builder.build().orElse(Call.Group.EMPTY_GROUP));
  }

  static class GroupBuilder {
    private  final ArrayList<Instruction> methodInstructions = new ArrayList<>();
    private  final ArrayList<Instruction> methodInstructionsFormatted = new ArrayList<>();
    private final HashMap<String, GroupBuilder> groups = new HashMap<>();
    private final HashMap<String, ArrayList<Call.MethodCall>> methods = new HashMap<>();
    private Call.Group.Kind kind = Call.Group.Kind.NONE;

    void addInstruction(InstructionType type, String instructionName) {
      methodInstructions.add(new Instruction(type, instructionName));
    }

    void addInstruction(Instruction instruction) {
      methodInstructions.add(instruction);
    }


    void addMethod(String instructionName, Call.MethodCall methodCall) {
      var list = methods.computeIfAbsent(instructionName, l -> new ArrayList<>());
      list.add(methodCall);
    }

    void addMethods(String instructionName, List<Call.MethodCall> methodCall) {
      var list = methods.computeIfAbsent(instructionName, l -> new ArrayList<>());
      list.addAll(methodCall);
    }

    Optional<Call.Group> build() {
      resolveGroups();
      var callsList = new ArrayList<Call>();
      for (var instruction : methodInstructionsFormatted) {
        var instructionMethods = methods.remove(instruction.instructionName());
        if (instructionMethods != null) {
          callsList.addAll(instructionMethods);
        }
        var getGroup = groups.remove(instruction.instructionName());
        if (getGroup != null) {
          getGroup.build().ifPresent(callsList::add);
        }
      }
      if (callsList.isEmpty()) {
        return Optional.empty();
      }
      return Optional.of(new Call.Group(kind, callsList));
    }

    void setKind(Call.Group.Kind kind) {
      this.kind = kind;
    }

    private void resolveGroups() {
      int posInstruction1 = 0;
      // For each instruction of the GroupBuilder
      while (posInstruction1 < methodInstructions.size()) {
        var currentInstruction = methodInstructions.get(posInstruction1);
        // Search for another instruction that should be the end of the statement
        var posInstruction2 = searchFromIndexInstruction(
                posInstruction1 + 1,
                      currentInstruction.instructionName());

        if (posInstruction2 >= 0) {
          var instruction2 = methodInstructions.get(posInstruction2);
          // Manage OPTIONAL and ALTERNATE statement
          if (currentInstruction.type() == InstructionType.IF
                  && instruction2.type() == InstructionType.NONE) {

            // Check if statement is alternate
            var loopInstruction = extractLoopInstruction(posInstruction1, posInstruction2);
            var alternateEndPoint = loopInstruction.stream()
                    .mapToInt(instruction -> searchFromIndexInstruction(posInstruction2 + 1,
                            instruction.instructionName()))
                    .filter(index -> index > 0).findFirst();

            // If statement is alternate
            if (alternateEndPoint.isPresent()) {
              // Add current instruction to
              methodInstructionsFormatted.add(currentInstruction);
              posInstruction1++;
              var groupIf = new GroupBuilder();
              var groupElse = new GroupBuilder();
              groupIf.setKind(Call.Group.Kind.ALTERNATE);
              groupElse.setKind(Call.Group.Kind.ALTERNATE);
              // Generate and add IF group
              generateStatement(posInstruction1, posInstruction2 - 1, groupIf);
              groups.put(currentInstruction.instructionName(), groupIf);
              // Get the GOTO instruction and associate it with the ELSE group
              var secondGroupEndPointInstruction = methodInstructions.get(posInstruction1);
              methodInstructionsFormatted.add(secondGroupEndPointInstruction);
              posInstruction1++;
              // Generate and add ELSE group
              var posInstruction3 = alternateEndPoint.getAsInt();
              generateStatement(posInstruction1, posInstruction3 - 2, groupElse);
              groups.put(secondGroupEndPointInstruction.instructionName(), groupElse);
            } else {
              // Add current instruction to
              methodInstructionsFormatted.add(currentInstruction);
              posInstruction1++;
              // If is optional statement add instruction to a new group
              var groupIf = new GroupBuilder();
              groupIf.setKind(Call.Group.Kind.OPTIONAL);
              // Generate and add IF group
              generateStatement(posInstruction1, posInstruction2, groupIf);
              groups.put(currentInstruction.instructionName(), groupIf);
              methodInstructionsFormatted.add(methodInstructions.get(posInstruction1));
              posInstruction1++;
            }
          } else if (currentInstruction.type() == InstructionType.NONE
                  && instruction2.type() == InstructionType.GOTO) {
            //Manage LOOP statement
            methodInstructionsFormatted.add(currentInstruction);
            posInstruction1++;
            //System.out.println("GOTO position instruction1" + posInstruction1);
            var groupLoop = new GroupBuilder();
            groupLoop.setKind(Call.Group.Kind.LOOP);
            // Add current instruction methods which are related to the loop
            addInstructionWithMethodsToGroup(currentInstruction, groupLoop);
            // Generate and add IF group
            generateStatement(posInstruction1, posInstruction2, groupLoop);
            groups.put(currentInstruction.instructionName(), groupLoop);
            posInstruction1++;
          } else {
            //TODO manage unhandled statement
            //throw new AssertionError("Unhandled statement.");
            posInstruction1++;
          }

        } else {
          // No end instruction found, so it is a statement inside another one
          if (currentInstruction.type() == InstructionType.IF) {
            // Add current instruction to
            methodInstructionsFormatted.add(currentInstruction);
            posInstruction1++;
            // If is optional statement add instruction to a new group
            var groupIf = new GroupBuilder();
            groupIf.setKind(Call.Group.Kind.OPTIONAL);
            // Generate and add IF group
            generateStatement(posInstruction1, methodInstructions.size() - 1, groupIf);
            groups.put(currentInstruction.instructionName(), groupIf);
            methodInstructionsFormatted.add(methodInstructions.get(posInstruction1));
            posInstruction1++;
          } else {
            // If there is no special semantic simply add instruction
            // to the formatted list of instructions
            methodInstructionsFormatted.add(currentInstruction);
            posInstruction1++;
          }
        }
      }
    }

    private void generateStatement(int at, int to, GroupBuilder group) {
      for (int i = at; i < to; i++) {
        addInstructionWithMethodsToGroup(methodInstructions.remove(at), group);
      }
    }

    private int searchFromIndexInstruction(int index, String instructionName) {
      var foundIndex = methodInstructions.subList(index, methodInstructions.size()).stream()
              .map(Instruction::instructionName).toList().indexOf(instructionName);
      if (foundIndex < 0) {
        return foundIndex;
      }
      return index + foundIndex;
    }

    private List<Instruction> extractLoopInstruction(int fromIndex, int toIndex) {
      return methodInstructions.subList(fromIndex, toIndex).stream()
              .filter(instruction -> instruction.type() == InstructionType.GOTO).toList();
    }

    private void addInstructionWithMethodsToGroup(Instruction instruction, GroupBuilder group) {
      var instructionMethods = this.methods.remove(instruction.instructionName());
      if (instructionMethods != null) {
        group.addMethods(instruction.instructionName(), instructionMethods);
      }
      group.addInstruction(instruction);
    }

  }

  record Instruction(InstructionType type, String instructionName) {}

  /**
   * Enum of the different kinds of instruction.
   */
  public enum InstructionType { GOTO, IF, NONE }
}

package com.github.forax.umldoc.classfile;

import com.github.forax.umldoc.core.Call;
import com.github.forax.umldoc.core.Method;
import com.github.forax.umldoc.core.Modifier;
import com.github.forax.umldoc.core.TypeInfo;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class MethodBuilder {
  private final GroupBuilder builder = new GroupBuilder();
  private final Set<Modifier> modifiers;
  private final String name;
  private final List<Method.Parameter> parameters;
  private final TypeInfo returnType;

  public MethodBuilder(Set<Modifier> modifiers, String name, TypeInfo returnType,
                       List<Method.Parameter> parameters) {
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

  public Method build() {
    return new Method(modifiers, name, returnType, parameters,
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

    private void addInstruction(Instruction instruction) {
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
        //System.out.println("Instruction1:" + currentInstruction);
        // Search for another instruction that should be the end of the statement
        var posInstruction2 = searchFromIndexInstruction(posInstruction1 + 1,
                currentInstruction.instructionName());
        //System.out.println("POS2:" + posInstruction2);
        if (posInstruction2 >= 0) {
          var instruction2 = methodInstructions.get(posInstruction2);
          //System.out.println("Instruction2:" + instruction2);
          //System.out.println("PositionInstruction2:" + posInstruction2);
          // Manage OPTIONAL and ALTERNATE statement
          if (currentInstruction.type() == InstructionType.IF
                  && instruction2.type() == InstructionType.NONE) {
            // Check if statement is alternate
            var loopInstruction = extractLoopInstruction(0, posInstruction2);
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
              for (int i = posInstruction1; i < posInstruction2 - 1; i++) {
                var extractedInstruction = methodInstructions.remove(posInstruction1);
                //System.out.println("ExtractedG1:" + extractedInstruction);
                addInstructionWithMethodsToGroup(extractedInstruction, groupIf);
              }
              groups.put(currentInstruction.instructionName(), groupIf);
              // Get the GOTO instruction and associate it with the ELSE group
              var secondGroupEndPointInstruction = methodInstructions.get(posInstruction1);
              methodInstructionsFormatted.add(secondGroupEndPointInstruction);
              posInstruction1++;
              // Generate and add ELSE group
              var posInstruction3 = alternateEndPoint.getAsInt();
              for (int i = posInstruction1; i < posInstruction3 - 2; i++) {
                var extractedInstruction = methodInstructions.remove(posInstruction1);
                //System.out.println("ExtractedG2:" + extractedInstruction);
                addInstructionWithMethodsToGroup(extractedInstruction, groupElse);
              }
              groups.put(secondGroupEndPointInstruction.instructionName(), groupElse);
              //methodInstructionsFormatted.add(methodInstructions.remove(posInstruction1));
              //posInstruction1++;
            } else {
              // Add current instruction to
              methodInstructionsFormatted.add(currentInstruction);
              posInstruction1++;
              // If is optional statement add instruction to a new group
              var groupIf = new GroupBuilder();
              groupIf.setKind(Call.Group.Kind.OPTIONAL);
              // Generate and add IF group
              for (int i = posInstruction1; i < posInstruction2; i++) {
                var extractedInstruction = methodInstructions.remove(posInstruction1);
                //System.out.println("Extracted:" + extractedInstruction);
                addInstructionWithMethodsToGroup(extractedInstruction, groupIf);
              }
              groups.put(currentInstruction.instructionName(), groupIf);
              methodInstructionsFormatted.add(methodInstructions.get(posInstruction1));
              posInstruction1++;
            }
          } else if (currentInstruction.type() == InstructionType.NONE
                  && instruction2.type() == InstructionType.GOTO) {
            //Manage LOOP statement
            //System.out.println("GOTO add instruction to methodInstructionsFormatted:"
            //        + currentInstruction);
            methodInstructionsFormatted.add(currentInstruction);
            posInstruction1++;
            //System.out.println("GOTO position instruction1" + posInstruction1);
            var groupLoop = new GroupBuilder();
            groupLoop.setKind(Call.Group.Kind.LOOP);
            // Add current instruction methods which are related to the loop
            addInstructionWithMethodsToGroup(currentInstruction, groupLoop);
            // Generate and add IF group
            for (int i = posInstruction1; i < posInstruction2; i++) {
              var extractedInstruction = methodInstructions.remove(posInstruction1);
              //System.out.println("GOTO Extracted instruction:" + extractedInstruction);
              addInstructionWithMethodsToGroup(extractedInstruction, groupLoop);
            }
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
            var instructionsListSize = methodInstructions.size();
            for (int i = posInstruction1; i < instructionsListSize - 1; i++) {
              var extractedInstruction = methodInstructions.remove(posInstruction1);
              //System.out.println("Extracted:" + extractedInstruction);
              addInstructionWithMethodsToGroup(extractedInstruction, groupIf);
            }
            groups.put(currentInstruction.instructionName(), groupIf);
            methodInstructionsFormatted.add(methodInstructions.get(posInstruction1));
            posInstruction1++;
          } else {
            //System.out.println("SIMPLE INSTRUCTION ADDED:" + currentInstruction);
            // If there is no special semantic simply add instruction
            // to the formatted list of instructions
            methodInstructionsFormatted.add(currentInstruction);
            posInstruction1++;
          }
        }
      }
    }

    private int searchFromIndexInstruction(int index, String instructionName) {
      //System.out.println("START INDEX :" + index);
      var foundIndex = methodInstructions.subList(index, methodInstructions.size()).stream()
              .map(Instruction::instructionName).toList().indexOf(instructionName);
      if (foundIndex < 0) {
        return foundIndex;
      }
      //System.out.println("END INDEX :" + foundIndex);
      //System.out.println("END INDEX CONCAT :" + (index + foundIndex));
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

  public enum InstructionType { GOTO, IF, NONE }
}

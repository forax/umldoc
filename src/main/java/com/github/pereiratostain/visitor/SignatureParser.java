package com.github.pereiratostain.visitor;

import com.github.forax.umldoc.core.TypeInfo;
import java.util.List;
import java.util.Optional;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 * Class to parse Class fields and into Field.
 */
public class SignatureParser extends SignatureVisitor {

  private TypeInfo headInfo;

  protected SignatureParser(int api) {
    super(api);
  }

  public TypeInfo getInfo() {
    return headInfo;
  }

  @Override
  public SignatureVisitor visitArrayType() {
    throw new IllegalStateException("Not Implemented : visitArrayType()");
  }

  @Override
  public void visitBaseType(char descriptor) {
    throw new IllegalStateException("Not Implemented : visitArrayType()");
  }

  @Override
  public SignatureVisitor visitClassBound() {
    throw new IllegalStateException("Not supposed to be here (visitClassBound).");
  }

  @Override
  public void visitClassType(String className) {
    if (headInfo == null) {
      headInfo = TypeInfo.of(className);
    } else {
      headInfo = new TypeInfo(Optional.of(headInfo), className, List.of());
    }
  }

  @Override
  public void visitEnd() {
    if (headInfo.outer().isPresent()) {
      headInfo = headInfo.outer().orElseThrow().withTypeParameter(headInfo);
    }
  }

  @Override
  public SignatureVisitor visitExceptionType() {
    throw new IllegalStateException("Not supposed to be here (visitExceptionType).");
  }

  @Override
  public void visitFormalTypeParameter(String name) {
    throw new IllegalStateException("Not supposed to be here (visitFormalTypeParameter).");
  }

  @Override
  public void visitInnerClassType(String name) {
    throw new IllegalStateException("Not Implemented : visitInnerClassType()");
  }

  @Override
  public SignatureVisitor visitInterface() {
    throw new IllegalStateException("Not supposed to be here (visitInterface).");
  }

  @Override
  public SignatureVisitor visitInterfaceBound() {
    throw new IllegalStateException("Not supposed to be here (visitInterfaceBound).");
  }

  @Override
  public SignatureVisitor visitParameterType() {
    throw new IllegalStateException("Not supposed to be here (visitParameterType).");
  }

  @Override
  public SignatureVisitor visitReturnType() {
    throw new IllegalStateException("Not supposed to be here (visitReturnType).");
  }

  @Override
  public SignatureVisitor visitSuperclass() {
    throw new IllegalStateException("Not supposed to be here (visitSuperclass).");
  }

  @Override
  public void visitTypeArgument() {
    throw new IllegalStateException("Not Implemented : visitTypeArgument()");
  }

  @Override
  public SignatureVisitor visitTypeArgument(char wildcard) {
    return this;
  }

  @Override
  public void visitTypeVariable(String name) {
    throw new IllegalStateException("Not Implemented : visitTypeVariable()");
  }
}


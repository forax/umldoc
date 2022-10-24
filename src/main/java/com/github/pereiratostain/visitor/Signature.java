package com.github.pereiratostain.visitor;

import com.github.forax.umldoc.core.AssociationDependency;
import com.github.forax.umldoc.core.TypeInfo;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.List;
import java.util.Optional;

public class Signature extends SignatureVisitor {
    private String name = "";

    private TypeInfo headInfo;
    protected Signature(int api) {
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
        name = Visitor.removePath(className);
        if(headInfo == null) {
            headInfo = TypeInfo.of(name);
        } else {
            var newInfo = new TypeInfo(Optional.of(headInfo), name, List.of());
            headInfo = newInfo;
        }
        /*if(cardinality == null) {
            if (association_type.equals("Optional")) {
                cardinality = AssociationDependency.Cardinality.ZERO_OR_ONE;
            } else {
                cardinality = AssociationDependency.Cardinality.MANY;
            }
        }
        if(name != "") {
            name = name + "<";
        }
        name = name + association_type;*/
        //System.out.println(name + " " + recursivity_count);
    }

    @Override
    public void  visitEnd() {
        /*if(recursivity_count > 0) {
            name = name + ">";
        }*/
        if(headInfo.outer().isPresent()) {
            headInfo = headInfo.outer().get().withTypeParameter(headInfo);
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
    public SignatureVisitor  visitInterfaceBound() {
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


package org.ita.testrefactoring.astparser;

import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.ita.testrefactoring.codeparser.Expression;
import org.ita.testrefactoring.codeparser.Type;
import org.ita.testrefactoring.codeparser.VariableDeclarationStatement;

public class ASTVariableDeclarationStatement extends ASTAbstractStatement<VariableDeclaration> implements VariableDeclarationStatement {

	private Type variableType;
	private String variableName;
	private Expression initializationExpression;

	@Override
	public Type getVariableType() {
		return variableType;
	}

	protected void setVariableType(Type declarationType) {
		this.variableType = declarationType;
	}

	@Override
	public String getVariableName() {
		return variableName;
	}

	protected void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	@Override
	public Expression getInitialization() {
		return initializationExpression;
	}
	
	protected void setInitializationExpression(Expression initializationExpression) {
		this.initializationExpression = initializationExpression;
	}

}

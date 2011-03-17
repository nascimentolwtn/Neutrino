package org.ita.testrefactoring.astparser;

import java.util.ArrayList;
import java.util.List;

import org.ita.testrefactoring.codeparser.Constructor;
import org.ita.testrefactoring.codeparser.ConstructorInvocationExpression;
import org.ita.testrefactoring.codeparser.Expression;

public class ASTConstructorInvocationExpression extends ASTAbstractExpression<org.eclipse.jdt.core.dom.ClassInstanceCreation> implements ConstructorInvocationExpression {

	private Constructor constructor;
	private List<Expression> parameterList = new ArrayList<Expression>();
	
	@Override
	public Constructor getCalledConstructor() {
		return constructor;
	}
	
	protected void setCalledConstructor(Constructor constructor) {
		this.constructor = constructor;
	}

	@Override
	public List<Expression> getParameterList() {
		return parameterList;
	}

	@Override
	public String getValue() {
		return null;
	}

}

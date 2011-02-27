package org.ita.testrefactoring.astparser;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.ita.testrefactoring.metacode.MethodInvocationExpression;
import org.ita.testrefactoring.metacode.ParserException;
import org.ita.testrefactoring.metacode.Statement;
import org.ita.testrefactoring.metacode.Type;
import org.ita.testrefactoring.metacode.VariableDeclarationStatement;

class BlockParser {

	private class UnsupportedSintaxException extends ParserException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3227126996063055111L;

		public UnsupportedSintaxException() {
			super("Sintax não suportada.");
		}
	}

	private ASTBlock block;
	QuickVisitor quickVisitor = new QuickVisitor();

	public void setBlock(ASTBlock block) {
		this.block = block;
	}

	public void parse() throws ParserException {
		List<ASTNode> nodes = quickVisitor.quickVisit(block.getASTObject());

		for (ASTNode node : nodes) {
			parseStatement(node);
		}
	}

	private void parseStatement(ASTNode node) throws ParserException {
		// ConsoleVisitor.showNodes(node);

		Statement statement = null;

		if (node instanceof org.eclipse.jdt.core.dom.VariableDeclarationStatement) {
			statement = parseVariableDeclaration((org.eclipse.jdt.core.dom.VariableDeclarationStatement) node);
		} else if (node instanceof ReturnStatement) {
			statement = parseReturnStatement((ReturnStatement) node);
		} else {
//			throw new UnsupportedSintaxException();
		}

		block.getStatementList().add(statement);
	}

	/**
	 * Faz o parsing de uma declaração de variável.
	 * 
	 * @param node
	 * @return
	 * @throws ParserException
	 */
	private VariableDeclarationStatement parseVariableDeclaration(org.eclipse.jdt.core.dom.VariableDeclarationStatement node) throws ParserException {
		List<ASTNode> nodes = quickVisitor.quickVisit(node);

		if (!(nodes.get(0) instanceof org.eclipse.jdt.core.dom.Type)) {
			throw new UnsupportedSintaxException();
		}

		org.eclipse.jdt.core.dom.Type variableTypeNode = (org.eclipse.jdt.core.dom.Type) nodes.get(0);


		if (!(nodes.get(1) instanceof VariableDeclarationFragment)) {
			throw new UnsupportedSintaxException();
		}
		
		VariableDeclarationFragment variableFragment = (VariableDeclarationFragment) nodes.get(1);
		
		List<ASTNode> fragmentNodes = quickVisitor.quickVisit(variableFragment);
		
		if (!(fragmentNodes.get(0) instanceof SimpleName)) {
			throw new UnsupportedSintaxException();
		}
		
		SimpleName variableNameNode = (SimpleName) fragmentNodes.get(0);
		
		ASTEnvironment environment = getEnvironmentForBlock();

		Type variableType = environment.getTypeCache().get(variableTypeNode.resolveBinding().getQualifiedName());

		String variableName = variableNameNode.getIdentifier();
		
		ASTVariableDeclarationStatement variableDeclaration = block.createVariableDeclaration(variableName);

		variableDeclaration.setVariableType(variableType);
		
		variableDeclaration.setASTObject(variableFragment);

		
		// Inicialização da variável
		if (fragmentNodes.size() > 1) {
			
			// Expressão listeral
			if (fragmentNodes.get(1) instanceof org.eclipse.jdt.core.dom.PrefixExpression) {
				org.eclipse.jdt.core.dom.PrefixExpression astNode = (PrefixExpression) fragmentNodes.get(1);
				
				ASTLiteralExpression literalExpression = environment.createLiteralExpression(astNode.toString());
				
				variableDeclaration.setInitializationExpression(literalExpression);
				
			// Variável inicializada por método
			} else if (fragmentNodes.get(1) instanceof org.eclipse.jdt.core.dom.MethodInvocation) {
				org.eclipse.jdt.core.dom.MethodInvocation astNode = (MethodInvocation) fragmentNodes.get(1);
				
				String methodSignatureString = ASTEnvironment.getMethodSignature(astNode);

				MethodInvocationExpression mie = environment.createMethodInvocationExpression(methodSignatureString);
				
				variableDeclaration.setInitializationExpression(mie);
			}
		}
		
		
		return variableDeclaration;
	}

	private Statement parseReturnStatement(ReturnStatement node) {
		return null;
	}

	private ASTEnvironment getEnvironmentForBlock() {
		ASTMethod correspondingMethod = block.getParentMethod();

		return (ASTEnvironment) correspondingMethod.getParentType().getPackage().getEnvironment();
	}

}

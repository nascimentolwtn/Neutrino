package org.ita.testrefactoring.astparser;

import org.eclipse.jdt.core.dom.ASTNode;
import org.ita.testrefactoring.metacode.Block;
import org.ita.testrefactoring.metacode.Statement;

public class AbstractASTStatement<T extends ASTNode> implements Statement, ASTWrapper<T> {

	private Block parentBlock;
	private T astObject;
	
	@Override
	public Block getParent() {
		return parentBlock;
	}
	
	protected void setParent(Block block) {
		parentBlock = block;
	}

	@Override
	public void setASTObject(T astObject) {
		this.astObject = astObject;
		
	}

	@Override
	public T getASTObject() {
		return astObject;
	}

}

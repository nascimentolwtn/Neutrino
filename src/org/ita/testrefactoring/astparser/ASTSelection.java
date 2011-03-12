package org.ita.testrefactoring.astparser;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.ita.testrefactoring.abstracrefactoring.RefactoringUtils;
import org.ita.testrefactoring.codeparser.CodeElement;
import org.ita.testrefactoring.codeparser.CodeSelection;

public class ASTSelection implements CodeSelection {

	private int selectionStart;
	private int selectionLength;
	private ICompilationUnit sourceFile;
	private CodeElement selectedElement;

	@Override
	public int getSelectionStart() {
		return selectionStart;
	}

	@Override
	public void setSelectionStart(int i) {
		selectionStart = i;
	}

	@Override
	public int getSelectionLength() {
		return selectionLength;
	}

	@Override
	public void setSelectionLength(int i) {
		selectionLength = i;
	}

	@Override
	public Object getSourceFile() {
		return sourceFile;
	}

	@Override
	public void setSourceFile(Object sourceFile) {
		if (!(sourceFile instanceof ICompilationUnit)) {
			throw new Error("SourceFile must implement ICompilationUnit.");
		}
		
		this.sourceFile = (ICompilationUnit) sourceFile;
	}

	boolean isOverNode(ASTNode node) {
		return RefactoringUtils.isNodeOverSelection(node, selectionStart, selectionLength);
	}

	@Override
	public CodeElement getSelectedElement() {
		return selectedElement;
	}
	
	void setSelectedElement(CodeElement element) {
		selectedElement = element;
	}

}

package org.ita.neutrino.codeparser.astparser;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.ita.neutrino.codeparser.CodeElement;
import org.ita.neutrino.codeparser.CodeSelection;

public class ASTSelection implements CodeSelection {

	private int selectionStart;
	private int selectionLength;
	private ICompilationUnit sourceFile;
	private CodeElement selectedElement;

	// Construtor restrito ao pacote
	ASTSelection() {

	}

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
		CompilationUnit nodeCompilationUnit = getCompilationUnitForNode(node);

		// Verifica se a seleção está no arquivo ativo
		if (nodeCompilationUnit.getJavaElement().equals(sourceFile)) {
			if (isNodeOverSelection(node, selectionStart, selectionLength)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Retorna true se a seleção está contida no nó.
	 * 
	 * @param node
	 * @param selectionStart
	 * @param selectionLength
	 * @return
	 */
	private boolean isNodeOverSelection(ASTNode node, int selectionStart, int selectionLength) {
		int nodeEndPosition = node.getStartPosition() + node.getLength();
		int selectionEndPosition = selectionStart + selectionLength;

		return ((selectionStart >= node.getStartPosition())
				&& (selectionStart <= nodeEndPosition) && (selectionEndPosition <= nodeEndPosition));
	}
	

	private CompilationUnit getCompilationUnitForNode(ASTNode node) {

		while (node != null) {
			if (node.getNodeType() == ASTNode.COMPILATION_UNIT) {
				return (CompilationUnit) node;
			}
			
			node = node.getParent();
		};

		return null;
	}

	@Override
	public CodeElement getSelectedElement() {
		return selectedElement;
	}

	void setSelectedElement(CodeElement element) {
		selectedElement = element;
	}

}

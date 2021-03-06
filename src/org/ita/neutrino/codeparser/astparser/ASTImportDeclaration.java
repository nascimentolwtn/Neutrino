package org.ita.neutrino.codeparser.astparser;

import org.ita.neutrino.codeparser.AbstractCodeElement;
import org.ita.neutrino.codeparser.ImportDeclaration;
import org.ita.neutrino.codeparser.Type;
import org.ita.neutrino.codeparser.TypeListener;

public class ASTImportDeclaration extends AbstractCodeElement implements ImportDeclaration, ASTWrapper<org.eclipse.jdt.core.dom.ImportDeclaration> {

	private boolean isStatic;
	private boolean isPackage;
	private Type importedType;
	private TypeListener importedTypeListener = new ImportedTypeListener();
	private org.eclipse.jdt.core.dom.ImportDeclaration astObject;

	private class ImportedTypeListener implements TypeListener {

		@Override
		public void typePromoted(Type oldType, Type newType) {
			importedType = newType;
		}
	}

	// Construtor acessível somente através do pacote
	ASTImportDeclaration() {

	}

	protected void setType(Type type) {
		if (this.importedType != null) {
			this.importedType.removeListener(importedTypeListener);
		}

		this.importedType = type;

		if (this.importedType != null) {
			this.importedType.addListener(importedTypeListener);
		}
	}

	@Override
	public Type getType() {
		return importedType;
	}

	protected void setSourceFile(ASTSourceFile parent) {
		this.parent = parent;
	}

	@Override
	public ASTSourceFile getParent() {
		return (ASTSourceFile) super.getParent();
	}

	protected void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	@Override
	public boolean isStatic() {
		return isStatic;
	}

	/**
	 * Os dois métodos abaixo não estão sendo populados.
	 */
	@Override
	public org.eclipse.jdt.core.dom.ImportDeclaration getASTObject() {
		return astObject;
	}

	public void setASTObject(org.eclipse.jdt.core.dom.ImportDeclaration astObject) {
		this.astObject = astObject;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("import ");

		if (isStatic) {
			sb.append("static ");
		}

		sb.append(importedType.getQualifiedName() + ";");

		return sb.toString();
	}

	@Override
	public boolean isPackage() {
		return isPackage;
	}

	@Override
	public void setPackage(boolean isPack) {
		isPackage = isPack;
	}

}

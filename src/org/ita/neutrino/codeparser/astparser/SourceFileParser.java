package org.ita.neutrino.codeparser.astparser;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.ita.neutrino.codeparser.Type;

class SourceFileParser {

	/**
	 * Localiza as declarações de import e as salva.
	 * 
	 * @author Rafael Monico
	 * 
	 */
	private static class ImportVisitor extends ASTVisitor {

		private ASTSourceFile sourceFile;

		public void setSourceFile(ASTSourceFile sourceFile) {
			this.sourceFile = sourceFile;
		}

		@Override
		public boolean visit(ImportDeclaration node) {
			ASTImportDeclaration _import = sourceFile.createImportDeclaration();

			ASTEnvironment environment = sourceFile.getParent().getParent();

			// Nesse caso, node.getName() já devolve o nome qualificado do tipo
			// importado

			ITypeBinding typeBinding;

			if (node.resolveBinding() instanceof IMethodBinding) {
				IMethodBinding methodBinding = (IMethodBinding) node.resolveBinding();

				typeBinding = methodBinding.getDeclaringClass();
			} else if(node.resolveBinding() instanceof IPackageBinding) {
				typeBinding = null;
			} else {
				typeBinding = (ITypeBinding) node.resolveBinding();
			}

			Type type;
			if(typeBinding != null) {
				if (typeBinding.isClass()) {
					type = environment.getTypeCache().getOrCreateClass(node.getName().getFullyQualifiedName());
				} else if (typeBinding.isAnnotation()) {
					type = environment.getTypeCache().getOrCreateAnnotation(node.getName().getFullyQualifiedName());
				} else {
					type = environment.getTypeCache().get(node.getName());
				}
				_import.setType(type);
			}
			else _import.setType(null);

			_import.setASTObject(node);

			ASTSelection selection = environment.getSelection();

			if (selection.isOverNode(node)) {
				selection.setSelectedElement(_import);
			}

			// Nunca visita os nós filhos, isso será feito posteriormente
			return false;
		}

	}

	/**
	 * Localiza as declarações de tipo e as lança na lista.
	 * 
	 * @author Rafael Monico
	 * 
	 */
	private static class TypeVisitor extends ASTVisitor {

		private ASTSourceFile sourceFile;

		public void setSourceFile(ASTSourceFile sourceFile) {
			this.sourceFile = sourceFile;
		}

		@Override
		public boolean visit(TypeDeclaration node) {

			ASTType type = null;

			// Nesse caso, só pode ser classe
			if (!node.isInterface()) {
				type = classFound(node);
			} else {
				// É interface
				type = interfaceFound(node);
			}
			
			// TODO Popular as informações de tipagem genérica aqui
			// Verificar o que tem no node.typeDeclaration()

			// Se encontrou alguma coisa, verifica se está dentro da seleção
			if (type != null) {
				ASTSelection selection = sourceFile.getParent().getParent().getSelection();

				if (selection.isOverNode(node.getName())) {
					selection.setSelectedElement(type);
				}
			}

			return false;
		}

		private ASTClass classFound(TypeDeclaration node) {
			ASTClass clazz = sourceFile.createClass(node.getName().getIdentifier());

			clazz.setASTObject(node);

			return clazz;
		}

		private ASTInterface interfaceFound(TypeDeclaration node) {
			ASTInterface _interface = sourceFile.createInterface(node.getName().getIdentifier());

			_interface.setASTObject(node);

			return _interface;
		}

		@Override
		public boolean visit(AnnotationTypeDeclaration node) {
			annotationFound(node);
			return false;
		}

		private void annotationFound(AnnotationTypeDeclaration node) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean visit(EnumDeclaration node) {
			enumFound(node);
			return false;
		}

		private void enumFound(EnumDeclaration node) {
			// TODO Auto-generated method stub

		}

	}

	private ASTSourceFile sourceFile;

	public void setSourceFile(ASTSourceFile sourceFile) {
		this.sourceFile = sourceFile;
	}

	/**
	 * Faz o parsing do source file, baseado na compilation unit passada como
	 * parâmetro anteriormente.
	 */
	public void parse() {
		populateImportList();

		populateTypeList();
	}

	private void populateImportList() {
		ImportVisitor visitor = new ImportVisitor();

		visitor.setSourceFile(sourceFile);

		sourceFile.getASTObject().getCompilationUnit().accept(visitor);
	}

	private void populateTypeList() {
		TypeVisitor visitor = new TypeVisitor();

		visitor.setSourceFile(sourceFile);

		sourceFile.getASTObject().getCompilationUnit().accept(visitor);
	}

}

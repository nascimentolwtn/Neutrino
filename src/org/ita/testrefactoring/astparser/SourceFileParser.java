package org.ita.testrefactoring.astparser;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.ita.testrefactoring.metacode.Type;

class SourceFileParser {

	/**
	 * Localiza as declarações de import e as salva.
	 * @author Rafael Monico
	 *
	 */
	private static class ImportVisitor extends ASTVisitor {

		private ASTSourceFile sourceFile;

		public void setSourceFile(ASTSourceFile sourceFile) {
			this.sourceFile = sourceFile;
		}

		@Override
		public boolean visit(org.eclipse.jdt.core.dom.ImportDeclaration node) {
			ASTImportDeclaration _import = sourceFile.createImportDeclaration();

			String packageName = extractPackageName(node.getName().toString());
			String typeName = extractTypeName(node.getName().toString());
			
			ASTEnvironment environment = sourceFile.getPackage().getEnvironment();
			
			ASTPackage pack = environment.getPackageList().get(packageName);

			Type type = environment.getTypeCache().get(typeName);
				
			if (pack == null) {
				// Pacote não encontrado no cache, cria um pacote "dummy"
				pack = environment.createPackage(packageName);
			}
			
			if (type == null) {
				// Criar type "dummy"
				ASTDummyType dummy = environment.createDummyType(typeName, pack);
				
				type = dummy;
			}

			_import.setPackage(pack);
			_import.setType(type);

			// Nunca visita os nós filhos, isso será feito posteriormente
			return false;
		}

		private String extractTypeName(String packageName) {
			int endDot = packageName.lastIndexOf('.');

			return packageName.substring(endDot+1, packageName.length());
		}

		private String extractPackageName(String packageName) {
			int endDot = packageName.lastIndexOf('.');

			return packageName.substring(0, endDot);
		}
	}

	/**
	 * Localiza as declarações de tipo e as lança na lista.
	 * 
	 * @author Rafael Monico
	 *
	 */
	public class TypeVisitor extends ASTVisitor {

		private ASTSourceFile sourceFile;

		public void setSourceFile(ASTSourceFile sourceFile) {
			this.sourceFile = sourceFile;
		}
		
		@Override
		public boolean visit(TypeDeclaration node) {
			
			// Nesse caso, só pode ser classe
			if (!node.isInterface()) {
				classFound(node);
			} else {
				// É interface
				interfaceFound(node);
			}
			
			return false;
		}
		
		private void classFound(TypeDeclaration node) {
			ASTClass clazz = sourceFile.createClass(node.getName().getIdentifier());
			
			clazz.setASTObject(node);
		}
		
		private void interfaceFound(TypeDeclaration node) {
			sourceFile.createInterface(node.getName().getIdentifier());
			
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
	 * Faz o parsing do source file, baseado na compilation unit passada como parâmetro anteriormente.
	 */
	public void parse() {
		sourceFile.setFileName(sourceFile.getASTObject().getICompilationUnit()
				.getPath().toFile().getName());

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

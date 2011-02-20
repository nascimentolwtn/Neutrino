package org.ita.testrefactoring.astparser;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.ita.testrefactoring.metacode.Class;
import org.ita.testrefactoring.metacode.ParserException;
import org.ita.testrefactoring.metacode.Type;
import org.zero.utils.StringUtils;

class ClassParser implements ASTTypeParser<ASTClass> {

	private class ClassVisitor extends ASTVisitor {

		private ASTClass clazz;

		public void setClass(ASTClass clazz) {
			this.clazz = clazz;
		}
		
		@Override
		public boolean visit(FieldDeclaration node) {
			ASTField field = clazz.createField(node.toString());
			
			// TODO: field.setInitialization
			field.setParentType(clazz);

			String fieldTypeQualifiedName = node.getType().resolveBinding().getQualifiedName();

			ASTEnvironment environment = clazz.getPackage().getEnvironment();
			Type fieldType = environment.getTypeCache().get(fieldTypeQualifiedName);

			if (fieldType == null) {
				String packageName = StringUtils.extractPackageName(fieldTypeQualifiedName);
				
				ASTPackage pack = environment.getPackageList().get(packageName);
				
				if (pack == null) {
					pack = environment.createPackage(packageName);
				}
				
				String typeName = StringUtils.extractTypeName(fieldTypeQualifiedName);
					
				fieldType = environment.createDummyType(typeName, pack);
			}

			field.setFieldType(fieldType);
			
			return false;
		}

		@Override
		public boolean visit(MethodDeclaration node) {
			return false;
		}
	}

	private ASTClass clazz;

	@Override
	public void setType(ASTClass type) {
		this.clazz = type;
	}

	@Override
	public void parse() throws ParserException {
		ClassVisitor visitor = new ClassVisitor();

		visitor.setClass(clazz);

		String superClassName;

		org.eclipse.jdt.core.dom.Type superclassNode = clazz.getASTObject().getSuperclassType();
		
		// TODO: Popular a lista de interfaces implementadas
		//clazz.getASTObject().superInterfaceTypes()

		if (superclassNode == null) {
			superClassName = "java.lang.Object";
		} else {
			superClassName = superclassNode.resolveBinding().getQualifiedName();
		}

		ASTEnvironment environment = clazz.getPackage().getEnvironment();
		Type superClass = environment.getTypeCache().get(superClassName);

		if (superClass == null) {
			superClass = environment.createDummyClass(superClassName);
		} else if (superClass.getKind() == TypeKind.UNKNOWN) {
			// Se antes não era possível saber qual o Kind do tipo, agora sei
			// que se trata de uma classe
			DummyClass dummyClass = environment.createDummyClass(superClassName);

			superClass.promote(dummyClass);

			superClass = dummyClass;
		} else if (superClass.getKind() != TypeKind.CLASS) {
			throw new ParserException("Super classe de \"" + clazz.getQualifiedName() + "\" inválida (\"" + superClass.getQualifiedName() + "\")");
		}

		// Aqui superClass deve ser uma classe, já que getKind devolveu CLASS...
		clazz.setSuperClass((Class) superClass);

		// TODO: Popular os modificadores da classe
		clazz.getASTObject().accept(visitor);
	}

}

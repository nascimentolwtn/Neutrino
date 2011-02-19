package org.ita.testrefactoring.astparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.ita.testrefactoring.metacode.Method;
import org.ita.testrefactoring.metacode.SourceFile;
import org.ita.testrefactoring.metacode.Type;
import org.ita.testrefactoring.metacode.TypeAccessModifier;
import org.ita.testrefactoring.metacode.TypeListener;

public abstract class ASTType implements Type, ASTWrapper<TypeDeclaration> {

	private SourceFile parent;
	private ASTPackage pack;
	private String name;
	private TypeAccessModifier accessModifier = new TypeAccessModifier();
	private Map<String, ASTField> fieldList = new HashMap<String, ASTField>();
	private Map<String, Method> methodList = new HashMap<String, Method>();
	private TypeDeclaration astObject;
	private List<TypeListener> listeners = new ArrayList<TypeListener>();

	@Override
	public SourceFile getSourceFile() {
		return parent;
	}

	protected void setParent(SourceFile parent) {
		this.parent = parent;
	}

	@Override
	public ASTPackage getPackage() {
		return pack;
	}

	protected void setPackage(ASTPackage pack) {
		this.pack = pack;
	}

	@Override
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Override
	public TypeAccessModifier getAccessModifier() {
		return accessModifier;
	}

	public String getQualifiedName() {
		return getPackage().getName() + "." + getName();
	}

	@Override
	public Map<String, ASTField> getFieldList() {
		return fieldList;
	}

	@Override
	public Map<String, Method> getMethodList() {
		return methodList;
	}

	@Override
	public TypeDeclaration getASTObject() {
		return astObject;
	}

	@Override
	public void setASTObject(TypeDeclaration astObject) {
		this.astObject = astObject;
	}
	
	@Override
	/**
	 * Apenas notifica os listeners sobre a promoção.
	 */
	public void promote(Type newType) {
		for (TypeListener listener : listeners ) {
			listener.typePromoted(this, newType);
		}
	}

	@Override
	public void addListener(TypeListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(TypeListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(getQualifiedName() + ":\n");
		sb.append("\n");
		sb.append("Kind: " + getKind() + "\n");
		sb.append("instanceof: " + getClass() + "\n");
		sb.append("File: " + parent.getFileName() + "\n");
		sb.append("Package: " + pack.getName() + "\n");
		sb.append("Access: " + accessModifier.toString() + "\n");

		if (astObject != null) {
			int firstLineBreak = astObject.toString().indexOf('\n');

			if (firstLineBreak == -1) {
				firstLineBreak = astObject.toString().length();
			}

			String astHeader = astObject.toString().substring(0, firstLineBreak);

			sb.append("AST: <" + astHeader + ">\n");
		}
		
		sb.append("\n");
		sb.append("\n");
		sb.append("Field list:\n");

		for (String key : fieldList.keySet()) {
			ASTField field = fieldList.get(key);

			sb.append(key + " --> " + field.getFieldType().getQualifiedName() + " " + field.getName() + ";\n");
		}

		sb.append("\n");
		sb.append("\n");
		sb.append("Method list:\n");

		for (String key : methodList.keySet()) {
			Method method = methodList.get(key);

			sb.append(key + " --> " + method.getName() + "();\n");
		}

		return sb.toString();
	}
}

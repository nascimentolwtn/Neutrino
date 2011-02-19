package org.ita.testrefactoring.metacode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ita.testrefactoring.astparser.TypeKind;



/*
 * Representa um tipo de dados onde o código fonte não está disponível.
 * 
 * @author Rafael Monico
 * 
 */
public class DummyType implements Type {

	private SourceFile sourceFile;
	private Package pack;
	private String name;
	private TypeAccessModifier accessModifier = new TypeAccessModifier();
	private Map<String, ? extends Field> fieldList = new HashMap<String, Field>();
	private Map<String, Method> methodList = new HashMap<String, Method>();
	private List<TypeListener> listeners = new ArrayList<TypeListener>();
	
	@Override
	public SourceFile getSourceFile() {
		return sourceFile;
	}
	
	protected void setSourceFile(SourceFile sourceFile) {
		this.sourceFile = sourceFile;
	}

	@Override
	public Package getPackage() {
		return pack;
	}
	
	protected void setPackage(Package pack) {
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

	@Override
	public Map<String, ? extends Field> getFieldList() {
		return fieldList;
	}

	@Override
	public Map<String, Method> getMethodList() {
		return methodList;
	}

	@Override
	public TypeKind getKind() {
		return TypeKind.UNKNOWN;
	}

	@Override
	public String getQualifiedName() {
		return getPackage().getName() + "." + getName();
	}
	
	
	@Override
	public void promote(Type newType) {
		for (TypeListener listener : listeners) {
			listener.typePromoted(this, newType);
		}
	}
	
	@Override
	public void addListener(TypeListener listener) {
		listeners .add(listener);
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
		sb.append("Package: " + pack.getName() + "\n");
		sb.append("Access: " + accessModifier.toString() + "\n");
		
		sb.append("\n");
		sb.append("\n");
		sb.append("Field list:\n");
		
		for (String key : fieldList.keySet()) {
			Field field = fieldList.get(key);
			
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

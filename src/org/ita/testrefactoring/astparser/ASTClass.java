package org.ita.testrefactoring.astparser;

import org.ita.testrefactoring.metacode.Class;
import org.ita.testrefactoring.metacode.NonAccessClassModifier;
import org.ita.testrefactoring.metacode.Type;
import org.ita.testrefactoring.metacode.TypeListener;

public class ASTClass extends ASTType implements Class {
	
	// Vai dar problema depois, pois NonAccessClassModifier é read-only
	private NonAccessClassModifier nonAccessModifier = new NonAccessClassModifier();
	private ParentListener parentListener = new ParentListener();
	private Class parent;
	
	
	private class ParentListener implements TypeListener {

		@Override
		public void typePromoted(Type oldType, Type newType) {
			parent = (Class) newType;
		}
		
	}

	@Override
	public NonAccessClassModifier getNonAccessModifier() {
		return nonAccessModifier;
	}

	@Override
	public Class getSuperClass() {
		return parent;
	}
	
	protected void setParent(Class parent) {
		if (this.parent != null) {
			this.parent.removeListener(parentListener);
		}
		
		this.parent = parent;
		
		if (this.parent != null) {
			this.parent.addListener(parentListener);
		}
	}

	@Override
	public TypeKind getKind() {
		return TypeKind.CLASS;
	}

	ASTField createField(String name) {
		ASTField field = new ASTField();
		
		field.setName(name);
		field.setParentType(this);
		
		getFieldList().put(name, field);
		
		return field;
	}

}

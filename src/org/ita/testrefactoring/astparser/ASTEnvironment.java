package org.ita.testrefactoring.astparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.ita.testrefactoring.metacode.DummyClass;
import org.ita.testrefactoring.metacode.Environment;
import org.ita.testrefactoring.metacode.Type;
import org.ita.testrefactoring.metacode.Package;


public class ASTEnvironment implements Environment, ASTWrapper<List<ICompilationUnit>> {
	
	private Map<String, Package> packageList = new HashMap<String, Package>();
	private Map<String, Type> typeCache = new HashMap<String, Type>();
	private List<ICompilationUnit> astObject;
	
	// Construtor restrito ao pacote
	ASTEnvironment() {
		
	}

	@Override
	public Map<String, Package> getPackageList() {
		return packageList;
	}

	/**
	 * Preciso do nome do pacote de antemão pois coloco todos os pacotes no Map
	 * @param packageName
	 * @return
	 */
	ASTPackage createPackage(String packageName) {
		ASTPackage _package = new ASTPackage();
		_package.setEnvironment(this);
		_package.setName(packageName);
		
		packageList.put(packageName, _package);
		
		return _package;
	}

	@Override
	public Map<String, Type> getTypeCache() {
		return typeCache;
	}

	@Override
	public void setASTObject(List<ICompilationUnit> astObject) {
		this.astObject = astObject;
	}

	@Override
	public List<ICompilationUnit> getASTObject() {
		return astObject;
	}

	DummyType createDummyType(String typeName, Package pack) {
		DummyType dummy = new DummyType();

		dummy.setName(typeName);
		dummy.setPackage(pack);

		registerType(dummy);

		return dummy;
	}

	void registerType(Type type) {
		getTypeCache().put(type.getQualifiedName(), type);
	}

	public Type createDummyClass(String qualifiedName) {
		DummyClass dummy = new DummyClass();
		
		String packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf('.')-1);
		String className = qualifiedName.substring(qualifiedName.lastIndexOf('.')+1, qualifiedName.length());

//		Package pack = getPackageList().get(packageName);

//		dummy.setName(typeName);
//		dummy.setPackage(pack);

		registerType(dummy);

		return dummy;
	}


}

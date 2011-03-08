package org.ita.testrefactoring.abstracttestparser;

import java.util.List;

public abstract class TestBattery implements TestElement {

	public abstract List<? extends TestSuite> getSuiteList();
	
	public abstract TestSuite getSuiteByName();
	
	@Override
	public TestElement getParent() {
		// A bateria não possui parent
		return null;
	}
}

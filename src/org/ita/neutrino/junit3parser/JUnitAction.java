package org.ita.neutrino.junit3parser;

import org.ita.neutrino.abstracttestparser.Action;
import org.ita.neutrino.codeparser.Statement;

public class JUnitAction extends JUnitTestStatement implements Action {

	private Statement element;

	JUnitAction() {
		
	}

	@Override
	public Statement getCodeElement() {
		return element;
	}
	
	void setCodeElement(Statement element) {
		this.element = element;
	}
}
package org.ita.testrefactoring.codeparser;

public abstract class AbstractCodeParser {
	private Environment environment;

	public abstract void parse() throws ParserException;
	
	protected void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	public Environment getEnvironment() {
		return environment;
	}

	public abstract CodeSelection getSelection();
}

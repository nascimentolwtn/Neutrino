package org.ita.neutrino.tests;

import static org.zero.utils.JUnitUtils.assertBlockEquals;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.ita.neutrino.abstracrefactoring.AbstractRefactoring;
import org.ita.neutrino.abstracrefactoring.RefactoringException;
import org.ita.neutrino.abstracttestparser.TestBattery;
import org.ita.neutrino.abstracttestparser.TestParserException;
import org.ita.neutrino.codeparser.ParserException;
import org.ita.neutrino.junitgenericparser.JUnitGenericParser;
import org.junit.Test;

public abstract class RefactoringAbstractTests extends PluginAbstractTests {
	
	protected ICompilationUnit refactoredCompilationUnit;
	protected JUnitGenericParser testParser;
	protected AbstractRefactoring refactoring;

	protected abstract void prepareTests() throws JavaModelException, ParserException, TestParserException;
	protected abstract StringBuilder getExpectedSource();
	protected abstract AbstractRefactoring instantiateRefactoring();
	protected void setupRefactoring() {}
	protected abstract String getRefactoringName();
	
	@Test
	public void testRefactoring() throws JavaModelException, RefactoringException, TestParserException, ParserException {

		prepareTests();

		// Aplica a refatoração na bateria de testes
		TestBattery battery = testParser.getBattery();

		
		refactoring = instantiateRefactoring();

		refactoring.setBattery(battery);

		setupRefactoring();

		// Define em que arquivo fonte e local será feita a refatoração
		refactoring.setTargetFragment(battery.getSelection().getSelectedFragment());

		refactoring.refactor();

		// Verificação
		String afterRefactoringSource = refactoredCompilationUnit.getSource();

		StringBuilder expectedSource = getExpectedSource();

		assertBlockEquals(getRefactoringName(), expectedSource.toString(), afterRefactoringSource);
	}



}
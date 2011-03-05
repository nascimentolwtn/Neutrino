package org.ita.testrefactoring.junitparser;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.ita.testrefactoring.abstracttestparser.AbstractTestParser;
import org.ita.testrefactoring.abstracttestparser.TestParserException;
import org.ita.testrefactoring.codeparser.AbstractCodeParser;
import org.ita.testrefactoring.codeparser.ParserException;

public class JUnitParser extends AbstractTestParser {

	private List<ICompilationUnit> compilationUnits;
	private JUnitSelection selection;
	private ICompilationUnit activeCompilationUnit;
	private AbstractCodeParser codeParser;

	public JUnitParser(AbstractCodeParser parser) {
		codeParser = parser;
		selection = new JUnitSelection(codeParser.getSelection());
	}

	@Override
	protected JUnitTestBattery createTestBattery() {
		JUnitTestBattery battery = new JUnitTestBattery();
		
		battery.setParser(this);
		
		return battery;
	}

	@Override
	public JUnitTestBattery getBattery() {
		return (JUnitTestBattery) super.getBattery();
	}

	@Override
	public void parse() throws TestParserException {
		try {
			codeParser.parse();
		} catch (ParserException e) {
			throw new TestParserException(e);
		}
		
		if (compilationUnits.isEmpty()) {
			return;
		}

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(activeCompilationUnit); // Fonte para ser parseado,
													// considero o primeiro
													// arquivo
													// da lista como sendo o
													// principal
		parser.setResolveBindings(true);

		// Projeto java que será usado para resolver os bindings
		parser.setProject(activeCompilationUnit.getJavaProject());

		// final IProgressMonitor monitor = new ProgressMonitorPart(new
		// Composite(null, -1), null);
		
		getBattery().parse(parser, compilationUnits, activeCompilationUnit);
		
		getSelection().setSelectedFragment(getBattery().getSelectedFragment());
	}

	public void setCompilationUnits(List<ICompilationUnit> compilationUnits) {
		this.compilationUnits = compilationUnits;
	}
	
	public void setActiveCompilationUnit(ICompilationUnit activeCompilationUnit) {
		this.activeCompilationUnit = activeCompilationUnit;
	}

	@Override
	public JUnitSelection getSelection() {
		return selection;
	}

}

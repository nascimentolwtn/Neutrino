package org.ita.neutrino.testsmells.smells;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.ita.neutrino.abstracttestparser.Assertion;
import org.ita.neutrino.codeparser.Expression;
import org.ita.neutrino.decomposeassertion.DecomposeAssertionAction;
import org.ita.neutrino.testsmells.core.MarkerManager;

@NeutrinoRefactoringForEclipse(description = "Splits this assertion into multiple ones",
		title="Decompose assertion",
		value=DecomposeAssertionAction.class)
public class CompositeAssertionSmell implements TestCodeSmell<Assertion> {
	@Override
	public void checkForPresence(Assertion assertion, MarkerManager markerManager)
			throws JavaModelException, CoreException {
		for (Expression expression : assertion.getCodeElement().getParameterList()) {
			if (expression.isLogicalExpression()) {
				markerManager.addMarker(expression, 
						"composite assertion", this.getClass());
			}
		}
	}
}
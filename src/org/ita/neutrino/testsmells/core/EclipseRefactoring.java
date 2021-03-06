package org.ita.neutrino.testsmells.core;

import org.eclipse.jface.viewers.ISelection;
import org.ita.neutrino.eclipseaction.ActionException;

public interface EclipseRefactoring {
	String title();
	String description();
	void run(ISelection ensureMarkerSelected) throws ActionException;
}

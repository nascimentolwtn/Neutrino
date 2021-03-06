package org.ita.neutrino.testsmells.core;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.ita.neutrino.codeparser.CodeElement;
import org.ita.neutrino.codeparser.Method;
import org.ita.neutrino.codeparser.Statement;
import org.ita.neutrino.codeparser.astparser.ASTClass;
import org.ita.neutrino.codeparser.astparser.ASTMethod;
import org.ita.neutrino.codeparser.astparser.ASTType;
import org.ita.neutrino.codeparser.astparser.ASTWrapper;
import org.ita.neutrino.testsmells.smells.TestCodeSmell;
import org.ita.neutrino.tparsers.abstracttestparser.TestSuite;

import com.google.common.collect.Lists;

public class EclipseMarkerManager implements MarkerManager {
	private final String MARKER_TYPE = "org.ita.neutrino.testsmells.marker";
	private final String MARKER_SOURCE = "org.ita.neutrino";
	
	@Override
	public void clearMarkers(TestSuite suite) throws JavaModelException, CoreException {
		CompilationUnit compilationUnit = getCompilationUnit(((ASTType) suite.getCodeElement()).getASTObject());
		compilationUnit.getJavaElement().getUnderlyingResource().deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
	}

	@Override
	public void addMarker(List<CodeElement> range, String markerText, Class<? extends TestCodeSmell<?>> smellType) throws CoreException {
		ASTNode rangeStart = astNodeFromCodeElement(range.get(0));
		ASTNode rangeEnd = astNodeFromCodeElement(range.get(range.size() - 1));
		
		CompilationUnit compilationUnit = getCompilationUnit(rangeStart);
		int lineNumber = compilationUnit.getLineNumber(rangeStart.getStartPosition());		
		IMarker marker = compilationUnit.getJavaElement().getUnderlyingResource().createMarker(MARKER_TYPE);
	
		marker.setAttribute("smellType", smellType.getCanonicalName());
		marker.setAttribute(IMarker.CHAR_START, rangeStart.getStartPosition());
		marker.setAttribute(IMarker.CHAR_END, rangeEnd.getStartPosition() + rangeEnd.getLength());
		marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		marker.setAttribute(IMarker.SOURCE_ID, MARKER_SOURCE);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		marker.setAttribute(IMarker.MESSAGE, markerText);
	}
	
	@Override
	public void addMarker(CodeElement codeElement, String markerText, Class<? extends TestCodeSmell<?>> smellType) throws JavaModelException, CoreException {
		addMarker(Lists.newArrayList(codeElement), markerText, smellType);
	}
	
	@SuppressWarnings("unchecked")
	private ASTNode astNodeFromCodeElement(CodeElement element) {
		if (element instanceof org.ita.neutrino.codeparser.Class) {
			return ((ASTClass)element).getASTObject().getName();
		} else if (element instanceof Method) {
			return ((ASTMethod)element).getASTObject().getName();
		} else if (element instanceof Statement && ((Statement) element).isBranchStatement()) {
			ASTNode node = ((ASTWrapper<? extends ASTNode>)element).getASTObject();
			if (node.getNodeType() == ASTNode.IF_STATEMENT) {
				return ((IfStatement) node).getExpression();
			} else if (node.getNodeType() == ASTNode.SWITCH_STATEMENT) {
				return ((SwitchStatement) node).getExpression();
			} else {
				return node;
			}
		} else {
			return ((ASTWrapper<? extends ASTNode>)element).getASTObject();
		}
	}
	
	private CompilationUnit getCompilationUnit(ASTNode node) {
		while (node.getNodeType() != ASTNode.COMPILATION_UNIT) {
			node = node.getParent();
		}
		
		return (CompilationUnit) node;
	}
}

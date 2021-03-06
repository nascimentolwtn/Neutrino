package org.ita.neutrino.tparsers.junit3parser;

import java.util.ArrayList;
import java.util.List;

import org.ita.neutrino.codeparser.MutableMethod;
import org.ita.neutrino.tparsers.abstracttestparser.TestMethod;

public class JUnitTestSuite extends org.ita.neutrino.tparsers.junitgenericparser.JUnitTestSuite {

	protected JUnitTestSuite() {
		super();
	}

	@Override
	protected JUnitTestMethod instantiateTestMethod() {
		return new JUnitTestMethod();
	}

	@Override
	protected List<JUnitTestMethod> instantiateMethodList() {
		return new ArrayList<JUnitTestMethod>();
	}

	protected JUnitFixture instantiateFixture() {
		return new JUnitFixture();
	};

	@Override
	protected List<JUnitFixture> instantiateFixtureList() {
		return new ArrayList<JUnitFixture>();
	}

	/**
	 * Devolve o método executado antes dos testes. Não há setter correspondente pois o createBeforeMethod já faz isso.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<JUnitTestMethod> getBeforeMethodList() {
		return (List<JUnitTestMethod>) super.getBeforeMethodList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JUnitTestMethod> getTestMethodList() {
		return (List<JUnitTestMethod>) super.getTestMethodList();
	}

	/**
	 * Devolve o método executado após os testes. Não há setter correspondente pois o createAfterMethod já faz isso.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<JUnitTestMethod> getAfterMethodList() {
		return (List<JUnitTestMethod>) super.getAfterMethodList();
	}

	@Override
	public JUnitTestBattery getParent() {
		return (JUnitTestBattery) super.getParent();
	}

	protected void setParent(JUnitTestBattery parent) {
		super.setParent(parent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JUnitFixture> getFixtures() {
		return (List<JUnitFixture>) super.getFixtures();
	}

	@Override
	public JUnitTestMethod getMethodByName(String methodName) {
		return (JUnitTestMethod) super.getMethodByName(methodName);
	}

	@Override
	public TestMethod createNewBeforeTestsMethod() {
		MutableMethod newMethod = getCodeElement().createNewMethod("setUp", 0);

		return parseBeforeMethod(newMethod);
	}

	@Override
	public TestMethod createNewAfterTestsMethod() {
		MutableMethod newMethod = getCodeElement().createNewMethod("tearDown", -1);

		return parseAfterMethod(newMethod);
	}

	@Override
	public TestMethod createNewTestMethod(String newMethodName) {
		MutableMethod newMethod = getCodeElement().createNewMethod(newMethodName, -1);

		return parseTestMethod(newMethod);
	}
}

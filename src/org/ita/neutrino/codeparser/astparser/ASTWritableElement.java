package org.ita.neutrino.codeparser.astparser;

/**
 * Indica que o elemento do AST possui propriedades alteráveis.
 * 
 * @author Rafael Monico
 *
 */
interface ASTWritableElement {

	/**
	 * Informa que o parse foi finalizado.
	 */
	public void parseFinished(); 
}

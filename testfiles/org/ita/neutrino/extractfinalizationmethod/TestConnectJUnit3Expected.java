package org.ita.neutrino.extractfinalizationmethod;

import junit.framework.TestCase;

public class TestConnectJUnit3Expected extends TestCase {

	Connect connect;

	public void testConecta() {
		connect = new Connect();
		connect.setPorta(8080);
		connect.setIP("127.0.0.1");

		connect.estabelecerConexao();
		assertTrue("Conexão Estabelecida", connect.isConectado());
		assertFalse("Esperando Conexão", connect.isListen());
	}

	public void testListen() {
		connect = new Connect();
		connect.setPorta(8080);
		connect.setIP("127.0.0.1");

		connect.escutarConexao();
		assertFalse("Conexão Estabelecida", connect.isConectado());
		assertTrue("Esperando Conexão", connect.isListen());
	}
	
	public void teardown() {
		connect.fecharConexao();
	}

}

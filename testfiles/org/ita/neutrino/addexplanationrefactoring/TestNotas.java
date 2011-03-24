package org.ita.neutrino.addexplanationrefactoring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestNotas {
    
    @SuppressWarnings("unused")
    @Test
    private void testNotasTurma() {
        Notas not = new Notas();

        not.add("João", 8.0);
        not.add("Pedro", 7.0);
        not.add("Maria", 9.0);

        assertEquals(not.avg(), 8.0, 0);
    }
}

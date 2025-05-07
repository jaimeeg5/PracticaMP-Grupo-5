package characters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinionTest {

    // Prueba si el minion sufre daño cuando se llama a takeDamage
    @Test
    void takeDamage() {
        Minion minion = new Minion();
        minion.setHealth(10);
        assertTrue(minion.takeDamage());
        assertEquals(9, minion.getHealth());
        minion.setHealth(0);
        assertTrue(minion.takeDamage());
        assertEquals(-1, minion.getHealth());
    }

    // Prueba si al "pasar" el objeto minion a tipo String devuelve el nombre
    @Test
    void testToString() {
        Minion minion = new Minion();
        minion.setName("Minion1");
        assertEquals("Minion1", minion.toString());
    }
}
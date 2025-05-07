package characters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialAbilityTest {

    // Prueba si al "pasar" el objeto SpecialAbility a tipo String devuelve el nombre
    @Test
    void testToString() {
        SpecialAbility specialAbility = new SpecialAbility("SpecialAbility1", 5, 5);
        assertEquals("SpecialAbility1", specialAbility.toString());
    }
}
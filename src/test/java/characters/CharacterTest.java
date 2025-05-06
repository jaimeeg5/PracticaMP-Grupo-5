package characters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    // Probar que si se introduce salud > 5, lo establece automáticamente a 5
    @Test
    void getHealth() {
        Character character = new Vampire();
        character.setHealth(10);
        assertSame(5, character.getHealth());
    }

    // Probar que si se introduce poder > 5, lo establece automáticamente a 5
    @Test
    void getPower() {
        Character character = new Vampire();
        character.setPower(10);
        assertSame(5, character.getPower());
    }

    // Probar que si se introduce oro < 0, lo establece automáticamente a 0
    @Test
    void getGold() {
        Character character = new Vampire();
        character.setGold(-10);
        assertSame(0, character.getGold());
    }

    // Probar que el valor de ataque no puede superar el poder máximo
    @Test
    void calculatePower() {
        Character character = new Vampire();
        int power = character.calculatePower(25);
        assertTrue(power <= 25);
    }

    // Prueba si el personaje sufre daño cuando se llama a takeDamage y no tiene esbirros, y su defensa
    // es menor que el ataque del rival
    @Test
    void takeDamage() {
        Character character = new Vampire();
        character.setHealth(5);
        assertSame(true, character.takeDamage(10, 5));
        assertSame(4, character.getHealth());
    }
}
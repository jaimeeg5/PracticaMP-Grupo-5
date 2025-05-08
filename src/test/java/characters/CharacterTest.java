package characters;

import game.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    @BeforeEach
    void setUp() {
        FileManager.setup();
    }

    // Probar que si se introduce salud > 5, lo establece automáticamente a 5
    @Test
    void getHealth() {
        Character character = new Vampire();
        character.setHealth(10);
        assertEquals(5, character.getHealth());
        character.setHealth(-2);
        assertEquals(1, character.getHealth());
    }


    // Probar que si se introduce poder > 5, lo establece automáticamente a 5
    @Test
    void getPower() {
        Character character = new Vampire();
        character.setPower(9);
        assertEquals(5, character.getPower());
        character.setPower(-3);
        assertEquals(1, character.getPower());
        character.setPower(3);
        assertEquals(3, character.getPower());
    }

    // Probar que si se introduce oro < 0, lo establece automáticamente a 0
    @Test
    void getGold() {
        Character character = new Vampire();
        character.setGold(-10);
        assertEquals(0, character.getGold());
        character.setGold(2345);
        assertEquals(2345, character.getGold());
    }

    // Probar que el valor de ataque no puede superar el poder máximo
    @Test
    void calculatePower() {
        Character character = new Vampire();
        int power = character.calculatePower(25);
        assertTrue(power <= 25);
        int  power2 = character.calculatePower(0);
        assertEquals(0, power2);
    }

    // Prueba si el personaje sufre daño cuando se llama a takeDamage y no tiene esbirros, y su defensa
    // es menor que el ataque del rival
    @Test
    void takeDamage() {
        Character character = new Vampire();
        character.setHealth(5);
        assertTrue(character.takeDamage(10, 5));
        assertEquals(4, character.getHealth());
        character.setHealth(5);
        assertTrue(character.takeDamage(5, 5));
        assertEquals(4, character.getHealth());
        character.setHealth(5);
        assertFalse(character.takeDamage(4, 5));
        assertEquals(5, character.getHealth());
    }
}
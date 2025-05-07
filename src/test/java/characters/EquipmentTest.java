package characters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentTest {

    // Probar que al "pasar" el objeto arma a un string devuelve el nombre
    @Test
    void testToString() {
        Equipment equipment = new Equipment("Equipment1", 5, 5, EquipmentType.ARMOR);
        assertEquals("Equipment1", equipment.toString());
    }
}
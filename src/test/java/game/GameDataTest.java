package game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class GameDataTest {

    @BeforeEach
    void setUp() {
        FileManager.setup();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInstance() {
        GameData d1 = GameData.getInstance();
        GameData d2 = GameData.getInstance();
        assertEquals(d1, d2);
    }

    @Test
    void banUser() {
        GameData gameData = GameData.getInstance();
        Player player = new Player();
        player.setNick("Player1");
        gameData.banUser("Player1");
        assertTrue(gameData.isBanned(player.getNick()));
    }

    @Test
    void unbanUser() {
        GameData gameData = GameData.getInstance();
        Player player = new Player();
        player.setNick("Player1");
        gameData.banUser("Player1");
        gameData.unbanUser("Player1");
        assertFalse(gameData.isBanned(player.getNick()));
    }
}
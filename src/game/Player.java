package game;

import java.nio.file.Path;
import characters.Character;

public class Player extends User {
    private String registerNumber;
    private int goldWon;
    private int goldLost;
    private Character character;

    public Player(String nick, String name, String registerNumber) {
        super(nick, name);
        this.registerNumber = registerNumber;
    }

    @Override
    public void operate(){

    }

    @Override
    public void update(Path file){

    }

    public void challengeUser(User player){

    }

    public void pay(Player player, int amount){
        player.goldWon += amount;
        this.goldLost -= amount;
    }

    public void registerCharacter(Character character){

    }

    public void dropoutCharacter(){

    }

    public Character getCharacter() {
        return character;
    }
}

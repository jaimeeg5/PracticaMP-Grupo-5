package game;

import java.nio.file.Path;

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

    private void challengeUser(User player){

    }

    private void pay(Player player, int amount){

    }

    public void registerCharacter(Character character){

    }

    public void dropoutCharacter(){

    }
}

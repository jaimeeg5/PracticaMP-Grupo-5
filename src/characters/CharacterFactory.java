package characters;

public class CharacterFactory {
    public Character registerCharacter(CharacterType type){
        if (type == Vampire)
            return new Vampire();
        else if (type == Werewolf)
            return new Werewolf();
        else if (type == Hunter)
            return new Hunter();
        else
            return null;
    }
}

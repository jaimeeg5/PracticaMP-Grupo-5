package characters;

public class CharacterFactory {
    public Character registerCharacter(CharacterType type){
        if (type == CharacterType.Vampire)
            return new Vampire();
        else if (type == CharacterType.Werewolf)
            return new Werewolf();
        else if (type == CharacterType.Hunter)
            return new Hunter();
        else
            return null;
    }
}

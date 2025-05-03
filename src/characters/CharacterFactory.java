package characters;

public class CharacterFactory {

    public Character registerCharacter(CharacterType type){
        if (type == CharacterType.Vampire)
            return new Vampire(150, type);
        else if (type == CharacterType.Werewolf)
            return new Werewolf(1.90, 100, type);
        else if (type == CharacterType.Hunter)
            return new Hunter(type);
        else
            return null;
    }
}

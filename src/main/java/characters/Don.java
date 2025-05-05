package characters;

public class Don extends SpecialAbility{
    private int rageValue;

    public Don(String name, int attack, int defense) {
        super(name, attack, defense);
    }

    public int getRageValue() {
        return rageValue;
    }


}

package characters;

public abstract class SpecialAbility {
    private String name;
    private int attack;
    private int defense;

    public SpecialAbility(String name, int attack, int defense){
        this.name = name;
        this.attack = attack;
        this.defense = defense;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }
}

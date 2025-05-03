package characters;

public class Discipline extends SpecialAbility{
    private int bloodCost;

    public Discipline(String name, int attack, int defense) {
        super(name, attack, defense);
    }

    public int getBloodCost() {
        return bloodCost;
    }
}

package equipments;

public class Equipment {
    private String name;
    private int attackValue;
    private int defenseValue;
    private EquipmentType type;

    public Equipment(String name, int attack, int defense, EquipmentType type) implements Jsonable {
        this.name = name;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.type = type;
    }

    public EquipmentType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }
}

package equipments;

public class EquipmentBuilder {
    EquipmentType type;
    String name;
    int attack = -1;
    int defense = -1;

    public void reset(){
        this.type = null;
        this.name = null;
        this.attack = -1;
        this.defense = -1;
    }

    public void buildName(String name){
        this.name = name;
    }

    public void buildAttack(int attackValue){
        this.attack = attackValue;
    }

    public void buildDefense(int defenseValue){
        this.defense = defenseValue;
    }

    public EquipmentBuilder(EquipmentType type) {
        this.type = type;
    }

    public Equipment build() {
        if ((name == null) || (attack == -1) || (defense == -1)) {
            throw new RuntimeException("Deben establecerse el nombre, ataque y defensa del equipamiento");
        }
        if (type == EquipmentType.ARMOR) {
            return new Armor(name, attack, defense);
        }
        else if(type == EquipmentType.ONEHANDEDWEAPON){
            return new OneHandedWeapon(name, attack, defense);
        }
        else{
           return new TwoHandedWeapon(name, attack, defense);
        }
    }
}

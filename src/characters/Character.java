package characters;

import equipments.Armor;
import equipments.Equipment;
import java.util.List;

public abstract class Character {
    private String name;
    private SpecialAbility specialAbility;
    private List<Equipment>  availableWeapons;
    private List<Armor> availableArmors;
    private Equipment[] weapons = new Equipment[2];
    private Armor activeArmor;
    private List<Minion> minions;
    private int health;
    private int power;
    private Modifier modifier;
    private List<PowerUp> powerUps;
    private List<Weakness> weaknesses;
    private CharacterType type;
    private int powerupValue = 0;
    private int weaknessValue = 0;
    private int gold;

    public String getName(){
        return name;
    }

    public CharacterType getType(){
        return type;
    }

    public int getHealth() {
        return health;
    }

    public void setPowerupValue(int powerupValue) {
        this.powerupValue = powerupValue;
    }

    public void setWeaknessValue(int weaknessValue) {
        this.weaknessValue = weaknessValue;
    }

    public int getPowerupValue() {
        return powerupValue;
    }

    public int getWeaknessValue() {
        return weaknessValue;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public List<Weakness> getWeaknesses() {
        return weaknesses;
    }

    public void addMinion(Minion minion){
        minions.add(minion);
    }

    public void removeMinion(int index){
        minions.remove(index);
    }

    public void addPowerUp(PowerUp powerup){
        powerUps.add(powerup);
    }

    public void addWeakness(Weakness weakness){
        weaknesses.add(weakness);
    }

    public Equipment[] getWeapons() {
        return weapons;
    }

    public Armor getActiveArmor() {
        return activeArmor;
    }

    public int getPower() {
        return power;
    }

    public List<Minion> getMinions() {
        return minions;
    }

    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }

    public void setType(CharacterType type) {
        this.type = type;
    }

    public void setSpecialAbility(SpecialAbility ability){
        this.specialAbility = ability;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActiveArmor(Armor activeArmor) {
        this.activeArmor = activeArmor;
    }

    public List<Equipment> getAvailableWeapons() {
        return availableWeapons;
    }

    public void setAvailableWeapons(List<Equipment> availableWeapons) {
        if (availableWeapons.size() > 2){
            System.out.println("No puedes tener más de dos armas disponibles.");
        }
        this.availableWeapons = availableWeapons;
    }

    public void setHealth(int health) {
        if (health < 0){
            this.health = health;
        } else if (health > 5) {
            this.health = 5;
        } else {
            this.health = health;
        }

    }

    public void setPower(int power) {
        if (power < 1){
            this.power = 1;
        } else if (power > 5) {
            this.power = 5;
        } else {
            this.power = power;
        }

    }

    public void setWeapons(Equipment weapon0, Equipment weapon1) {
        if (weapon0 != null) {
            weapons[0] = weapon0;
        }
        if (weapon1 != null) {
            weapons[1] = weapon1;
        }
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public abstract void attack(Character enemy);

    public boolean takeDamage(int attackValue, int defenseValue){
        boolean result = false;
        int minionsHealth = 0;
        for (Minion minion : minions){
            minionsHealth += minion.getHealth();
        }
        if (minionsHealth != 0) {
            while ((attackValue != 0) && (!minions.isEmpty())){
                minions.getFirst().takeDamage();
                if (minions.getFirst().getHealth() <= 0) {
                    minions.removeFirst();
                }
                attackValue -= 1;
            }
            result = true;
        } else {
            if(attackValue >= defenseValue){
                this.health -= 1;
                result = true;
            }
        }
        return result;
    }

    public abstract int calculateAttackPoints(int powerupValue, int weaknessValue);
    public abstract int calculateDefensePoints(int powerupValue, int weaknessValue);

    public int calculatePower(int value){
        int i;
        int successes = 0;
        for (i = 0; i < value; i++){
            int randomNumber = (int) (Math.random() * 6) + 1;
            if ((randomNumber == 5) || (randomNumber == 6)){
                successes += 1;
            }
        }
        return successes;
    }

    public void showStats() {
        System.out.println("Nombre: " + getName());
        System.out.println("Tipo: " + getType());
        System.out.println("Habilidad especial: " + getSpecialAbility());
        System.out.println("Armas activas: " + getWeapons() );
        System.out.println("Armadura activa: "+ getActiveArmor() );
        System.out.println("Conjunto de esbirros: "+ getMinions() );
        System.out.println("Oro: " + getGold());
        System.out.println("Salud: " + getHealth());
        System.out.println("Poder: " + getPower());
        System.out.println("Conjunto de debilidades: " + getWeaknesses());
        System.out.println("Conjunto de fortalezas: " + getPowerUps() );
    }

    public void selectEquipment() {
        if (availableWeapons.size() > 0) {
            setWeapons(availableWeapons.get(0), availableWeapons.size()> 1 ? availableWeapons.get(1) : null);
        }

        if (availableArmors.size() > 0) {
            setActiveArmor(availableArmors.get(0));
        }
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        if (gold < 0) {
            gold = 0;
        }
        this.gold = gold;
    }
}

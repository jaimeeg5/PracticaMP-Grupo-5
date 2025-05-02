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

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setWeapons(Equipment weapon0, Equipment weapon1) {
        weapons[0] = weapon0;
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

    public abstract void showStats();

    public void selectEquipment() {

    }

    public int getGold() {
        return gold;
    }
}

package characters;

import equipments.Equipment;
import org.json.JSONObject;

public class Werewolf extends Character{
    private double height;
    private int weight;
    private int rage = 0;

    public Werewolf(double height, int weight, CharacterType type) {
        this.weight = weight;
        this.height = height;
        super.setType(type);
    }

    public double getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getRage() {
        return rage;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setRage(int rage) {
        this.rage = rage;
    }

    @Override
    public void attack(Character enemy) {
        int powerupValue = 0;
        for (PowerUp powerup : super.getPowerUps()){
            powerupValue += powerup.getValue();
        }
        super.setPowerupValue(powerupValue);
        int weaknessValue = 0;
        for (Weakness weakness : super.getWeaknesses()){
            weaknessValue += weakness.getValue();
        }
        super.setWeaknessValue(weaknessValue);
        int powerupEnemyValue = 0;
        for (PowerUp powerup : enemy.getPowerUps()){
            powerupEnemyValue += powerup.getValue();
        }
        enemy.setPowerupValue(powerupEnemyValue);
        int weaknessEnemyValue = 0;
        for (Weakness weakness : enemy.getWeaknesses()){
            weaknessEnemyValue += weakness.getValue();
        }
        enemy.setWeaknessValue(weaknessEnemyValue);
        int attackPower = calculateAttackPoints(super.getPowerupValue(), super.getWeaknessValue());
        int defensePower = enemy.calculateDefensePoints(enemy.getPowerupValue(), enemy.getWeaknessValue());
        enemy.takeDamage(attackPower, defensePower);
    }

    @Override
    public int calculateAttackPoints(int powerupValue, int weaknessValue) {
        int attackPower = 0;
        int weaponsDamage = 0;
        for (Equipment weapon : super.getWeapons()){
            weaponsDamage += weapon.getAttackValue();
        }
        attackPower = super.getPower() + super.getActiveArmor().getAttackValue() + weaponsDamage + this.rage + powerupValue - weaknessValue;
        Don ability = (Don) super.getSpecialAbility();
        if (rage >= ability.getRageValue()){
            attackPower += ability.getAttack();
        }
        return super.calculatePower(attackPower);
    }

    @Override
    public int calculateDefensePoints(int powerupValue, int weaknessValue) {
        int defensePower = 0;
        int weaponsDefense = 0;
        for (Equipment weapon : super.getWeapons()){
            weaponsDefense += weapon.getDefenseValue();
        }
        defensePower = super.getPower() + super.getActiveArmor().getDefenseValue() + weaponsDefense+ this.rage + powerupValue - weaknessValue;
        Don ability = (Don) super.getSpecialAbility();
        if (rage >= ability.getRageValue()){
            defensePower += ability.getDefense();
        }
        return super.calculatePower(defensePower);
    }

    @Override
    public void showStats() {
        super.showStats();
        System.out.println("Altura: " + getHeight());
        System.out.println("Peso: " + getWeight());
        System.out.println("Rabia: " + getRage());
    }

    @Override
    public boolean takeDamage(int attackValue, int defenseValue){
        int minionsHealth = 0;
        for (Minion minion : super.getMinions()){
            minionsHealth += minion.getHealth();
        }
        boolean status = super.takeDamage(attackValue, defenseValue);
        if((minionsHealth == 0) && status && (this.rage < 3)){
            this.rage += 1;
        }
        return status;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = super.toJSONObject();
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        super.fromJSONObject(json);
    }
}

package characters;

import org.json.JSONObject;

public class Vampire extends Character{
    private int bloodPoints = 0;
    private int age;


    public Vampire(int age, CharacterType type) {
        this.age = age;
        super.setType(type);
    }

    public Vampire() {

    }

    public int getBloodPoints() {
        return bloodPoints;
    }

    public int getAge() {
        return age;
    }

    public void setBloodPoints(int bloodPoints) {
        this.bloodPoints = bloodPoints;
    }

    public void setAge(int age) {
        this.age = age;
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
        if (enemy.takeDamage(attackPower, defensePower)){
            bloodPoints += 4;
        }
    }

    @Override
    public int calculateAttackPoints(int powerupValue, int weaknessValue) {
        int attackPower = 0;
        int weaponsDamage = 0;
        for (Equipment weapon : getActiveWeapons()){
            if (weapon != null) {
                weaponsDamage += weapon.getAttackValue();
            }
        }
        attackPower = getPower() + getActiveArmor().getAttackValue() + weaponsDamage + powerupValue - weaknessValue;
        if (bloodPoints >= 5){
            attackPower += 2;
        }
        /*
        Discipline ability = (Discipline) getSpecialAbility();
        if (bloodPoints >= ability.getBloodCost()){
            bloodPoints -= ability.getBloodCost();
            attackPower += ability.getAttack();
        }
        */
        return calculatePower(attackPower);
    }

    @Override
    public int calculateDefensePoints(int powerupValue, int weaknessValue) {
        int defensePower = 0;
        int weaponsDefense = 0;
        for (Equipment weapon : super.getActiveWeapons()){
            if (weapon != null) {
                weaponsDefense += weapon.getDefenseValue();
            }
        }
        defensePower = super.getPower() + super.getActiveArmor().getDefenseValue() + weaponsDefense + powerupValue - weaknessValue;
        if (this.bloodPoints >= 5){
            defensePower += 2;
        }
        /*
        Discipline ability = (Discipline) super.getSpecialAbility();
        if (this.bloodPoints >= ability.getBloodCost()){
            this.bloodPoints -= ability.getBloodCost();
            defensePower += ability.getDefense();
        }
        */
        return super.calculatePower(defensePower);
    }

    @Override
    public void showStats() {
        super.showStats();
        System.out.println("Puntos de sangre: " + getBloodPoints());
        System.out.println("Edad: " + getAge());

    }

    @Override
    public boolean takeDamage(int attackValue, int defenseValue) {
        return super.takeDamage(attackValue, defenseValue);
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = super.toJSONObject();
        json.put("bloodPoints", bloodPoints);
        json.put("age", age);
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        super.fromJSONObject(json);
        bloodPoints = json.getInt("bloodPoints");
        age = json.getInt("age");
    }
}

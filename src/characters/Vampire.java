package characters;

import equipments.Equipment;

public class Vampire extends Character{
    private int bloodPoints = 0;
    private int age;

    public Vampire(int age, CharacterType type) {
        this.age = age;
        super.setType(type);
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
        for (Equipment weapon : super.getWeapons()){
            weaponsDamage += weapon.getAttackValue();
        }
        attackPower = super.getPower() + super.getActiveArmor().getAttackValue() + weaponsDamage + powerupValue - weaknessValue;
        if (bloodPoints >= 5){
            attackPower += 2;
        }
        Discipline ability = (Discipline) super.getSpecialAbility();
        if (bloodPoints >= ability.getBloodCost()){
            bloodPoints -= ability.getBloodCost();
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
        defensePower = super.getPower() + super.getActiveArmor().getDefenseValue() + weaponsDefense + powerupValue - weaknessValue;
        if (this.bloodPoints >= 5){
            defensePower += 2;
        }
        Discipline ability = (Discipline) super.getSpecialAbility();
        if (this.bloodPoints >= ability.getBloodCost()){
            this.bloodPoints -= ability.getBloodCost();
            defensePower += ability.getDefense();
        }
        return super.calculatePower(defensePower);
    }

    @Override
    public void showStats() {

    }

    @Override
    public boolean takeDamage(int attackValue, int defenseValue) {
        return super.takeDamage(attackValue, defenseValue);
    }

    public void setAge(int age) {
        this.age = age;
    }
}

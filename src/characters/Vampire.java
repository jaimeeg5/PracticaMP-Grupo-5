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
        int attackPower = 0;
        int defensePower = 0;
        int weaponsDamage = 0;
        int weaponsDefense = 0;
        int powerupValue = 0;
        int weaknessValue = 0;
        for (Equipment weapon : super.getWeapons()){
            weaponsDamage += weapon.getAttackValue();
            weaponsDefense += weapon.getDefenseValue();
        }
        for (PowerUp powerup : super.getPowerUps()){
            powerupValue += powerup.getValue();
        }
        for (Weakness weakness : super.getWeaknesses()){
            weaknessValue += weakness.getValue();
        }
        attackPower = super.getPower() + super.getActiveArmor().getAttackValue() + weaponsDamage + powerupValue - weaknessValue;
        defensePower = super.getPower() + super.getActiveArmor().getDefenseValue() + weaponsDefense + powerupValue - weaknessValue;
        if (bloodPoints >= 5){
            attackPower += 2;
            defensePower += 2;
        }
        Discipline ability = (Discipline) super.getSpecialAbility();
        if (bloodPoints >= ability.getBloodCost()){
            bloodPoints -= ability.getBloodCost();
            attackPower += ability.getAttack();
            defensePower += ability.getDefense();
        }
        int attack = super.calculatePower(attackPower, 'a');
        int defense = super.calculatePower(defensePower, 'd');
        if (enemy.takeDamage(attack, enemy.getDefenseValue())){
            bloodPoints += 4;
        }
    }

    @Override
    public boolean takeDamage(int attackValue, int defenseValue) {
        return super.takeDamage(attackValue, defenseValue);
    }

    public void setAge(int age) {
        this.age = age;
    }
}

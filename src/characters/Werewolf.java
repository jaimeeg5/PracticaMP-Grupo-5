package characters;

import equipments.Equipment;

public class Werewolf extends Character{
    private double height;
    private int weight;
    private int rage = 0;

    public Werewolf(double height, int weight, CharacterType type) {
        this.weight = weight;
        this.height = height;
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
        attackPower = super.getPower() + super.getActiveArmor().getAttackValue() + weaponsDamage + this.rage + powerupValue - weaknessValue;
        defensePower = super.getPower() + super.getActiveArmor().getDefenseValue() + weaponsDefense + this.rage + powerupValue - weaknessValue;
        Don ability = (Don) super.getSpecialAbility();
        if (rage >= ability.getRageValue()){
            attackPower += ability.getAttack();
            defensePower += ability.getDefense();
        }
        int attack = super.calculatePower(attackPower, 'a');
        int defense = super.calculatePower(defensePower, 'd');
        enemy.takeDamage(attack, enemy.getDefenseValue());
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
}

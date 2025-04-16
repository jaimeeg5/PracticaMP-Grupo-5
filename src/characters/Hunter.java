package characters;

import equipments.Equipment;

public class Hunter extends Character{
    private int willpower = 3;
    private int attackValue = 0;
    private int defenseValue = 0;

    public Hunter(CharacterType type) {
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
        attackPower = super.getPower() + super.getActiveArmor().getAttackValue() + weaponsDamage + this.willpower + powerupValue - weaknessValue;
        defensePower = super.getPower() + super.getActiveArmor().getDefenseValue() + weaponsDefense + this.willpower + powerupValue - weaknessValue;
        Talent ability = (Talent) super.getSpecialAbility();
        attackPower += ability.getAttack();
        defensePower += ability.getDefense();
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
        if((minionsHealth == 0) && status && (this.willpower > 0)){
            this.willpower -= 1;
        }
        return status;
    }
}

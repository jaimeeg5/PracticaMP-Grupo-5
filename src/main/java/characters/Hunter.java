package characters;

import org.json.JSONObject;

public class Hunter extends Character{
    private int willpower = 3;

    public Hunter(CharacterType type) {
        super.setType(type);
    }

    public Hunter() {

    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
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
        for (Equipment weapon : super.getActiveWeapons()){
            if (weapon != null) {
                weaponsDamage += weapon.getAttackValue();
            }
        }
        attackPower = super.getPower() + super.getActiveArmor().getAttackValue() + weaponsDamage + this.willpower + powerupValue - weaknessValue;
        /*
        Talent ability = (Talent) super.getSpecialAbility();
        attackPower += ability.getAttack();
         */
        return super.calculatePower(attackPower);
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
        defensePower = super.getPower() + super.getActiveArmor().getDefenseValue() + weaponsDefense + this.willpower + powerupValue - weaknessValue;
        /*
        Talent ability = (Talent) super.getSpecialAbility();
        defensePower += ability.getDefense();
         */
        return super.calculatePower(defensePower);
    }

    @Override
    public void showStats() {
        super.showStats();
        System.out.println("Voluntad: " + getWillpower());
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

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = super.toJSONObject();
        json.put("willpower", willpower);
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        super.fromJSONObject(json);
        willpower = json.getInt("willpower");
    }
}

package characters;

import equipments.Armor;
import equipments.Equipment;
import equipments.EquipmentType;
import equipments.OneHandedWeapon;
import game.Jsonable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public abstract class Character implements Jsonable {
    private String name;
    private SpecialAbility specialAbility;
    private List<Equipment>  availableWeapons;
    private List<Armor> availableArmors;
    private Equipment[] activeWeapons = new Equipment[2];
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

    public Equipment[] getActiveWeapons() {
        return activeWeapons;
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
        activeWeapons[0] = weapon0;
        activeWeapons[1] = weapon1;
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
        System.out.println("Nombre: " + name);
        System.out.println("Tipo: " + type);
        System.out.println("Habilidad especial: " + specialAbility);
        System.out.println("Armas activas: " + Arrays.toString(activeWeapons));
        System.out.println("Armadura activa: " + activeArmor);
        System.out.println("Esbirros: " + minions);
        System.out.println("Oro: " + gold);
        System.out.println("Salud: " + health);
        System.out.println("Poder: " + power);
        System.out.println("Fortalezas: " + powerUps);
        System.out.println("Debilidades: " + weaknesses);
    }

    public void selectEquipment() {
        if (!availableWeapons.isEmpty()) {
            int i = 1;
            for (Equipment weapon : availableWeapons) {
                System.out.println("[" + i + "] " + "Nombre: " + weapon.getName() + " Ataque: " + weapon.getAttackValue() + " Defensa: " + weapon.getDefenseValue());
            }
            Scanner input = new Scanner(System.in);
            int chosen;
            System.out.println("Introduce el numero del arma que quieres equipar");
            chosen = Integer.parseInt(input.nextLine()) - 1;
            if (availableWeapons.get(chosen).getType() == EquipmentType.TWOHANDEDWEAPON) {
                setWeapons(availableWeapons.get(chosen), null);
            } else {
                Equipment weapon1 = availableWeapons.get(chosen);
                System.out.println("Introduce el numero del segundo arma que quieres equipar");
                chosen = Integer.parseInt(input.nextLine()) - 1;
                setWeapons(weapon1, availableWeapons.get(chosen));
            }
        } else {
            System.out.println("No hay armas disponibles");
        }

        if (!availableArmors.isEmpty()) {
            int i = 1;
            for (Equipment armor : availableArmors) {
                System.out.println("[" + i + "] " + "Nombre: " + armor.getName() + " Ataque: " + armor.getAttackValue() + " Defensa: " + armor.getDefenseValue());
            }
            Scanner input = new Scanner(System.in);
            int chosen;
            System.out.println("Introduce el numero de la armadura que quieres equipar");
            chosen = Integer.parseInt(input.nextLine()) - 1;
            setActiveArmor(availableArmors.get(chosen));
        } else {
            System.out.println("No hay armaduras disponibles");
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


    public Character() {
        this.specialAbility = new SpecialAbility("", 0, 0);  // Inicializamos con valores por defecto
    }


    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("type", type);
        JSONArray arr = new JSONArray();
        for (Equipment weapon: availableWeapons) {
            arr.put(weapon.toJSONObject());
        }
        json.put("availableWeapons", arr);
        arr.clear();
        for (Equipment armor: availableArmors) {
            arr.put(armor.toJSONObject());
        }
        json.put("availableArmors", arr);
        arr.clear();
        for (Equipment weapon: activeWeapons) {
            arr.put(weapon.toJSONObject());
        }
        json.put("activeWeapons", arr);
        json.put("activeArmor", activeArmor.toJSONObject());
        arr.clear();
        for (Minion minion: minions) {
            arr.put(minion.toJSONObject());
        }
        json.put("minions", arr);
        arr.clear();
        for (PowerUp pw: powerUps) {
            arr.put(pw.toJSONObject());
        }
        json.put("powerUps", arr);
        arr.clear();
        for (Weakness wk: weaknesses) {
            arr.put(wk.toJSONObject());
        }
        json.put("weaknesses", arr);
        json.put("modifier", modifier.toJSONObject());
        json.put("power", power);
        json.put("name", name);
        json.put("health", health);
        json.put("gold", gold);
        json.put("powerUpValue", powerupValue);
        json.put("weaknessValue", weaknessValue);
        json.put("specialAbility", specialAbility.toJSONObject());  // Guardamos la habilidad especial
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        type = CharacterType.valueOf(json.getString("type"));
        JSONArray arr = json.getJSONArray("availableWeapons");
        availableWeapons.clear();
        for (int i = 0; i < arr.length(); i++) {
            Equipment eq = new ;   // TODO: en toJsonObject guardar el tipo de arma y aqui crear un arma en funcion de cual sea
            availableWeapons.add(eq.fromJSONObject(arr.getJSONObject(i)));
        }
        availableArmors.clear();
        for (int i = 0; i < arr.length(); i++) {
            Armor a = new Armor(0, "", );   // TODO: desisto, a partir de aqui la funcion está mal. Buenas noches gente.
            arr.put(armor.getName());
        }
        json.put("availableArmors", arr);
        arr.clear();
        for (Equipment weapon: activeWeapons) {
            arr.put(weapon.getName());
        }
        json.put("activeWeapons", arr);
        json.put("activeArmor", activeArmor.getName());
        arr.clear();
        for (Minion minion: minions) {
            arr.put(minion.toJSONObject());
        }
        json.put("minions", arr);
        arr.clear();
        for (PowerUp pw: powerUps) {
            arr.put(pw.toJSONObject());
        }
        json.put("powerUps", arr);
        arr.clear();
        for (Weakness wk: weaknesses) {
            arr.put(wk.toJSONObject());
        }
        json.put("weaknesses", arr);
        json.put("modifier", modifier.toJSONObject());
        json.put("power", power);
        json.put("name", name);
        json.put("health", health);
        json.put("gold", gold);
        json.put("powerUpValue", powerupValue);
        json.put("weaknessValue", weaknessValue);
        json.put("specialAbility", specialAbility.toJSONObject());
    }

}

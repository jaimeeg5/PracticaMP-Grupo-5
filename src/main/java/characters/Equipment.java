package characters;

import game.Jsonable;
import org.json.JSONObject;

public class Equipment implements Jsonable {
    private String name;
    private int attackValue;
    private int defenseValue;
    private EquipmentType type;

    public Equipment(String name, int attack, int defense, EquipmentType type){
        this.name = name;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.type = type;
    }

    public Equipment() {

    }

    public EquipmentType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("attackValue", attackValue);
        json.put("defenseValue", defenseValue);
        json.put("type", type.name());
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        this.name = json.getString("name");
        this.attackValue = json.getInt("attackValue");
        this.defenseValue = json.getInt("defenseValue");
        this.type = EquipmentType.valueOf(json.getString("type"));
    }

    @Override
    public String toString() {
        return name;
    }
}

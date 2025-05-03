package characters;

import game.Jsonable;
import org.json.JSONObject;

public class SpecialAbility implements Jsonable {
    private String name;
    private int attack;
    private int defense;

    public SpecialAbility(String name, int attack, int defense){
        this.name = name;
        this.attack = attack;
        this.defense = defense;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("attack", attack);
        json.put("defense", defense);
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        this.name = json.getString("name");
        this.attack = json.getInt("attack");
        this.defense = json.getInt("defense");
    }
}

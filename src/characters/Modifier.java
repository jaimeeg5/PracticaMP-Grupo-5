package characters;

import game.Jsonable;
import org.json.JSONObject;

public class Modifier implements Jsonable {
    private String name;
    private int value;
    private ModifierType type;

    public Modifier(String name, int value, String type) {
        this.name = name;
        this.value = value;
        if (type.equals("Fortaleza")){
            this.type = ModifierType.Strength;
        } else {
            this.type = ModifierType.Weakness;
        }
    }

    public Modifier() {

    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("value", value);
        json.put("type", type);
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        name = json.getString("name");
        value = json.getInt("value");
        type = ModifierType.valueOf(json.getString("type"));
    }
}

package characters;

import game.Jsonable;
import org.json.JSONObject;

public class PowerUp implements Jsonable {
    private String name;
    private int value;

    public PowerUp(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public PowerUp() {

    }

    @Override
    public String toString(){
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("value", value);
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        this.name = json.getString("name");
        this.value = json.getInt("value");
    }
}

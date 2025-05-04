package characters;

import game.Jsonable;
import org.json.JSONObject;

public class Weakness implements Jsonable {
    private String name;
    private int value;

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
        // TODO
        return null;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        // TODO
    }
}

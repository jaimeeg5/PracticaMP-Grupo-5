package characters;

import game.Jsonable;
import org.json.JSONObject;

public class Minion implements Jsonable {
    private String name;
    private int health;
    private MinionType type;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setType(MinionType type) {
        this.type = type;
    }
    public boolean takeDamage(){
        this.health -= 1;
        return true;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("health", health);
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        this.name = json.getString("name");
        this.health = json.getInt("health");
    }
}

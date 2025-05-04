package characters;

import game.Jsonable;
import org.json.JSONObject;

public class Minion implements Jsonable {
    private String name;
    private int health;

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public boolean takeDamage(){
        this.health -= 1;
        return true;
    }

    @Override
    public JSONObject toJSONObject() {
        // TODO:
        return null;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        // TODO:
    }
}

package game;

import org.json.JSONObject;

public interface Jsonable {
    public JSONObject toJSONObject();

    public void fromJSONObject(JSONObject json);
}

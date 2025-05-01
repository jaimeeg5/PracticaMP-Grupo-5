package characters;

public class Modifier {
    private String name;
    private int value;
    private ModifierType type;

    public Modifier(String name, int value, String type){
        this.name = name;
        this.value = value;
        if (type.equals("Fortaleza")){
            this.type = ModifierType.Strength;
        } else {
            this.type = ModifierType.Weakness;
        }
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}

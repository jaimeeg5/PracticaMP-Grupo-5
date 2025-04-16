package characters;

public class Modifier {
    private String name;
    private int value;
    private ModifierType type;

    public Modifier(String name, int value, ModifierType type){
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        return value;
    }
}

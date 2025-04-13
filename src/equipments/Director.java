package equipments;

public class Director {

    private EquipmentBuilder builder;

    public Director(EquipmentBuilder builder) {
        this.builder = builder;
    }

    public void changeBuilder(EquipmentBuilder builder) {
        this.builder = builder;
    }

    public EquipmentBuilder build(EquipmentType type) {
        return new EquipmentBuilder(type);
    }
}

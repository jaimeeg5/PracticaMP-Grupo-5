package equipments;

public interface Builder {
    void reset();

    void buildName(String name);

    void buildAttack(int attackValue);

    void buildDefense(int defenseValue);
}

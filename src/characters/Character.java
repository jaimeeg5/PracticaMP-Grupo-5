package characters;

import java.util.List;

public abstract class Character {
    private String name;
    private SpecialAbility specialAbility;
    private List<Weapon>  availableWeapons;
    private List<Armor> availableArmors;
    private Weapon[] Weapons= new Weapon[2];
    private Armor activeArmor;
    private List<Minion> minions;
    private int gold;
    private int health;
    private int power;
    private Modifier modifiers;

    public void addMinion(Minion minion){

    }

    public void removeMinion(int index){

    }

    public void chooseArmor(Armor armor){

    }

    public abstract void attack();

    public void setSpecialAbility(SpecialAbility specialAbility){

    }
    }

package characters;

public class Human extends Minion{
    private String loyalty;

    public void setLoyalty(String loyalty) {
        this.loyalty = loyalty;
    }

    @Override
    public boolean takeDamage(){
        return super.takeDamage();
    }
}

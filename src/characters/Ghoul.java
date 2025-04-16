package characters;

public class Ghoul extends Minion{
    private int dependency;

    public void setDependency(int dependency) {
        this.dependency = dependency;
    }

    @Override
    public boolean takeDamage(){
        return super.takeDamage();
    }
}

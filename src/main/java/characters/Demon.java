package characters;

import java.util.List;

public class Demon extends Minion {
    private String Pact;
    private List<Minion> minionList;

    public void setPact(String pact) {
        Pact = pact;
    }

    public List<Minion> getMinionList() {
        return minionList;
    }

    @Override
    public boolean takeDamage() {
        if (!minionList.isEmpty()) {
            minionList.getFirst().takeDamage();
            if (minionList.getFirst().getHealth() <= 0) {
                minionList.removeFirst();
            }
            return true;
        } else {
            return super.takeDamage();
        }
    }
}

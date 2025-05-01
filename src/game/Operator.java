package game;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

public class Operator extends User {
    public Operator(String nick, String name) {
        super(nick, name);
    }

    @Override
    public void operate(){

    }

    @Override
    public void update(WatchEvent<?> event) {

    }
}

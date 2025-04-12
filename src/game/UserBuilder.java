package game;

public class UserBuilder {
    UserType type;
    String name;
    String nick;
    String registerNumber;

    public UserBuilder(UserType type) {
        this.type = type;
    }

    public void buildName(String name) {
        this.name = name;
    }

    public void buildNick(String nick) {
        this.nick = nick;
    }

    public void buildRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public User build() {
        if ((name == null) || (nick == null)) {
            throw new RuntimeException("Deben establecerse el nombre y el nick antes de ejecutar build");
        }
        if (type == UserType.OPERATOR) {
            return new Operator(nick, name);
        }
        else {
            return new Player(nick, name, registerNumber);
        }
    }
}

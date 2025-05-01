package game;

import characters.Modifier;
import characters.Character;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class Combat {
    private Player challenger;
    private Player challenged;
    private int rounds = 0;
    private String date;
    private ZonedDateTime calcDate;
    private int gold;
    private String winner;
    private String loser;
    private int minionsAlive;
    private List<String> turnSummary;
    private List<Modifier> activeModifiers;
    private int id;
    private Notification notification;

    public Combat(Notification notification) {
        this.notification = notification;
        this.calcDate = ZonedDateTime.now(ZoneId.of("Europe/Madrid"));
        this.date = calcDate.getDayOfWeek().toString() + " " + calcDate.getDayOfMonth() + "/" +  calcDate.getMonth().toString() + "/" + calcDate.getYear() + " - " + calcDate.getHour() + ":" + calcDate.getMinute();
        this.turnSummary.add(this.date + "\n........................");
    }

    public static Combat loadFromDisk(int id){
        ObjectInputStream in = null;
        try {
            FileInputStream fileIn = new FileInputStream("data/combats/combat_" + id + ".dat");
            in = new ObjectInputStream(fileIn);
            Combat combat = (Combat) in.readObject();
            return combat;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar el combate: " + e.getMessage());
            return null;
        } finally {
            if (in != null){
                try{
                    in.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar el archivo: " + e.getMessage());
                }
            }
        }
    }

    public void saveToDisk() {
        ObjectOutputStream out = null;
        try {
            FileOutputStream fileOut = new FileOutputStream("data/combats/combat_" + id + ".dat");
            out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            System.out.println("Combate guardado correctamente en data/combats/combat_" + id + ".dat");
        } catch (IOException e) {
            System.out.println("Error al guardar el combate: " + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar el archivo: " + e.getMessage());
                }
            }
        }
    }

    public void fight(){
        Character character1 = challenger.getCharacter();
        Character character2 = challenged.getCharacter();
        while ((character1.getHealth() > 0) && (character2.getHealth() > 0)){
            character1.attack(character2);
            character2.attack(character1);
            this.rounds += 1;
            this.turnSummary.add("Turno " + this.rounds + ":\nSalud del jugador 1 = " + character1.getHealth() + "\nSalud del jugador 2 = " + character2.getHealth() + "\n-------------------------");
        }
        if ((character1.getHealth() > 0) && (character2.getHealth() <= 0)) {
            winner = "Jugador 1";
            loser = "Jugador 2";
            challenged.pay(challenger, gold);
        } else if ((character1.getHealth() <= 0) && (character2.getHealth() > 0)) {
            winner = "Jugador 2";
            loser = "Jugador 1";
            challenger.pay(challenged, gold);
        } else {
            winner = "Empate";
        }
    }

    public void printResult(){
        if (!winner.equals("Empate")) {
            System.out.println("El ganador del combate ha sido " + winner);
            System.out.println("El " + loser + " paga " + gold + " a " + winner);
            System.out.println(loser + " no podrá jugar en 24 h");
        } else {
            System.out.println("Los jugadores han empatado");
            System.out.println("Ninguno de los dos jugadores paga oro a su rival");
        }
    }

    public List<Modifier> getActiveModifiers() {
        return activeModifiers;
    }

    public void addModifier(Modifier modifier){
        this.activeModifiers.add(modifier);
    }

    public void removeModifier(Modifier modifier){
        this.activeModifiers.remove(modifier);
    }
}

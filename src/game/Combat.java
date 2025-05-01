package game;

import characters.Modifier;
import characters.Character;

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

        return null;
    }

    public void saveToDisk(){

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
        if ((character1.getHealth() > 0) && (character2.getHealth() == 0)) {
            winner = "Jugador 1";
            loser = "Jugador 2";
            challenged.pay(challenger, gold);
        } else if ((character1.getHealth() == 0) && (character2.getHealth() > 0)) {
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
}

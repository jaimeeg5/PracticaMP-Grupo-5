package game;
import characters.Character;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Combat implements Jsonable{
    private Player challenger = new Player();
    private Player challenged = new Player();
    private int rounds = 0;
    private String date;
    private ZonedDateTime calcDate;
    private int gold;
    private String winner;
    private String loser;
    private int minionsAlive;
    private List<String> turnSummary = new ArrayList<>();
    private List<String> activeModifiers = new ArrayList<>();
    private int id;

    public Combat(Player challenger, Player challenged, List<String> activeModifiers) {
        this.challenger = challenger;
        this.challenged = challenged;
        this.activeModifiers = activeModifiers;
        this.calcDate = ZonedDateTime.now(ZoneId.systemDefault());
        this.date = calcDate.getDayOfWeek().toString() + " " + calcDate.getDayOfMonth() + "/" +  calcDate.getMonth().toString() + "/" + calcDate.getYear() + " - " + calcDate.getHour() + ":" + calcDate.getMinute();
        this.id = GameData.getInstance().increaseLastCombatId();
    }

    public Combat() {

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
            winner = challenger.getNick();
            loser = challenged.getNick();
        } else if ((character1.getHealth() <= 0) && (character2.getHealth() > 0)) {
            winner = challenged.getNick();
            loser = challenger.getNick();
        } else {
            winner = "Empate";
        }
    }

    public void printResult(){
        if (!winner.equals("Empate")) {
            System.out.println("El ganador del combate ha sido " + winner);
            System.out.println(loser + " paga " + gold + " a " + winner);
            System.out.println(loser + " no podrá jugar en 24 h");
        } else {
            System.out.println("Los jugadores han empatado");
            System.out.println("Ninguno de los dos jugadores paga oro a su rival");
        }
    }

    public List<String> getActiveModifiers() {
        return activeModifiers;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        JSONArray turnsArray = new JSONArray();
        for (String turn: turnSummary) {
            turnsArray.put(turn);
        }
        JSONArray modifiersArray = new JSONArray();
        for (String modifier: activeModifiers) {
            modifiersArray.put(modifier);
        }
        json.put("id", id);
        json.put("date", date);
        json.put("challenger", challenger.toJSONObject());
        json.put("challenged", challenged.toJSONObject());
        json.put("gold", gold);
        json.put("modifiers", modifiersArray);
        json.put("numRounds", rounds);
        json.put("turns", turnsArray);
        json.put("winner", winner);
        json.put("loser", loser);
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        JSONArray arr;
        id = json.getInt("id");
        date = (json.getString("date"));
        JSONObject player = json.getJSONObject("challenger");
        challenger.fromJSONObject(player);
        player = json.getJSONObject("challenged");
        challenged.fromJSONObject(player);
        gold = json.getInt("gold");
        arr = json.getJSONArray("modifiers");
        activeModifiers.clear();
        for (int i = 0; i < arr.length(); i++) {
            activeModifiers.add(arr.getString(i));
        }
        rounds = json.getInt("numRounds");
        arr = json.getJSONArray("turns");
        turnSummary.clear();
        for (int i = 0; i < arr.length(); i++) {
            turnSummary.add(arr.getString(i));
        }
        winner = (json.getString("winner"));
        loser = (json.getString("loser"));
    }

    public String getWinner() {
        return winner;
    }

    public String getLoser() {
        return loser;
    }

    public int getGold() {
        return gold;
    }

    public int getId() {
        return id;
    }
}

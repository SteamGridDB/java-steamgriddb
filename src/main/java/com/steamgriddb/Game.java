package com.steamgriddb;

import com.steamgriddb.Enums.SGDBIdTypes;
import com.steamgriddb.Connection.SGDBConnectionManager;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Represents a Game as found on SteamGridDB.com
 * 
 * @author mpaterakis
 */
public class Game {

    /*
    * Fields
    */
    private int id = -1;
    private String name = "";
    private ArrayList<String> types = new ArrayList<>();

    /**
     * Constructor for Game.
     * 
     * @param id The id of the Game
     * @param type The type of the given id
     */
    public Game(int id, SGDBIdTypes type) {
        JSONObject json = new JSONObject();

        if (type == SGDBIdTypes.SteamAppId) {
            json = SGDBConnectionManager.getJSON("games/steam/" + id);
        } else if (type == SGDBIdTypes.GameId) {
            json = SGDBConnectionManager.getJSON("games/id/" + id);
        }
        
        if (json.getBoolean("success")) {
            this.id = json.getJSONObject("data").getInt("id");
            this.name = json.getJSONObject("data").getString("name");
            JSONArray typesArray = json.getJSONObject("data").getJSONArray("types");
            for (int i = 0; i < typesArray.length(); i++) {
                this.types.add(typesArray.get(i).toString());
            }
        }
    }

    /**
     * Get a Game object from a SteamAppId.
     * 
     * @param steamAppId The Game's SteamAppId
     * @return A Game object
     */
    public static Game getGameBySteamAppId(int steamAppId) {
        Game game = new Game(steamAppId, SGDBIdTypes.SteamAppId);
        return game;
    }

    /**
     * Get a JSONObject of a Game from a SteamAppId.
     * 
     * @param steamAppId The Game's SteamAppId
     * @return A JSONObject object with the Game's data
     */
    public static JSONObject getGameJSONBySteamAppId(int steamAppId) {
        JSONObject json = SGDBConnectionManager.getJSON("games/steam/" + steamAppId);
        return json;
    }

    /**
     * Get a Game object from a GameId.
     * 
     * @param gameId The Game's GameId
     * @return A Game object
     */
    public static Game getGameByGameId(int gameId) {
        Game game = new Game(gameId, SGDBIdTypes.GameId);
        return game;
    }

    /**
     * Get a JSONObject of a Game from a GameId.
     * 
     * @param gameId The Game's GameId
     * @return A JSONObject object with the Game's data
     */
    public static JSONObject getGameJSONByGameId(int gameId) {
        JSONObject json = SGDBConnectionManager.getJSON("games/id/" + gameId);
        return json;
    }

    /**
     * Get the Game's id.
     * 
     * @return id of the Game
     */
    public int getId() {
        return id;
    }

    /**
     * Get the Game's name.
     * 
     * @return name of the Game
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Game's types.
     * 
     * @return types of the Game
     */
    public ArrayList<String> getTypes() {
        return types;
    }
}

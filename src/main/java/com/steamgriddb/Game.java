package com.steamgriddb;

import com.steamgriddb.Enums.SGDBIdTypes;
import com.steamgriddb.Connection.SGDBConnectionManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String id = "";
    private String name = "";
    private ArrayList<String> types = new ArrayList<>();

    /**
     * Constructor for Game.
     *
     * @param id The id of the Game
     * @param type The type of the given id [OriginId, EgsId, UplayId]
     */
    public Game(String id, SGDBIdTypes type) {
        try {
            id = URLEncoder.encode(id, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONObject json = new JSONObject();

        switch (type) {
            case SteamAppId:
                json = getGameJSONBySteamAppId(id);
                break;
            case GameId:
                json = getGameJSONByGameId(id);
                break;
            case GogId:
                json = getGameJSONByGogId(id);
                break;
            case OriginId:
                json = getGameJSONByOriginId(id);
                break;
            case EgsId:
                json = getGameJSONByEgsId(id);
                break;
            case UplayId:
                json = getGameJSONByUplayId(id);
                break;
            default:
                break;
        }

        if (json.getBoolean("success")) {
            this.id = String.valueOf(json.getJSONObject("data").getInt("id"));
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
    public static Game getGameBySteamAppId(String steamAppId) {
        Game game = new Game(steamAppId, SGDBIdTypes.SteamAppId);
        return game;
    }

    /**
     * Get a JSONObject of a Game from a SteamAppId.
     *
     * @param steamAppId The Game's SteamAppId
     * @return A JSONObject object with the Game's data
     */
    public static JSONObject getGameJSONBySteamAppId(String steamAppId) {
        JSONObject json = SGDBConnectionManager.getJSON("games/steam/" + steamAppId);
        return json;
    }

    /**
     * Get a Game object from an EgsId.
     *
     * @param egsId The Game's EgsId
     * @return A Game object
     */
    public static Game getGameByEgsId(String egsId) {
        Game game = new Game(egsId, SGDBIdTypes.EgsId);
        return game;
    }

    /**
     * Get a JSONObject of a Game from an EgsId.
     *
     * @param egsId The Game's EgsId
     * @return A JSONObject object with the Game's data
     */
    public static JSONObject getGameJSONByEgsId(String egsId) {
        JSONObject json = SGDBConnectionManager.getJSON("games/egs/" + egsId);
        return json;
    }

    /**
     * Get a Game object from an OriginId.
     *
     * @param originId The Game's OriginId
     * @return A Game object
     */
    public static Game getGameByOriginId(String originId) {
        Game game = new Game(originId, SGDBIdTypes.OriginId);
        return game;
    }

    /**
     * Get a JSONObject of a Game from an OriginId.
     *
     * @param originId The Game's OriginId
     * @return A JSONObject object with the Game's data
     */
    public static JSONObject getGameJSONByOriginId(String originId) {
        JSONObject json = SGDBConnectionManager.getJSON("games/origin/" + originId);
        return json;
    }

    /**
     * Get a Game object from a UplayId.
     *
     * @param uplayId The Game's UplayId
     * @return A Game object
     */
    public static Game getGameByUplayId(String uplayId) {
        Game game = new Game(uplayId, SGDBIdTypes.UplayId);
        return game;
    }

    /**
     * Get a JSONObject of a Game from a UplayId.
     *
     * @param uplayId The Game's UplayId
     * @return A JSONObject object with the Game's data
     */
    public static JSONObject getGameJSONByUplayId(String uplayId) {
        JSONObject json = SGDBConnectionManager.getJSON("games/uplay/" + uplayId);
        return json;
    }

    /**
     * Get a Game object from a GogId.
     *
     * @param gogId The Game's GogId
     * @return A Game object
     */
    public static Game getGameByGogId(String gogId) {
        Game game = new Game(gogId, SGDBIdTypes.GogId);
        return game;
    }

    /**
     * Get a JSONObject of a Game from a GogId.
     *
     * @param gogId The Game's GogId
     * @return A JSONObject object with the Game's data
     */
    public static JSONObject getGameJSONByGogId(String gogId) {
        JSONObject json = SGDBConnectionManager.getJSON("games/gog/" + gogId);
        return json;
    }

    /**
     * Get a Game object from a GameId.
     *
     * @param gameId The Game's GameId
     * @return A Game object
     */
    public static Game getGameByGameId(String gameId) {
        Game game = new Game(gameId, SGDBIdTypes.GameId);
        return game;
    }

    /**
     * Get a JSONObject of a Game from a GameId.
     *
     * @param gameId The Game's GameId
     * @return A JSONObject object with the Game's data
     */
    public static JSONObject getGameJSONByGameId(String gameId) {
        JSONObject json = SGDBConnectionManager.getJSON("games/id/" + gameId);
        return json;
    }

    /**
     * Get the Game's id.
     *
     * @return id of the Game
     */
    public String getId() {
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

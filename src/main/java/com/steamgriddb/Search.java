package com.steamgriddb;

import com.steamgriddb.Connection.SGDBConnectionManager;
import com.steamgriddb.Enums.SGDBIdTypes;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class represents SGDB's Search API function.
 * 
 * @author mpaterakis
 */
public class Search {

    /**
     * Search for Games using a search term.
     *
     * @param searchTerm The search term to be used in the search
     * @return An ArrayList of Game objects that the search yielded
     */
    public static ArrayList<Game> searchGamesByName(String searchTerm) {
        JSONObject json = new JSONObject();
        try {
            json = SGDBConnectionManager.getJSON("search/autocomplete/" + URLEncoder.encode(searchTerm, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (json.getBoolean("success")) {
            ArrayList<Game> games = new ArrayList<>();
            JSONArray gamesArray = json.getJSONArray("data");
            for (int i = 0; i < gamesArray.length(); i++) {
                games.add(new Game(String.valueOf(gamesArray.getJSONObject(i).getInt("id")), SGDBIdTypes.GameId));
            }
            return games;
        }

        // If json is empty, return empty ArrayList
        return new ArrayList<>();
    }

    /**
     * Search for Games using a search term to get a raw JSONObject.
     *
     * @param searchTerm The search term to be used in the search
     * @return A JSONObject that the search yielded
     */
    public static JSONObject searchGamesByNameJSON(String searchTerm) {
        JSONObject json = SGDBConnectionManager.getJSON("search/autocomplete/" + searchTerm);
        return json;
    }
}

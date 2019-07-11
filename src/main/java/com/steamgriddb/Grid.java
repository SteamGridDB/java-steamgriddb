package com.steamgriddb;

import com.steamgriddb.Enums.SGDBIdTypes;
import com.steamgriddb.Enums.SGDBStyles;
import com.steamgriddb.Connection.SGDBConnectionManager;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents a Grid as found on SteamGridDB.com
 *
 * @author mpaterakis
 */
public class Grid {

    /*
    * Fields
     */
    private double score = -1;
    private String id = "";
    private String style = "";
    private String url = "";
    private String thumb = "";
    private ArrayList<String> tags = new ArrayList<>();
    private Author author;

    /**
     * Constructor for Grid.
     *
     * @param id The Grid ID
     * @param score The Grid's Score
     * @param style The Grid's Style
     * @param url The Grid's URL
     * @param thumb The Grid's Thumb URL
     * @param tags The Grid's Tags
     * @param author The Grid's Author
     */
    public Grid(String id, double score, SGDBStyles style, String url, String thumb, ArrayList<String> tags, Author author) {
        switch (style) {
            case Alternate: {
                this.style = "alternate";
                break;
            }
            case NoLogo: {
                this.style = "no_logo";
                break;
            }
            case Blurred: {
                this.style = "blurred";
                break;
            }
            case Material: {
                this.style = "material";
                break;
            }
            default:
                break;
        }
        this.id = id;
        this.score = score;
        this.url = url;
        this.thumb = thumb;
        this.tags = tags;
        this.author = author;
    }

    /**
     * Get Grids by ID and filter by styles.
     *
     * @param id The ID a Game uses
     * @param idType The type of ID (SteamAppID or GameID)
     * @param styles An array of styles for filtering the results
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsById(String id, SGDBIdTypes idType, SGDBStyles[] styles) {
        ArrayList<Grid> grids = new ArrayList<>();
        String apiUrl = "";
        String stylesStr = buildStylesString(styles);

        switch (idType) {
            case GameId:
                apiUrl = "grids/game/";
                break;
            case SteamAppId:
                apiUrl = "grids/steam/";
                break;
            case EgsId:
                apiUrl = "grids/egs/";
                break;
            case UplayId:
                apiUrl = "grids/uplay/";
                break;
            case OriginId:
                apiUrl = "grids/origin/";
                break;
            case GogId:
                apiUrl = "grids/gog/";
                break;
            default:
                break;
        }

        JSONObject json = SGDBConnectionManager.getJSON(apiUrl + id + "?styles=" + stylesStr);

        if (json.getBoolean("success")) {
            JSONArray jsonArray = json.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonGrid = jsonArray.getJSONObject(i);
                JSONObject jsonAuthor = jsonGrid.getJSONObject("author");

                JSONArray jsonTags = jsonGrid.getJSONArray("tags");

                ArrayList<String> tagStrings = new ArrayList<>();

                for (int j = 0; j < jsonTags.length(); j++) {
                    tagStrings.add(jsonTags.get(i).toString());
                }

                Author authorObject = new Author(jsonAuthor.getString("name"), jsonAuthor.getString("steam64"), jsonAuthor.getString("avatar"));

                String styleString = jsonGrid.getString("style");
                SGDBStyles style = null;
                switch (styleString) {
                    case "alternate": {
                        style = SGDBStyles.Alternate;
                        break;
                    }
                    case "no_logo": {
                        style = SGDBStyles.NoLogo;
                        break;
                    }
                    case "blurred": {
                        style = SGDBStyles.Blurred;
                        break;
                    }
                    case "material": {
                        style = SGDBStyles.Material;
                        break;
                    }
                    default:
                        break;
                }

                Grid grid = new Grid(String.valueOf(jsonGrid.getInt("id")), jsonGrid.getDouble("score"), style,
                        jsonGrid.getString("url"), jsonGrid.getString("thumb"), tagStrings, authorObject);
                grids.add(grid);
            }
        }

        return grids;
    }

    /**
     * Get Grids by ID.
     *
     * @param id The ID a Game uses
     * @param idType The type of ID (SteamAppID or GameID)
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsById(String id, SGDBIdTypes idType) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(id, idType, new SGDBStyles[0]);
        return grids;
    }

    /**
     * Get Grids by SteamAppID.
     *
     * @param steamAppId The SteamAppID of a Game
     * @param styles An array of SGDBStyles for filtering Grids
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsBySteamAppId(String steamAppId, SGDBStyles[] styles) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(steamAppId, SGDBIdTypes.SteamAppId, styles);
        return grids;
    }

    /**
     * Get Grids by SteamAppID.
     *
     * @param steamAppId The SteamAppID of a Game
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsBySteamAppId(String steamAppId) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(steamAppId, SGDBIdTypes.SteamAppId);
        return grids;
    }

    /**
     * Get Grids by OriginId.
     *
     * @param originId The OriginId of a Game
     * @param styles An array of SGDBStyles for filtering Grids
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsByOriginId(String originId, SGDBStyles[] styles) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(originId, SGDBIdTypes.OriginId, styles);
        return grids;
    }

    /**
     * Get Grids by OriginId.
     *
     * @param originId The OriginId of a Game
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsByOriginId(String originId) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(originId, SGDBIdTypes.OriginId);
        return grids;
    }

    /**
     * Get Grids by UplayId.
     *
     * @param uplayId The UplayId of a Game
     * @param styles An array of SGDBStyles for filtering Grids
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsByUplayId(String uplayId, SGDBStyles[] styles) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(uplayId, SGDBIdTypes.UplayId, styles);
        return grids;
    }

    /**
     * Get Grids by UplayId.
     *
     * @param uplayId The UplayId of a Game
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsByUplayId(String uplayId) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(uplayId, SGDBIdTypes.UplayId);
        return grids;
    }

    /**
     * Get Grids by EgsId.
     *
     * @param egsId The EgsId of a Game
     * @param styles An array of SGDBStyles for filtering Grids
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsByEgsId(String egsId, SGDBStyles[] styles) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(egsId, SGDBIdTypes.EgsId, styles);
        return grids;
    }

    /**
     * Get Grids by EgsId.
     *
     * @param egsId The EgsId of a Game
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsByEgsId(String egsId) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(egsId, SGDBIdTypes.EgsId);
        return grids;
    }

    /**
     * Get Grids by GogId.
     *
     * @param gogId The GogId of a Game
     * @param styles An array of SGDBStyles for filtering Grids
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsByGogId(String gogId, SGDBStyles[] styles) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(gogId, SGDBIdTypes.GogId, styles);
        return grids;
    }

    /**
     * Get Grids by GogId.
     *
     * @param gogId The GogId of a Game
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsByGogId(String gogId) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(gogId, SGDBIdTypes.GogId);
        return grids;
    }

    /**
     * Get Grids by GameID.
     *
     * @param gameId The GameID of a Game
     * @param styles An array of SGDBStyles for filtering Grids
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsByGameId(String gameId, SGDBStyles[] styles) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(gameId, SGDBIdTypes.GameId, styles);
        return grids;
    }

    /**
     * Get Grids by GameID.
     *
     * @param gameId The GameID of a Game
     * @return An ArrayList of Grid objects
     */
    public static ArrayList<Grid> getGridsByGameId(String gameId) {
        ArrayList<Grid> grids = new ArrayList<>();
        grids = getGridsById(gameId, SGDBIdTypes.GameId);
        return grids;
    }

    /**
     * Get a JSONObject of a Grids array from a SteamAppId.
     *
     * @param steamAppId A Game's SteamAppId
     * @param styles An array of SGDBStyles for filtering Grids
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONBySteamAppId(String steamAppId, SGDBStyles[] styles) {
        String stylesStr = buildStylesString(styles);
        JSONObject json = SGDBConnectionManager.getJSON("grids/steam/" + steamAppId + "?styles=" + stylesStr);
        return json;
    }

    /**
     * Get a JSONObject of a Grids array from a SteamAppId.
     *
     * @param steamAppId A Game's SteamAppId
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONBySteamAppId(String steamAppId) {
        JSONObject json = SGDBConnectionManager.getJSON("grids/steam/" + steamAppId);
        return json;
    }

    /**
     * Get a JSONObject of a Grids array from an OriginId.
     *
     * @param originId A Game's OriginId
     * @param styles An array of SGDBStyles for filtering Grids
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONByOriginId(String originId, SGDBStyles[] styles) {
        String stylesStr = buildStylesString(styles);
        JSONObject json = SGDBConnectionManager.getJSON("grids/origin/" + originId + "?styles=" + stylesStr);
        return json;
    }

    /**
     * Get a JSONObject of a Grids array from an OriginId.
     *
     * @param originId A Game's OriginId
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONByOriginId(String originId) {
        JSONObject json = SGDBConnectionManager.getJSON("grids/origin/" + originId);
        return json;
    }

    /**
     * Get a JSONObject of a Grids array from an EgsId.
     *
     * @param egsId A Game's EgsId
     * @param styles An array of SGDBStyles for filtering Grids
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONByEgsId(String egsId, SGDBStyles[] styles) {
        String stylesStr = buildStylesString(styles);
        JSONObject json = SGDBConnectionManager.getJSON("grids/egs/" + egsId + "?styles=" + stylesStr);
        return json;
    }

    /**
     * Get a JSONObject of a Grids array from an EgsId(.
     *
     * @param egsId A Game's EgsId(
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONByEgsId(String egsId) {
        JSONObject json = SGDBConnectionManager.getJSON("grids/egs/" + egsId);
        return json;
    }

    /**
     * Get a JSONObject of a Grids array from a UplayId.
     *
     * @param uplayId A Game's UplayId
     * @param styles An array of SGDBStyles for filtering Grids
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONByUplayId(String uplayId, SGDBStyles[] styles) {
        String stylesStr = buildStylesString(styles);
        JSONObject json = SGDBConnectionManager.getJSON("grids/uplay/" + uplayId + "?styles=" + stylesStr);
        return json;
    }

    /**
     * Get a JSONObject of a Grids array from a UplayId.
     *
     * @param uplayId A Game's UplayId
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONByUplayId(String uplayId) {
        JSONObject json = SGDBConnectionManager.getJSON("grids/uplay/" + uplayId);
        return json;
    }

    /**
     * Get a JSONObject of a Grids array from a GogId.
     *
     * @param gogId A Game's GogId
     * @param styles An array of SGDBStyles for filtering Grids
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONByGogId(String gogId, SGDBStyles[] styles) {
        String stylesStr = buildStylesString(styles);
        JSONObject json = SGDBConnectionManager.getJSON("grids/gog/" + gogId + "?styles=" + stylesStr);
        return json;
    }

    /**
     * Get a JSONObject of a Grids array from a GogId.
     *
     * @param gogId A Game's GogId
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONByGogId(String gogId) {
        JSONObject json = SGDBConnectionManager.getJSON("grids/gog/" + gogId);
        return json;
    }

    /**
     * Get a JSONObject of a Grids array from a GameId.
     *
     * @param gameId A Game's GameId
     * @param styles An array of SGDBStyles for filtering Grids
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONByGameId(String gameId, SGDBStyles[] styles) {
        String stylesStr = buildStylesString(styles);
        JSONObject json = SGDBConnectionManager.getJSON("grids/game/" + gameId + "?styles=" + stylesStr);
        return json;
    }

    /**
     * Get a JSONObject of a Grids array from a GameId.
     *
     * @param gameId A Game's GameId
     * @return A JSONObject object with the Grid data
     */
    public static JSONObject getGridJSONByGameId(String gameId) {
        JSONObject json = SGDBConnectionManager.getJSON("grids/game/" + gameId);
        return json;
    }

    /**
     * Upload a Grid by entering its required data.
     *
     * @param gameId The GameID of a Game
     * @param style The style of the Grid
     * @param filePath The file path of an image
     * @return True if the upload was successful, false if otherwise
     */
    public static boolean uploadGrid(String gameId, SGDBStyles style, String filePath) {
        File grid = new File(filePath);

        String styleStr = "";

        switch (style) {
            case Alternate: {
                styleStr = "alternate";
                break;
            }
            case NoLogo: {
                styleStr = "no_logo";
                break;
            }
            case Blurred: {
                styleStr = "blurred";
                break;
            }
            case Material: {
                styleStr = "material";
                break;
            }
            default:
                break;
        }

        Map<Object, Object> params = new LinkedHashMap<>();
        params.put("game_id", gameId);
        params.put("style", styleStr);
        params.put("grid", grid.toPath());

        JSONObject json = SGDBConnectionManager.postMultipart("grids", params);

        return json.getBoolean("success");
    }

    /**
     * Vote for a Grid using its ID.
     *
     * @param vote The vote's value (True = Upvote, False = Downvote)
     * @param gridID The Grid's ID
     */
    public static void vote(boolean vote, String gridID) {
        if (vote) {
            upvoteById(gridID);
        } else {
            downvoteById(gridID);
        }
    }

    /**
     * Vote for this Grid.
     *
     * @param vote The vote's value (True = Upvote, False = Downvote)
     */
    public void vote(boolean vote) {
        if (vote) {
            upvote();
        } else {
            downvote();
        }
    }

    /**
     * Upvote this Grid.
     */
    public void upvote() {
        SGDBConnectionManager.post("grids/vote/up/" + getId());
    }

    /**
     * Downvote this Grid.
     */
    public void downvote() {
        SGDBConnectionManager.post("grids/vote/down/" + getId());
    }

    /**
     * Upvote a Grid using its ID.
     *
     * @param gridId The Grid's ID
     */
    public static void upvoteById(String gridId) {
        SGDBConnectionManager.post("grids/vote/up/" + gridId);
    }

    /**
     * Downvote a Grid using its ID.
     *
     * @param gridId The Grid's ID
     */
    public static void downvoteById(String gridId) {
        SGDBConnectionManager.post("grids/vote/down/" + gridId);
    }

    /**
     * Delete this Grid.
     */
    public void delete() {
        SGDBConnectionManager.delete("grids/" + getId());
    }

    /**
     * Delete a Grid using its ID.
     *
     * @param gridId The Grid's ID
     */
    public static void deleteByGridID(String gridId) {
        SGDBConnectionManager.delete("grids/" + gridId);
    }

    /**
     * Delete multiple Grids using an array of IDs.
     *
     * @param gridIds The Grid's ID
     */
    public static void deleteByGridIDs(String[] gridIds) {
        for (int i = 0; i < gridIds.length; i++) {
            deleteByGridID(gridIds[i]);
        }
    }

    /**
     * Get the Grid's ID.
     *
     * @return The Grid's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get the Grid's Score.
     *
     * @return The Grid's Score
     */
    public double getScore() {
        return score;
    }

    /**
     * Get the Grid's Style.
     *
     * @return The Grid's Style
     */
    public String getStyle() {
        return style;
    }

    /**
     * Get the Grid's URL.
     *
     * @return The Grid's URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Get the Grid's Thumb URL.
     *
     * @return The Grid's Thumb URL
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * Get the Grid's Tags.
     *
     * @return The Grid's Tags
     */
    public ArrayList<String> getTags() {
        return tags;
    }

    /**
     * Get the Grid's Author.
     *
     * @return The Grid's Author
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * Build a string from and SGDBStyles array.
     * 
     * @param styles The SGDBStyles array to be converted
     * @return String containing the given SGDBStyles
     */
    private static String buildStylesString(SGDBStyles[] styles) {
        String stylesStr = "";

        for (int i = 0; i < styles.length; i++) {
            String currentStyle = "";
            switch (styles[i]) {
                case Alternate: {
                    currentStyle = "alternate";
                    break;
                }
                case NoLogo: {
                    currentStyle = "no_logo";
                    break;
                }
                case Blurred: {
                    currentStyle = "blurred";
                    break;
                }
                case Material: {
                    currentStyle = "material";
                    break;
                }
                default:
                    break;
            }
            stylesStr += currentStyle;
            if (i != styles.length - 1) {
                stylesStr += ",";
            }
        }

        return stylesStr;
    }
}

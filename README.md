# Java-SteamGridDB
A Java wrapper for SteamGridDB's API

### Installation

[![](https://jitpack.io/v/mpaterakis/java-steamgriddb.svg)](https://jitpack.io/#mpaterakis/java-steamgriddb)

## Getting Started
#### Get your API key
[You can generate an API key on your user preferences page.](https://www.steamgriddb.com/profile/preferences)

#### Require the library into your project.
```java
import com.SteamGridDB.*;
import com.SteamGridDB.Enums.*;
import com.SteamGridDB.Connection.*;
```

#### Initialize the SGDBConnectionManager using your API key and the API base uri
```java
SGDBConnectionManager.initialize("https://www.steamgriddb.com/api/v2", "myAuthKey");
```

#### Search for a game:
```java
// Get an ArrayList of games that match the search term
var games = Search.searchGamesByName("cyberpunk");

// Get a JSONObject containing the response from the API [Can be converted to string using .toString()]
var gamesJson = Search.searchGamesByNameJSON("cyberpunk");
```

#### Get a game object without searching:
```java
// Get a Game using a GameId
var game = Game.getGameByGameId("1234");

// Get a Game using a SteamAppId
var game = Game.getGameBySteamAppId("567890");
      
// Get a Game using an EgsId
var game = Game.getGameByGogId("77f2b98e2cef40c8a7437518bf420e47");

// Get a Game using an OriginId
var game = Game.getGameByOriginId("OFB-EAST:57557");

// Get a Game using an UplayId
var game = Game.getGameByUplayId("4a1562a4-c4d2-4bc5-a85e-f3db588b0072");

// Get a Game using an GogId
var game = Game.getGameByGogId("2031519202");

// Get a Game using its constructor
var game = new Game("567890", SGDBIdTypes.SteamAppId);

// Get a JSONObject containing the response from the API
var gameJson = Search.getGameJSONBySteamAppId("567890");
```

#### Do something with a game object:
```java
// Get a Game's Name
var gameName = game.getName();

// Get a Game's styles
var stylesArray = game.getStyles();
```

#### Get some grids:
```java
// Get grids by game ID
var grid = Grid.getGridsByGameId("1234");
    
// Get grids by Steam App Id
var grids = Grid.getGridsBySteamAppId("1234");
    
// Alternatively, you can do it like this:
var grids = Grid.getGridsById("1234", SGDBIdTypes.GameId);

// Get a JSONObject containing the response from the API
var gridJson = Search.getGridJSONBySteamAppId("567890");
```

#### Filter the styles:
```java
// Create an SGDBStyles array
SGDBStyles styles[] = {SGDBStyles.Alternate, SGDBStyles.NoLogo};

// Same as before, but using the styles filter
var grid = Grid.getGridsByGameId("1234", styles);
    
var grids = Grid.getGridsBySteamAppId("1234", styles);

var grids = Grid.getGridsById("1234", SGDBIdTypes.GameId, styles);
```

#### Do something with a grid object:
```java
// Get the first Grid's score
var gridScore = grids.get(0).getScore();

// Get the same Grid's Author's username
var authorName = grids.get(0).getAuthor().getName();
```

## Other methods
#### Vote on grids:
```java
// Upvote the first grid of the game with ID 1234
var grid = Grid.getGridsByGameId("1234").get(0);
grid.upvote();

// Downvote the same grid
var grid = Grid.getGridsByGameId("1234").get(0);
grid.upvote();
    
// Alternatively, use the Grid's ID (80 in this case) to vote:
Grid.upvoteById("80");
Grid.downvoteById("80");
```

#### Upload a grid:
```java
// Upload a blurred grid to Half-Life 2 (2254)
Grid.upload("2254", SGDBStyles.Blurred, "path/of/image.img");
```

#### Delete grids:
```java
// Delete a grid by ID
Grid.deleteByGridID(gameID);

// Delete multiple grids by ID
var gameIDs = {"123", "456"};
Grid.deleteByGridIDs(gameIDs);

// Delete a Grid object
var grid = Grid.getGridsByGameId("1234").get(0);
grid.delete();
```

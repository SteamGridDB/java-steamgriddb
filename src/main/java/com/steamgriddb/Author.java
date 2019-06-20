package com.steamgriddb;

/**
 * Represents a Author as found on SteamGridDB.com
 *
 * @author mpaterakis
 */
public class Author {

    /*
    * Fields
    */
    private String name = "";
    private String steam64 = "";
    private String avatar = "";

    /**
     * Constructor for Author.
     * 
     * @param name The Author's name
     * @param steam64 The Author's Steam64 ID
     * @param avatar  The Author's avatar URL
     */
    public Author(String name, String steam64, String avatar) {
        this.name = name;
        this.steam64 = steam64;
        this.avatar = avatar;
    }

    /**
     * Get the Author's name.
     * 
     * @return The Author's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Author's Steam64 ID.
     * 
     * @return The Author's Steam64 ID
     */
    public String getSteam64() {
        return steam64;
    }
    
    /**
     * Get the Author's avatar URL.
     * 
     * @return The Author's avatar URL
     */
    public String getAvatar() {
        return avatar;
    }
    
    
}

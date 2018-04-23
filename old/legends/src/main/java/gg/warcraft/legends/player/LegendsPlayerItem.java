package gg.warcraft.legends.player;

public class LegendsPlayerItem {
    private String id;
    private String displayName;
    private String race;
    private String specialization;
    private String description;
    private long timeOfFirstConnect;
    private long timePlayed;
    private long goldCoins;
    private long shopGems;
    private long votesCast;

    public LegendsPlayerItem(String id, String displayName, String race, String specialization, String description,
                             long timeOfFirstConnect, long timePlayed, long goldCoins, long shopGems, long votesCast) {
        this.id = id;
        this.displayName = displayName;
        this.race = race;
        this.specialization = specialization;
        this.description = description;
        this.timeOfFirstConnect = timeOfFirstConnect;
        this.timePlayed = timePlayed;
        this.goldCoins = goldCoins;
        this.shopGems = shopGems;
        this.votesCast = votesCast;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeOfFirstConnect() {
        return timeOfFirstConnect;
    }

    public void setTimeOfFirstConnect(long timeOfFirstConnect) {
        this.timeOfFirstConnect = timeOfFirstConnect;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

    public long getGoldCoins() {
        return goldCoins;
    }

    public void setGoldCoins(long goldCoins) {
        this.goldCoins = goldCoins;
    }

    public long getShopGems() {
        return shopGems;
    }

    public void setShopGems(long shopGems) {
        this.shopGems = shopGems;
    }

    public long getVotesCast() {
        return votesCast;
    }

    public void setVotesCast(long votesCast) {
        this.votesCast = votesCast;
    }
}

package gg.warcraft.legends.player;

import gg.warcraft.legends.combat.Specialization;
import gg.warcraft.legends.entity.LegendsRace;
import gg.warcraft.legends.entity.SimpleLegendsEntity;
import gg.warcraft.monolith.spigot.PlayerDecorator;
import gg.warcraft.monolith.util.ConsoleColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SimpleLegendsPlayer extends SimpleLegendsEntity implements LegendsPlayer, PlayerDecorator {
    private final UUID uniqueId;
    private final long joined;

    private String displayName;
    private LegendsRace race;
    private Specialization specialization;
    private String description;
    private long votes;
    private long gold;
    private long timePlayed;
    private long gems;

    private Player player;
    private long timeOfConnect;

    public SimpleLegendsPlayer(UUID uniqueId, String name) {
        this(uniqueId, System.currentTimeMillis(), name,
                0L, 0L, 0L,
                null, null, "undescribed", 0L);
    }

    public SimpleLegendsPlayer(UUID uniqueId, long joined, String displayName,
                               long timePlayed, long votes, long gems,
                               LegendsRace race, Specialization specialization, String description, long gold) {
        this.uniqueId = uniqueId;
        this.joined = joined;
        this.displayName = displayName;
        this.timePlayed = timePlayed;
        this.votes = votes;
        this.gems = gems;
        this.race = race;
        this.specialization = specialization;
        this.description = description;
        this.gold = gold;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public LegendsRace getRace() {
        return race;
    }

    @Override
    public void setRace(LegendsRace race) {
        this.race = race;
        updatePlayerListName();
    }

    @Override
    public Specialization getSpecialization() {
        return specialization;
    }

    @Override
    public void setSpecialization(Specialization spec) {
        this.specialization = spec;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        if (getPlayer() != null) {
            return getPlayer().getDisplayName();
        } else {
            System.out.println("Accessed display name of offline player! " + "(" + displayName + ")");
            return displayName;
        }
    }

    @Override
    public void setDisplayName(String name) {
        this.displayName = name;
        if (getPlayer() != null) {
            System.out.println("Display name was set for " + name);
            getPlayer().setDisplayName(name);
        } else {
            System.out.println("Tried to set display name of offline player! " + "(" + name + ")");
        }
    }

    @Override
    public long getTimeOfFirstJoin() {
        return joined;
    }

    @Override
    public long getTimePlayed() {
        long timePlayedThisSession = System.currentTimeMillis() - timeOfConnect;
        return timePlayed + timePlayedThisSession;
    }

    @Override
    public long getVoteCount() {
        return votes;
    }

    @Override
    public void addVote() {
        votes += 1;
    }

    @Override
    public long getInGameCurrency() {
        return gold;
    }

    @Override
    public void addInGameCurrency(long amount) {
        this.gold += amount;
    }

    @Override
    public void removeInGameCurrency(long amount) {
        this.gold -= amount;
        if (gold < 0) {
            System.out.println(ConsoleColor.RED +
                    "Plugin deducted too much gold from player resulting in negative balance!" +
                    ConsoleColor.RESET);
        }
    }

    @Override
    public long getDonatorCurrency() {
        return gems;
    }

    @Override
    public long getTimeOfConnect() {
        return timeOfConnect;
    }

    @Override
    public void onConnect(Player player) {
        this.player = player;
        player.setDisplayName(displayName);
        timeOfConnect = System.currentTimeMillis();
        recalculateMaxHealth();
        updatePlayerListName();
    }

    @Override
    public void onDisconnect() {
        long timePlayedThisSession = System.currentTimeMillis() - timeOfConnect;
        timePlayed += timePlayedThisSession;
    }

    private void updatePlayerListName() {
        switch (player.getName()) {
            case "fishb6nes":
            case "eijlen":
                player.setPlayerListName(ChatColor.DARK_RED + "[A] " + getRace().getColor() + player.getName());
                break;
            case "Dragonayr":
                player.setPlayerListName(ChatColor.GOLD + "[M] " + getRace().getColor() + player.getName());
                break;
        }
    }
}

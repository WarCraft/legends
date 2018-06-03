package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.LegendsFaction;
import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerDataRepository;
import gg.warcraft.legends.app.entity.player.SimpleLegendsPlayerData;
import gg.warcraft.monolith.api.data.DataService;
import gg.warcraft.monolith.api.entity.Faction;
import gg.warcraft.monolith.api.entity.player.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultLegendsPlayerDataRepository implements LegendsPlayerDataRepository {
    private static final String KEY_PREFIX = "legendsplayer:";
    private static final String MINECRAFT_NAME = "minecraftname";
    private static final String DISPLAY_NAME = "displayname";
    private static final String TIME_OF_CONNECT = "timeofconnect";
    private static final String TIME_OF_FIRST_CONNECT = "timeoffirstconnect";
    private static final String TIME_PLAYED = "timeplayed";
    private static final String TIMES_VOTED = "timesvoted";
    private static final String FACTION = "faction";
    private static final String GOLD_COINS = "goldcoins";
    private static final String TOTAL_GOLD_COINS = "totalgoldcoins";
    private static final String FAVOR = "favor";
    private static final String TOTAL_FAVOR = "totalfavor";
    private static final String RANK = "rank"; // TODO should this be stored? data is already in totalfavor

    private final DataService dataService;

    @Inject
    public DefaultLegendsPlayerDataRepository(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public LegendsPlayerData get(UUID playerId) {
        Map<String, String> data = dataService.getMap(getDataKey(playerId));
        if (data.isEmpty()) {
            return null;
        }
        String minecraftName = getMinecraftNameFromData(data);
        String displayName = getDisplayNameFromData(data);
        long timeOfConnect = getTimeOfConnectFromData(data);
        long timeOfFirstConnect = getTimeOfFirstConnectFromData(data);
        long timePlayed = getTimePlayedFromData(data);
        int timesVoted = getTimesVotedFromData(data);
        Faction faction = getFactionFromData(data);
        int goldCoins = getGoldCoinsFromData(data);
        int totalGoldCoins = getTotalGoldCoinsFromData(data);
        int favor = getFavorFromData(data);
        int totalFavor = getTotalFavorFromData(data);
        Rank rank = null; // TODO should this be stored? data is already in totalfavor
        return new SimpleLegendsPlayerData(playerId, minecraftName, displayName, timeOfConnect, timeOfFirstConnect,
                timePlayed, timesVoted, faction, goldCoins, totalGoldCoins, favor, totalFavor, rank);
    }

    @Override
    public void save(PlayerData data) {
        LegendsPlayerData legendsData = (LegendsPlayerData) data;
        Map<String, String> newData = new HashMap<>();
        mapMinecraftNameToData(newData, legendsData.getMinecraftName());
        mapDisplayNameToData(newData, legendsData.getDisplayName());
        mapTimeOfConnectToData(newData, legendsData.getTimeOfConnect());
        mapTimeOfFirstConnectToData(newData, legendsData.getTimeOfFirstConnect());
        mapTimePlayedToData(newData, legendsData.getTimePlayed());
        mapTimesVotedToData(newData, legendsData.getTimesVoted());
        mapFactionToData(newData, legendsData.getFaction());
        mapGoldCoinsToData(newData, legendsData.getGoldCoins());
        mapTotalGoldCoinsToData(newData, legendsData.getTotalGoldCoins());
        mapFavorToData(newData, legendsData.getFavor());
        mapTotalFavorToData(newData, legendsData.getTotalFavor());
        dataService.setMap(getDataKey(data.getPlayerId()), newData);
    }

    private String getDataKey(UUID playerId) {
        return KEY_PREFIX + playerId;
    }

    private String getMinecraftNameFromData(Map<String, String> data) {
        return data.get(MINECRAFT_NAME);
    }

    private String getDisplayNameFromData(Map<String, String> data) {
        return data.get(DISPLAY_NAME);
    }

    private long getTimeOfConnectFromData(Map<String, String> data) {
        return Long.parseLong(data.get(TIME_OF_CONNECT));
    }

    private long getTimeOfFirstConnectFromData(Map<String, String> data) {
        return Long.parseLong(data.get(TIME_OF_FIRST_CONNECT));
    }

    private long getTimePlayedFromData(Map<String, String> data) {
        return Long.parseLong(data.get(TIME_PLAYED));
    }

    private int getTimesVotedFromData(Map<String, String> data) {
        return Integer.parseInt(data.get(TIMES_VOTED));
    }

    private Faction getFactionFromData(Map<String, String> data) {
        return LegendsFaction.ofName(data.get(FACTION));
    }

    private int getGoldCoinsFromData(Map<String, String> data) {
        return Integer.parseInt(data.get(GOLD_COINS));
    }

    private int getTotalGoldCoinsFromData(Map<String, String> data) {
        return Integer.parseInt(data.get(TOTAL_GOLD_COINS));
    }

    private int getFavorFromData(Map<String, String> data) {
        return Integer.parseInt(data.get(FAVOR));
    }

    private int getTotalFavorFromData(Map<String, String> data) {
        return Integer.parseInt(data.get(TOTAL_FAVOR));
    }

    private void mapMinecraftNameToData(Map<String, String> data, String minecraftName) {
        data.put(MINECRAFT_NAME, minecraftName);
    }

    private void mapDisplayNameToData(Map<String, String> data, String displayName) {
        data.put(DISPLAY_NAME, displayName);
    }

    private void mapTimeOfConnectToData(Map<String, String> data, long timeOfConnect) {
        data.put(TIME_OF_CONNECT, "" + timeOfConnect);
    }

    private void mapTimeOfFirstConnectToData(Map<String, String> data, long timeOfFirstConnect) {
        data.put(TIME_OF_FIRST_CONNECT, "" + timeOfFirstConnect);
    }

    private void mapTimePlayedToData(Map<String, String> data, long timePlayed) {
        data.put(TIME_PLAYED, "" + timePlayed);
    }

    private void mapTimesVotedToData(Map<String, String> data, long timesVoted) {
        data.put(TIMES_VOTED, "" + timesVoted);
    }

    private void mapFactionToData(Map<String, String> data, Faction faction) {
        data.put(FACTION, "" + faction.getName());
    }

    private void mapGoldCoinsToData(Map<String, String> data, long goldCoins) {
        data.put(GOLD_COINS, "" + goldCoins);
    }

    private void mapTotalGoldCoinsToData(Map<String, String> data, long totalGoldCoins) {
        data.put(TOTAL_GOLD_COINS, "" + totalGoldCoins);
    }

    private void mapFavorToData(Map<String, String> data, long favor) {
        data.put(FAVOR, "" + favor);
    }

    private void mapTotalFavorToData(Map<String, String> data, long totalFavor) {
        data.put(TOTAL_FAVOR, "" + totalFavor);
    }
}

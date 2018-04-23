package gg.warcraft.legends.player;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.google.gson.Gson;
import gg.warcraft.legends.combat.Specialization;
import gg.warcraft.legends.entity.LegendsRace;
import gg.warcraft.monolith.infrastructure.DynamoDbTable;
import gg.warcraft.monolith.player.PlayerRepository;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.RunnableUtils;

import java.util.UUID;

public class LegendsPlayerRepository implements PlayerRepository<LegendsPlayer> {
    private final Gson gson;
    private final DynamoDbTable table;

    public LegendsPlayerRepository() {
        this.gson = new Gson();
        this.table = new DynamoDbTable("legends-player", "id");
    }

    @Override
    public LegendsPlayer get(UUID uniqueId) {
        Item item = table.getItem(uniqueId.toString());
        if (item == null) {
            return null;
        }

        String json = item.toJSON();
        LegendsPlayerItem playerItem = gson.fromJson(json, LegendsPlayerItem.class);
        return mapFromItem(playerItem);
    }

    @Override
    public LegendsPlayer getOrNew(UUID uniqueId, String name) {
        LegendsPlayer player = get(uniqueId);
        if (player != null) {
            return player;
        }

        player = new SimpleLegendsPlayer(uniqueId, name);
        save(player);
        return player;
    }

    @Override
    public void save(LegendsPlayer player) {
        LegendsPlayerItem playerItem = mapToItem(player);
        String json = gson.toJson(playerItem);
        Item item = Item.fromJSON(json);

        RunnableUtils.runLaterAsync(
                () -> table.putItem(item),
                new Duration(1, Duration.Type.TICKS)
        );
    }

    private LegendsPlayer mapFromItem(LegendsPlayerItem item) {
        boolean hasRace = item.getRace() != null && !item.getRace().equals("null");
        boolean hasSpecialization = item.getSpecialization() != null && !item.getSpecialization().equals("null");
        return new SimpleLegendsPlayer(
                UUID.fromString(item.getId()),
                item.getTimeOfFirstConnect(),
                item.getDisplayName(),
                item.getTimePlayed(),
                item.getVotesCast(),
                item.getShopGems(),
                hasRace ? LegendsRace.valueOf(item.getRace()) : null,
                hasSpecialization ? Specialization.valueOf(item.getSpecialization()) : null,
                item.getDescription(),
                item.getGoldCoins()
        );
    }

    private LegendsPlayerItem mapToItem(LegendsPlayer player) {
        boolean hasRace = player.getRace() != null;
        boolean hasSpecialization = player.getSpecialization() != null;
        return new LegendsPlayerItem(
                player.getUniqueId().toString(),
                player.getDisplayName(),
                hasRace ? player.getRace().name() : "null",
                hasSpecialization ? player.getSpecialization().name() : "null",
                player.getDescription(),
                player.getTimeOfFirstJoin(),
                player.getTimePlayed(),
                player.getInGameCurrency(),
                player.getDonatorCurrency(),
                player.getVoteCount()
        );
    }
}

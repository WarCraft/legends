package gg.warcraft.legends.shop;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.google.gson.Gson;
import gg.warcraft.monolith.infrastructure.DynamoDbTable;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.RunnableUtils;

import java.util.UUID;

public class ShopProfileRepository {
    private final Gson gson;
    private final DynamoDbTable table;

    public ShopProfileRepository() {
        this.gson = new Gson();
        this.table = new DynamoDbTable("legends-shop", "playerId");
    }

    public ShopProfile get(UUID playerId) {
        Item item = table.getItem(playerId.toString());
        if (item == null) {
            return null;
        }

        String json = item.toJSON();
        return gson.fromJson(json, ShopProfile.class);
    }

    public ShopProfile getOrNew(UUID playerId) {
        ShopProfile profile = get(playerId);
        if (profile != null) {
            return profile;
        }

        profile = new ShopProfile(playerId);
        save(profile);
        return profile;
    }

    public void save(ShopProfile profile) {
        String json = gson.toJson(profile);
        Item item = Item.fromJSON(json);

        RunnableUtils.runLaterAsync(
                () -> table.putItem(item),
                new Duration(1, Duration.Type.TICKS)
        );
    }
}

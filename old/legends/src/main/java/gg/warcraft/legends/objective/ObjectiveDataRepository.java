package gg.warcraft.legends.objective;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.google.gson.Gson;
import gg.warcraft.monolith.infrastructure.DynamoDbTable;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.RunnableUtils;

import java.util.HashMap;
import java.util.Map;

public class ObjectiveDataRepository {
    private final Gson gson;
    private final DynamoDbTable table;
    private final Map<String, LegendsGameItem> games;

    public ObjectiveDataRepository() {
        this.gson = new Gson();
        this.table = new DynamoDbTable("legends-war", "id");
        this.games = new HashMap<>();
        table.scan().stream()
                .map(this::itemToObject)
                .forEach(game -> games.put(game.getId(), game));
    }

    public LegendsGameItem get(String id) {
        return games.get(id);
    }

    public void save(LegendsGameItem game) {
        String json = gson.toJson(game);
        Item item = Item.fromJSON(json);

        RunnableUtils.runLaterAsync(
                () -> table.putItem(item),
                new Duration(1, Duration.Type.TICKS)
        );
    }

    private LegendsGameItem itemToObject(Item item) {
        String json = item.toJSON();
        System.out.println(json);
        return gson.fromJson(json, LegendsGameItem.class);
    }
}

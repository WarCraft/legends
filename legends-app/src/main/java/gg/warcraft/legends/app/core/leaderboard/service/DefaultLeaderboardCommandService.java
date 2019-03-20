package gg.warcraft.legends.app.core.leaderboard.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.core.leaderboard.service.AbstractLeaderboardService;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardCommandService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.app.core.leaderboard.LeaderboardEntry;
import gg.warcraft.legends.app.core.leaderboard.MojangProfile;
import gg.warcraft.legends.app.core.leaderboard.MojangTextures;
import gg.warcraft.monolith.api.core.JsonMapper;
import gg.warcraft.monolith.api.core.PersistenceService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.util.TimeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Base64;
import java.util.List;
import java.util.SortedMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultLeaderboardCommandService extends AbstractLeaderboardService implements LeaderboardCommandService {
    private static final String LEADERBOARDS_BUCKET = "leaderboards";

    private final PersistenceService persistenceService;
    private final TaskService taskService;
    private final ObjectMapper jsonMapper;
    private final AmazonS3 s3;

    @Inject
    public DefaultLeaderboardCommandService(GameQueryService gameQueryService,
                                            PersistenceService persistenceService,
                                            TaskService taskService, TimeUtils timeUtils,
                                            @JsonMapper ObjectMapper jsonMapper) {
        super(gameQueryService, timeUtils);
        this.persistenceService = persistenceService;
        this.taskService = taskService;
        this.jsonMapper = jsonMapper;
        this.s3 = AmazonS3ClientBuilder.defaultClient();
    }

    @Override
    public void updateEntry(LegendsStatistic statistic, UUID playerId, int value) {
        String leaderboardKey = getLeaderboardKey(statistic);
        persistenceService.setSortedSet(leaderboardKey, playerId.toString(), value);
    }

    @Override
    public void publishLeaderboard(LegendsStatistic statistic) {
        String leaderboardKey = getLeaderboardKey(statistic);
        SortedMap<String, Double> ranks = persistenceService.getSortedSet(leaderboardKey);

        taskService.runNextTickAsync(() -> {
            List<LeaderboardEntry> entries = ranks.entrySet().stream()
                    .map(entry -> {
                        String cleanPlayerId = entry.getKey().replace("-", "");
                        int playerScore = entry.getValue().intValue();
                        String playerName = "???";
                        String skinUrl = "";

                        try { // TODO cache these results
                            CloseableHttpClient httpclient = HttpClients.createDefault();
                            HttpGet httpGet = new HttpGet("https://sessionserver.mojang.com/session/minecraft/profile/" + cleanPlayerId);
                            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                                System.out.println(response.getStatusLine());
                                HttpEntity responseEntity = response.getEntity();

                                String body = EntityUtils.toString(responseEntity);
                                MojangProfile profile = jsonMapper.readValue(body, MojangProfile.class);
                                playerName = profile.getName();

                                String texturesData = profile.getProperties().stream()
                                        .filter(property -> property.getName().equals("textures"))
                                        .findFirst()
                                        .get().getValue();
                                byte[] decodedTexturesData = Base64.getDecoder().decode(texturesData);
                                MojangTextures textures = jsonMapper.readValue(decodedTexturesData, MojangTextures.class);
                                skinUrl = textures.getSkinUrl();

                                EntityUtils.consume(responseEntity);
                            }
                        } catch (Exception ex) {
                            System.out.println("Failed to retrieve Mojang data for " + cleanPlayerId);
                        }

                        return new LeaderboardEntry(playerName, playerScore, skinUrl);
                    })
                    .collect(Collectors.toList());

            try {
                String entriesJson = jsonMapper.writeValueAsString(entries.toArray());
                s3.putObject(LEADERBOARDS_BUCKET, leaderboardKey + ".json", entriesJson);
            } catch (JsonProcessingException ex) {
                System.out.println("Failed to parse " + leaderboardKey + " entries: " + ex.getMessage());
            } catch (AmazonServiceException ex) {
                System.out.println("Failed to publish " + leaderboardKey + " with error code " +
                        ex.getErrorCode() + ": " + ex.getMessage());
            }
        });
    }
}

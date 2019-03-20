package gg.warcraft.legends.app.leaderboard.handler;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import gg.warcraft.legends.api.core.leaderboard.Leaderboard;
import gg.warcraft.legends.app.core.persistence.LeaderboardEntryItem;
import gg.warcraft.monolith.api.core.JsonMapper;
import gg.warcraft.monolith.api.core.PersistenceService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.List;
import java.util.SortedMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class LeaderboardPublishingHandler implements Runnable {
    private static final String LEADERBOARDS_BUCKET = "leaderboards";

    private final PersistenceService persistenceService;
    private final ObjectMapper jsonMapper;
    private final AmazonS3 s3;
    // private final Map<UUID, String> avatarBase64Data;

    public LeaderboardPublishingHandler(PersistenceService persistenceService, @JsonMapper ObjectMapper jsonMapper) {
        this.persistenceService = persistenceService;
        this.jsonMapper = jsonMapper;
        this.s3 = AmazonS3ClientBuilder.defaultClient();
        // this.avatarBase64Data = new HashMap<>();
    }

    private void getPlayerDataFromMojang(UUID playerId) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("https://sessionserver.mojang.com/session/minecraft/profile/" + playerId);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please take care that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            try {
                System.out.println(response.getStatusLine());
                HttpEntity responseEntity = response.getEntity();
                // TODO do something useful with the response body
                String data = EntityUtils.toString(responseEntity);
                // and ensure it is fully consumed
                EntityUtils.consume(responseEntity);
            } finally {
                response.close();
            }
        } catch (Exception ex) {
            System.out.println("Failed to retrieve Mojang data for " + playerId);
        }
    }

    private String computeAvatarBase64(UUID playerId) {
        // TODO integrate user avatar
        return "TODO";
    }

    @Override
    public void run() {
        Leaderboard current = null;
        int gameId = 0;
        SortedMap<String, Double> ranks = persistenceService.getSortedSet(current.getKey() + gameId);

        List<LeaderboardEntryItem> entries = ranks.entrySet().stream()
                .map(entry -> {
                    String playerId = entry.getKey().replace("-", "");
                    int playerScore = entry.getValue().intValue();
                    // TODO at the start of a day update all the names and avatars cached
                    // TODO retrieve player name and avatar data using playerId from mojang API
                    // TODO (we have to get avatar data anyways, so might as well not bother the server for the name)

                    // TODO create a LeaderboardEntryItem using the name, score, and avatar
                    return new LeaderboardEntryItem("name here", playerScore, "avatar64 here");
                })
                .collect(Collectors.toList());

        try {
            String entriesJson = jsonMapper.writeValueAsString(entries.toArray());
            s3.putObject(LEADERBOARDS_BUCKET, "leaderboardname/gameid.json", entriesJson);
        } catch (JsonProcessingException ex) {
            System.out.println("Failed to map " + "type" + " leaderboard: " + ex.getMessage());
        } catch (AmazonServiceException ex) {
            System.out.println("Failed to publish " + "type" + " leaderboard with error code " +
                    ex.getErrorCode() + ": " + ex.getMessage());
        }
    }
}

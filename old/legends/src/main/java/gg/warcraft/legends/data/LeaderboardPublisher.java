package gg.warcraft.legends.data;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.RunnableUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import redis.clients.jedis.Tuple;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class LeaderboardPublisher implements Runnable {
    private static final String WARCRAFT_SITE_BUCKET = "warcraft.gg";

    private final AmazonS3 s3;
    private final Map<UUID, String> avatarBase64Data;

    public LeaderboardPublisher() {
        s3 = AmazonS3ClientBuilder.defaultClient();
        avatarBase64Data = new HashMap<>();

        RunnableUtils.runLaterAsync(this, new Duration(60, Duration.Type.SECONDS));
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
        Set<Tuple> killingBlowsLeaderboard = DataService.getInstance().getTopKillingBlows();
        System.out.println("Creating leaderboard for " + killingBlowsLeaderboard.size() + " entries.");

        if (killingBlowsLeaderboard.isEmpty()) {
            RunnableUtils.runLaterAsync(this, new Duration(60, Duration.Type.SECONDS));
            return;
        }

        String playerDataTemplate = "{ name: \"%s\", score: %s },\n";
        StringBuilder playerData = new StringBuilder();
        killingBlowsLeaderboard.iterator().forEachRemaining(tuple -> {
            UUID playerId = UUID.fromString(tuple.getElement());
            int playerScore = (int) tuple.getScore();

            String avatarBase64 = avatarBase64Data.get(playerId);
            if (avatarBase64 == null) {
                avatarBase64 = computeAvatarBase64(playerId);
                // TODO integrate user avatar
            }

            String playerName = Bukkit.getOfflinePlayer(playerId).getName();
            playerData.append(String.format(playerDataTemplate, playerName, playerScore));
        });

        File tmpLeaderboardHtmlFile = new File("/tmp/warcraft_leaderboard.html");
        try {
            if (tmpLeaderboardHtmlFile.createNewFile()) {
            } else {
                System.out.println("Failed to create temporary warcraft leaderboard HTML file, already existed!");
                return;
            }

        } catch (Exception ex) {
            System.out.println("Failed to create temporary leaderboard HTML file");
            return;
        }

        try (InputStream leaderboardHtmlStream = getClass().getResourceAsStream("/leaderboard.html")) {
            try (PrintWriter filePrinter = new PrintWriter("/tmp/warcraft_leaderboard.html")) {
                try (Scanner scanner = new Scanner(leaderboardHtmlStream)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (line.contains("//ADD_PLAYERS_HERE")) {
                            filePrinter.println(playerData.toString());
                        } else {
                            filePrinter.println(line);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to create real-time leaderboard html");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return;
        }

        try {
            s3.putObject(WARCRAFT_SITE_BUCKET, "leaderboard/index.html", tmpLeaderboardHtmlFile);
        } catch (AmazonServiceException ex) {
            System.out.println("Failed to publish leaderboard data with error code " + ex.getErrorCode());
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return;
        }

        if (!tmpLeaderboardHtmlFile.delete()) {
            System.out.println("Failed to delete temporary leaderboard HTML file");
            return;
        }

        RunnableUtils.runLaterAsync(this, new Duration(60, Duration.Type.SECONDS));
    }
}

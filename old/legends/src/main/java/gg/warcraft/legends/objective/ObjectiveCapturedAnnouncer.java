package gg.warcraft.legends.objective;

import gg.warcraft.monolith.entity.Race;
import gg.warcraft.monolith.objective.CapturableObjective;
import gg.warcraft.monolith.objective.event.ObjectiveCapturedEvent;
import gg.warcraft.monolith.objective.event.ObjectiveNeutralizedEvent;
import gg.warcraft.monolith.player.PlayerService;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.RunnableUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ObjectiveCapturedAnnouncer implements Listener {
    private static final String discordWebhook = "https://discordapp.com/api/webhooks/427168926540234773/izX7BeEhd-vxfAXA2ZxfQtFCYmWFKqU5ySEsVtt_xsRpSnvox6PdcwUFJyoaNXsGfOAp";
    private static final Duration ONE_TICK = new Duration(1, Duration.Type.TICKS);

    private final PlayerService playerService;

    public ObjectiveCapturedAnnouncer() {
        this(PlayerService.getInstance());
    }

    public ObjectiveCapturedAnnouncer(PlayerService playerService) {
        this.playerService = playerService;
    }

    private void publishDiscordNotification(String notification) {
        StatusLine status = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost post = new HttpPost(discordWebhook);
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("content", notification));
            post.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response = httpclient.execute(post);
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please take care that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            try {
                status = response.getStatusLine();
                HttpEntity responseEntity = response.getEntity();
                // and ensure it is fully consumed
                EntityUtils.consume(responseEntity);
            } finally {
                response.close();
            }
        } catch (Exception ex) {
            System.out.println("Failed to publish discord notification: " + notification + ", with status: " + status);
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onObjectiveCapturedEvent(ObjectiveCapturedEvent event) {
        CapturableObjective objective = event.getObjective();
        Race occupiers = objective.getOccupiers();
        String notification = "The " + occupiers.getName() + " have captured " + objective.getName() + "!";
        String announcement = occupiers.getColor() + notification;
        playerService.getOnlineMonolithPlayers().forEach(player -> {
            player.sendTitle("", announcement);
            player.sendMessage(announcement);
        });
        RunnableUtils.runLaterAsync(() -> publishDiscordNotification(notification), ONE_TICK);
    }

    @EventHandler
    public void onObjectiveNeutralizedEvent(ObjectiveNeutralizedEvent event) {
        CapturableObjective objective = event.getObjective();
        Race previousOccupiers = event.getPreviousOccupiers();
        String notification = "The " + previousOccupiers.getName() + " have lost " + objective.getName() + "!";
        String announcement = ChatColor.GRAY + notification;
        playerService.getOnlineMonolithPlayers().forEach(player -> {
            player.sendTitle("", announcement);
            player.sendMessage(announcement);
        });
        RunnableUtils.runLaterAsync(() -> publishDiscordNotification(notification), ONE_TICK);
    }

    @EventHandler
    public void onObjectiveAssaultedEvent(ObjectiveCapturedEvent event) {
        CapturableObjective objective = event.getObjective();
        Race occupiers = objective.getOccupiers();
        String notification = objective.getName() + " is under assault!";
        String announcement = occupiers.getColor() + notification;
        playerService.getOnlineMonolithPlayers().forEach(player -> {
            player.sendTitle("", announcement);
            player.sendMessage(announcement);
        });
        RunnableUtils.runLaterAsync(() -> publishDiscordNotification(notification), ONE_TICK);
    }
}

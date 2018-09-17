package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.siege.api.objective.Objective;
import gg.warcraft.siege.api.objective.event.ObjectiveCapturedEvent;
import gg.warcraft.siege.api.objective.event.ObjectiveNeutralizedEvent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ObjectiveAnnouncementHandler {
    // FIXME this key is now compromised, cycle it
    private static final String discordWebhook = "https://discordapp.com/api/webhooks/427168926540234773/izX7BeEhd-vxfAXA2ZxfQtFCYmWFKqU5ySEsVtt_xsRpSnvox6PdcwUFJyoaNXsGfOAp";

    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final LegendsPlayerCommandService legendsPlayerCommandService;
    private final TaskService taskService;
    private final TimeUtils timeUtils;

    @Inject
    public ObjectiveAnnouncementHandler(LegendsPlayerQueryService legendsPlayerQueryService,
                                        LegendsPlayerCommandService legendsPlayerCommandService,
                                        TaskService taskService, TimeUtils timeUtils) {
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.legendsPlayerCommandService = legendsPlayerCommandService;
        this.taskService = taskService;
        this.timeUtils = timeUtils;
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

    @Subscribe
    public void onObjectiveCapturedEvent(ObjectiveCapturedEvent event) {
        Objective objective = event.getObjective();
        Team occupiers = objective.getOccupiers();
        String notification = "The " + occupiers.getName() + " have captured " + objective.getName() + "!";
        String announcement = occupiers.getColor() + notification;
        legendsPlayerQueryService.getOnlinePlayers().forEach(player -> {
            UUID playerId = player.getId();
            legendsPlayerCommandService.sendTitle(playerId, "", announcement);
            legendsPlayerCommandService.sendNotification(player.getId(), announcement);
        });
        // TODO disabled for now: taskService.runLaterAsync(() -> publishDiscordNotification(notification), timeUtils.oneTick());
    }

    @Subscribe
    public void onObjectiveNeutralizedEvent(ObjectiveNeutralizedEvent event) {
        Objective objective = event.getObjective();
        Team defenders = event.getDefenders();
        String notification = "The " + defenders.getName() + " have lost " + objective.getName() + "!";
        String announcement = ColorCode.GRAY + notification;
        legendsPlayerQueryService.getOnlinePlayers().forEach(player -> {
            UUID playerId = player.getId();
            legendsPlayerCommandService.sendTitle(playerId, "", announcement);
            legendsPlayerCommandService.sendNotification(playerId, announcement);
        });
        // TODO disabled for now: taskService.runLaterAsync(() -> publishDiscordNotification(notification), timeUtils.oneTick());
    }
}

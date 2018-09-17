package gg.warcraft.legends.app.misc;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerDisconnectEvent;
import gg.warcraft.monolith.api.util.TimeUtils;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpydeyBot {
    // FIXME this key is now compromised, cycle it
    private static final String discordWebhook = "https://discordapp.com/api/webhooks/490521963639341076/hWDD2OL9m-4417ELeCsYGgU8ymc_T7kL_8b4-Y3B7yQWMhVIWHYM-Uz6LMSSSOS_xft_";
    private static final UUID eijlenId = UUID.fromString("926fe756-2622-4e18-94f9-c4b0fc22ebb9");

    private final TimeUtils timeUtils;

    private long loginTime;

    @Inject
    public SpydeyBot(TimeUtils timeUtils) {
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
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        if (event.getPlayerId().equals(eijlenId)) {
            loginTime = System.currentTimeMillis();
            String notification = "Eijlen logged in at " + LocalDateTime.now();
            publishDiscordNotification(notification);
        }
    }

    @Subscribe
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
        if (event.getPlayerId().equals(eijlenId)) {
            String timeElapsed = timeUtils.getTimeElapsedSince(loginTime);
            String notification = "Eijlen logged out at " + LocalDateTime.now() + " (played for " + timeElapsed + ")";
            publishDiscordNotification(notification);
        }
    }
}

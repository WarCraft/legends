package gg.warcraft.legends.api.core.game.handler

class GameAnnouncementHandler {

}

/* TODO
public class ObjectiveAnnouncementHandler {
    private static final String discordWebhook = "DISCORD_KEY";

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
        if (objective.getType().equals("TUTORIAL")) {
            return;
        }

        LegendsTeam occupiers = (LegendsTeam) objective.getOccupiers();
        String notification = "The " + occupiers.getPlural() +
                " have captured " + objective.getName() + "!";
        String announcement = occupiers.getChatColor() + notification;
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
        if (objective.getType().equals("TUTORIAL")) {
            return;
        }

        LegendsTeam defenders = (LegendsTeam) event.getDefenders();
        if (defenders != null) {
            String notification = "The " + defenders.getPlural() +
                    " have lost " + objective.getName() + "!";
            String announcement = ColorCode.GRAY + notification;
            legendsPlayerQueryService.getOnlinePlayers().forEach(player -> {
                UUID playerId = player.getId();
                legendsPlayerCommandService.sendTitle(playerId, "", announcement);
                legendsPlayerCommandService.sendNotification(playerId, announcement);
            });
        }
        // TODO disabled for now: taskService.runLaterAsync(() -> publishDiscordNotification(notification), timeUtils.oneTick());
    }
}

 */

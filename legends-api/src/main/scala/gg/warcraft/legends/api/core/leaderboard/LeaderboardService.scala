package gg.warcraft.legends.api.core.leaderboard

import gg.warcraft.legends.api.player.statistic.LegendsStatistic

import scala.concurrent.Future

class LeaderboardService {
  private var _leaderboards: Map[LegendsStatistic, Leaderboard] = Map.empty

  def leaderboards: Map[LegendsStatistic, Leaderboard] = _leaderboards

  def updateLeaderboard(statistic: LegendsStatistic): Future[Unit] = {
    ???
  }

  def publishLeaderboard(statistic: LegendsStatistic): Future[Unit] = {
    ???
  }
}

/*TODO
public abstract class AbstractLeaderboardService {
    private static final String DAILY_LEADERBOARD_PREFIX = "leaderboard:daily:";
    private static final String WARRY_LEADERBOARD_PREFIX = "leaderboard:warry:";
    private static final String ALLTIME_LEADERBOARD_PREFIX = "leaderboard:alltime:";

    private final GameQueryService gameQueryService;
    private final DateTimeFormatter dateTimeFormatter;

    public AbstractLeaderboardService(GameQueryService gameQueryService) {
        this.gameQueryService = gameQueryService;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    }

    private String getCurrentNewYorkDate() {
        LocalDateTime currentUtcTime = LocalDateTime.now();
        LocalDateTime currentNewYorkTime = currentUtcTime.minusHours(5);
        return currentNewYorkTime.toLocalDate().format(dateTimeFormatter);
    }

    protected String getLeaderboardKey(LegendsStatistic statistic) {
        if (statistic.isDaily()) {
            String today = getCurrentNewYorkDate();
            return DAILY_LEADERBOARD_PREFIX + today + ":" + statistic.getKey();
        } else if (statistic.isWarry()) {
            int currentWarId = gameQueryService.getCurrentWar().getId();
            return WARRY_LEADERBOARD_PREFIX + currentWarId + ":" + statistic.getKey();
        } else {
            return ALLTIME_LEADERBOARD_PREFIX + statistic.getKey();
        }
    }
}

 */





/*TODO
public class DefaultLeaderboardQueryService extends AbstractLeaderboardService implements LeaderboardQueryService {
    private final PersistenceService persistenceService;

    @Inject
    public DefaultLeaderboardQueryService(GameQueryService gameQueryService,
                                          PersistenceService persistenceService) {
        super(gameQueryService);
        this.persistenceService = persistenceService;
    }

    @Override
    public List<LeaderboardEntry> getLeaders(LegendsStatistic statistic, int count) {
        String leaderboardKey = getLeaderboardKey(statistic);
        Map<String, Double> leaders = persistenceService.getSortedSet(leaderboardKey, 0, count);
        if (leaders == null || leaders.isEmpty()) {
            return Collections.emptyList();
        }

        List<LeaderboardEntry> result = new ArrayList<>();
        int rank = 1;
        for (Map.Entry<String, Double> entry : leaders.entrySet()) {
            UUID playerId = UUID.fromString(entry.getKey());
            LeaderboardEntry leaderboardEntry = new SimpleLeaderboardEntry(
                    playerId, rank, entry.getValue().intValue());
            result.add(leaderboardEntry);
            rank += 1;
        }
        return result;
    }
}

 */








/*TODO
public class DefaultLeaderboardCommandService extends AbstractLeaderboardService implements LeaderboardCommandService {
    private static final String LEADERBOARDS_BUCKET = "leaderboards";

    private final PersistenceService persistenceService;
    private final TaskService taskService;
    private final ObjectMapper jsonMapper;
    private final AmazonS3 s3;

    @Inject
    public DefaultLeaderboardCommandService(GameQueryService gameQueryService,
                                            PersistenceService persistenceService,
                                            TaskService taskService,
                                            @JsonMapper ObjectMapper jsonMapper) {
        super(gameQueryService);
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
        Map<String, Double> ranks = persistenceService.getSortedSet(leaderboardKey);

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

 */
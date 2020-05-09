package gg.warcraft.legends.api.core

import gg.warcraft.monolith.api.entity.team.Team

trait MetricsService {
  def recordPlayerCount(team: Team, count: Int): Unit
  def recordPoints(team: Team, points: Int): Unit
  def recordScore(team: Team, score: Int): Unit
}


/*
public class AwsMetricsService implements MetricsService {
    private final TaskService taskService;
    private final AmazonCloudWatch cloudwatch;

    @Inject
    public AwsMetricsService(TaskService taskService) {
        this.taskService = taskService;
        this.cloudwatch = AmazonCloudWatchClientBuilder.defaultClient();
    }

    private void record(String metric, String dimensionName, String dimensionValue, int value) {
        Dimension dimension = new Dimension()
                .withName(dimensionName)
                .withValue(dimensionValue);
        MetricDatum datum = new MetricDatum()
                .withMetricName(metric)
                .withUnit(StandardUnit.None)
                .withValue((double) value)
                .withDimensions(dimension);

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("Legends")
                .withMetricData(datum);

        taskService.runNextTickAsync(() -> {
            PutMetricDataResult result = cloudwatch.putMetricData(request);
            int statusCode = result.getSdkHttpMetadata().getHttpStatusCode();
        });
    }

    @Override
    public void recordPlayerCount(LegendsTeam team, int count) {
        record("PlayerCount", "Team", team.getPlural(), count);
    }

    @Override
    public void recordTotalPoints(LegendsTeam team, int points) {
        record("TotalPoints", "Team", team.getPlural(), points);
    }
}

 */
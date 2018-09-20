package gg.warcraft.legends.app.core;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.PutMetricDataResult;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import gg.warcraft.legends.api.core.MetricsService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.team.Team;

@Singleton
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
    public void recordPlayerCount(Team team, int count) {
        record("PlayerCount", "Race", team.getName(), count);
    }

    @Override
    public void recordTotalPoints(Team team, int points) {
        record("TotalPoints", "Race", team.getName(), points);
    }
}

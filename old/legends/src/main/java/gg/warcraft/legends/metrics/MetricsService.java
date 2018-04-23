package gg.warcraft.legends.metrics;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.PutMetricDataResult;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

public class MetricsService {
    private static final MetricsService instance = new MetricsService();

    public static MetricsService getInstance() {
        return instance;
    }

    private MetricsService() {

    }

    private final AmazonCloudWatch cloudwatch = AmazonCloudWatchClientBuilder.defaultClient();

    private void publishRacialMetric(String metric, String race, int value) {
        MetricDatum datum = new MetricDatum()
                .withMetricName(metric)
                .withUnit(StandardUnit.None)
                .withValue((double) value)
                .withDimensions(new Dimension().withName("Race").withValue(race));

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("Legends")
                .withMetricData(datum);

        PutMetricDataResult result = cloudwatch.putMetricData(request);
        int statusCode = result.getSdkHttpMetadata().getHttpStatusCode();
    }

    public void publishPlayerCountMetric(int humans, int orcs) {
        publishRacialMetric("PlayerCount", "Human", humans);
        publishRacialMetric("PlayerCount", "Orc", orcs);
    }

    public void publishTotalPointsMetric(int humans, int orcs) {
        publishRacialMetric("TotalPoints", "Human", humans);
        publishRacialMetric("TotalPoints", "Orc", orcs);
    }
}

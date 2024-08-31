package am.run.tracker.core.user;

public record UserStatsAggregationResponse(
       Integer totalRuns,
       Double averageSpeed,
       Double totalDistance
) {
}

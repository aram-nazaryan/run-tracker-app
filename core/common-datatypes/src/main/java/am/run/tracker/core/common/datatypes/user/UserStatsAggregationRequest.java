package am.run.tracker.core.common.datatypes.user;

import java.time.Instant;

public record UserStatsAggregationRequest(
        Instant toDateTime,
        Instant fromDateTime
) {
}

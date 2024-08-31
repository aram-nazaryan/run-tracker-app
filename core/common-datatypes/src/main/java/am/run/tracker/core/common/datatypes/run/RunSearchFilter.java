package am.run.tracker.core.common.datatypes.run;

import java.time.Instant;

public record RunSearchFilter(
        Instant fromDatetime,
        Instant toDatetime
) {
}

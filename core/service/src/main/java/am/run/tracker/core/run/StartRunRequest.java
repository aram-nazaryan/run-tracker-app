package am.run.tracker.core.run;


import am.run.tracker.core.common.datatypes.run.PointGeography;
import am.run.tracker.core.common.datatypes.run.UpdateRunType;

import java.time.Instant;

public class StartRunRequest extends UpdateRunRequest {

    public StartRunRequest() {
        super(UpdateRunType.START);
    }

    public StartRunRequest(final PointGeography position,
                           final Instant time,
                           final UpdateRunType type) {
        super(position, time, type);
    }
}

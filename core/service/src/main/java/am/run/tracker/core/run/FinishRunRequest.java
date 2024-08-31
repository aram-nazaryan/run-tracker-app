package am.run.tracker.core.run;


import am.run.tracker.core.common.datatypes.run.PointGeography;
import am.run.tracker.core.common.datatypes.run.UpdateRunType;

import java.time.Instant;

public class FinishRunRequest extends UpdateRunRequest {

    private Integer distance;

    public FinishRunRequest() {
        super(UpdateRunType.FINISH);
    }

    public FinishRunRequest(final PointGeography position,
                            final Instant time,
                            final UpdateRunType type,
                            Integer distance) {
        super(position, time, type);
        this.distance = distance;
    }

    public FinishRunRequest(StartRunRequest startRunRequest) {
        super(startRunRequest.getPosition(), startRunRequest.getTime(), UpdateRunType.FINISH);
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}

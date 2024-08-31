package am.run.tracker.core.run;


import am.run.tracker.core.common.datatypes.run.PointGeography;
import am.run.tracker.core.common.datatypes.run.UpdateRunType;

import java.time.Instant;

public abstract class UpdateRunRequest {

    private PointGeography position;
    private Instant time;
    private UpdateRunType type;

    public UpdateRunRequest() {
    }

    public UpdateRunRequest(UpdateRunType type) {
        this.type = type;
    }

    public UpdateRunRequest(final PointGeography position,
                            final Instant time,
                            final UpdateRunType type) {
        this.position = position;
        this.time = time;
        this.type = type;
    }

    public PointGeography getPosition() {
        return position;
    }

    public void setPosition(PointGeography position) {
        this.position = position;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public UpdateRunType getType() {
        return type;
    }

    public void setType(UpdateRunType type) {
        this.type = type;
    }
}

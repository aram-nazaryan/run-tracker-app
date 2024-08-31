package am.run.tracker.api.common.run;


import am.run.tracker.core.common.datatypes.run.UpdateRunType;

import java.time.Instant;

public class FinishRunRequestDto extends UpdateRunRequestDto {

    private Integer distance;

    public FinishRunRequestDto() {
        super(UpdateRunType.FINISH);
    }

    public FinishRunRequestDto(final PointGeographyDto position,
                               final Instant time,
                               final UpdateRunType type,
                               final Integer distance) {
        super(position, time, type);
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}

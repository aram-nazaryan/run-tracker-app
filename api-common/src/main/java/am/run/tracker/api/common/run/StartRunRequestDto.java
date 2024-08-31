package am.run.tracker.api.common.run;


import am.run.tracker.core.common.datatypes.run.UpdateRunType;

import java.time.Instant;

public class StartRunRequestDto extends UpdateRunRequestDto {

    public StartRunRequestDto() {
        super(UpdateRunType.START);
    }

    public StartRunRequestDto(UpdateRunType type) {
        super(type);
    }

    public StartRunRequestDto(final PointGeographyDto position,
                              final Instant time,
                              final UpdateRunType type) {
        super(position, time, type);
    }
}

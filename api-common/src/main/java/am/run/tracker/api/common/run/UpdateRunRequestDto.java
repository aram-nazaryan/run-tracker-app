package am.run.tracker.api.common.run;

import am.run.tracker.core.common.datatypes.run.UpdateRunType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartRunRequestDto.class, name = "START"),
        @JsonSubTypes.Type(value = FinishRunRequestDto.class, name = "FINISH")
})
public abstract class UpdateRunRequestDto {

    @NotNull
    private PointGeographyDto position;

    @NotNull
    private Instant time;

    @NotNull
    private UpdateRunType type;

    public UpdateRunRequestDto() {
    }

    public UpdateRunRequestDto(UpdateRunType type) {
        this.type = type;
    }

    public UpdateRunRequestDto(final PointGeographyDto position,
                               final Instant time,
                               final UpdateRunType type) {
        this.position = position;
        this.time = time;
        this.type = type;
    }

    public @NotNull PointGeographyDto getPosition() {
        return position;
    }

    public void setPosition(@NotNull PointGeographyDto position) {
        this.position = position;
    }

    public @NotNull Instant getTime() {
        return time;
    }

    public void setTime(@NotNull Instant time) {
        this.time = time;
    }

    public @NotNull UpdateRunType getType() {
        return type;
    }

    public void setType(@NotNull UpdateRunType type) {
        this.type = type;
    }
}

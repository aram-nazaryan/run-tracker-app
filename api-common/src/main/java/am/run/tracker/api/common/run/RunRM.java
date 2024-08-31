package am.run.tracker.api.common.run;

import am.run.tracker.api.common.constants.RelationConstants;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;
import java.util.UUID;


@Relation(collectionRelation = RelationConstants.CONTENT_NAME)
public class RunRM extends RepresentationModel<RunRM> {

    private UUID id;
    private UUID userId;
    private Instant startTime;
    private Instant finishTime;
    private PointGeographyDto startPosition;
    private PointGeographyDto finishPosition;
    private Integer distance;
    private Double speed;
    private Instant created;

    public RunRM() {
    }

    public RunRM(final UUID id,
                 final UUID userId,
                 final Instant startTime,
                 final Instant finishTime,
                 final PointGeographyDto startPosition,
                 final PointGeographyDto finishPosition,
                 final Integer distance,
                 final Double speed,
                 final Instant created) {
        this.id = id;
        this.userId = userId;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.startPosition = startPosition;
        this.finishPosition = finishPosition;
        this.distance = distance;
        this.speed = speed;
        this.created = created;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Instant finishTime) {
        this.finishTime = finishTime;
    }

    public PointGeographyDto getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(PointGeographyDto startPosition) {
        this.startPosition = startPosition;
    }

    public PointGeographyDto getFinishPosition() {
        return finishPosition;
    }

    public void setFinishPosition(PointGeographyDto finishPosition) {
        this.finishPosition = finishPosition;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}

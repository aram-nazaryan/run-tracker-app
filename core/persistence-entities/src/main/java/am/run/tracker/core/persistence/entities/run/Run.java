package am.run.tracker.core.persistence.entities.run;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "run")
public class Run {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "start_time", nullable = false, updatable = false)
    private Instant startTime;

    @Column(name = "start_position", nullable = false, columnDefinition = "geometry(Point,4326)")
    private Point startPosition;

    @Column(name = "finish_time")
    private Instant finishTime;

    @Column(name = "finish_position", columnDefinition = "geometry(Point,4326)")
    private Point finishPosition;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "speed")
    private Double speed;

    @CreatedDate
    @Column(name = "created", updatable = false, nullable = false)
    private Instant created;

    @LastModifiedDate
    @Column(name = "updated", nullable = false)
    private Instant updated;

    @Column(name = "deleted")
    private Instant deleted;

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

    public Point getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Point startPosition) {
        this.startPosition = startPosition;
    }

    public Instant getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Instant finishTime) {
        this.finishTime = finishTime;
    }

    public Point getFinishPosition() {
        return finishPosition;
    }

    public void setFinishPosition(Point finishPosition) {
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

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getDeleted() {
        return deleted;
    }

    public void setDeleted(Instant deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Run run)) return false;

        return new EqualsBuilder()
                .append(id, run.id)
                .append(userId, run.userId)
                .append(startTime, run.startTime)
                .append(startPosition, run.startPosition)
                .append(finishTime, run.finishTime)
                .append(finishPosition, run.finishPosition)
                .append(distance, run.distance)
                .append(speed, run.speed)
                .append(created, run.created)
                .append(updated, run.updated)
                .append(deleted, run.deleted)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(userId)
                .append(startTime)
                .append(startPosition)
                .append(finishTime)
                .append(finishPosition)
                .append(distance)
                .append(speed)
                .append(created)
                .append(updated)
                .append(deleted)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("userId", userId)
                .append("startTime", startTime)
                .append("startPosition", startPosition)
                .append("finishTime", finishTime)
                .append("finishPosition", finishPosition)
                .append("distance", distance)
                .append("speed", speed)
                .append("created", created)
                .append("updated", updated)
                .append("deleted", deleted)
                .toString();
    }
}

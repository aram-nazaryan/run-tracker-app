package am.run.tracker.api.common.run;

import am.run.tracker.api.common.constants.RelationConstants;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;


@Relation(collectionRelation = RelationConstants.CONTENT_NAME)
public class RunStatsRM extends RepresentationModel<RunStatsRM> {

    private Integer totalRuns;
    private Double averageSpeed;
    private Double totalDistance;

    public RunStatsRM() {
    }

    public RunStatsRM(final Integer totalRuns,
                      final Double averageSpeed,
                      final Double totalDistance) {
        this.totalRuns = totalRuns;
        this.averageSpeed = averageSpeed;
        this.totalDistance = totalDistance;
    }

    public Integer getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(Integer totalRuns) {
        this.totalRuns = totalRuns;
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(Double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }
}

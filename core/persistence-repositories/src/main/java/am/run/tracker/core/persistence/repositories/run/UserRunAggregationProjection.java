package am.run.tracker.core.persistence.repositories.run;

public interface UserRunAggregationProjection {
    Integer getRunCount();
    Double getTotalDistance();
    Double getAverageSpeed();
}

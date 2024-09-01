package am.run.tracker.core.run;

import am.run.tracker.core.common.datatypes.PageResponse;
import am.run.tracker.core.common.datatypes.SearchGenericRequest;
import am.run.tracker.core.common.datatypes.run.RunSearchFilter;
import am.run.tracker.core.common.datatypes.run.RunSortProperty;
import am.run.tracker.core.common.datatypes.user.UserStatsAggregationRequest;
import am.run.tracker.core.persistence.entities.run.Run;
import am.run.tracker.core.user.UserStatsAggregationResponse;

import java.util.Optional;
import java.util.UUID;

public interface RunService {

    /**
     * Create run from request
     *
     * @param userId
     * @param startRunRequest
     */
    Run create(final UUID userId, final StartRunRequest startRunRequest);

    /**
     * Finishes active run for user
     *
     * @param runId
     * @param finishRunRequest
     */
    Run finish(final UUID runId, FinishRunRequest finishRunRequest);

    /**
     * Finding active run for user ID
     *
     * @param userId
     */
    Optional<Run> findActiveRun(final UUID userId);

    /**
     * Aggregates user stats
     *
     * @param userId
     * @param request
     */
    UserStatsAggregationResponse aggregateUserStats(final UUID userId, final UserStatsAggregationRequest request);

    /**
     * Searches for runs
     *
     * @param userId
     * @param request
     */
    PageResponse<Run> search(final UUID userId, final SearchGenericRequest<RunSearchFilter, RunSortProperty> request);

    /**
     * Deletes runs by user Id
     *
     * @param userId
     */
    void deleteRunByUserId(final UUID userId);
}

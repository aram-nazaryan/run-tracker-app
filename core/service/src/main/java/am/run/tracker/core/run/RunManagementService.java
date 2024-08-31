package am.run.tracker.core.run;

import am.run.tracker.core.common.datatypes.PageResponse;
import am.run.tracker.core.common.datatypes.SearchGenericRequest;
import am.run.tracker.core.common.datatypes.run.RunSearchFilter;
import am.run.tracker.core.common.datatypes.run.RunSortProperty;
import am.run.tracker.core.persistence.entities.run.Run;

import java.util.UUID;

public interface RunManagementService {

    /**
     * Updates run with request
     *
     * @param userId
     * @param updateRunRequest
     */
    Run update(final UUID userId, final UpdateRunRequest updateRunRequest);

    /**
     * Searches for runs by user Id and  request
     *
     * @param userId
     * @param request
     */
    PageResponse<Run> searchRun(UUID userId, SearchGenericRequest<RunSearchFilter, RunSortProperty> request);
}

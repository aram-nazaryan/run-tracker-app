package am.run.tracker.api.rest.mapper.run;

import am.run.tracker.api.common.run.RunStatsRM;
import am.run.tracker.api.rest.RunTrackerEndpoint;
import am.run.tracker.core.user.UserStatsAggregationResponse;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RunStatsAssembler extends RepresentationModelAssemblerSupport<UserStatsAggregationResponse, RunStatsRM> {

    public RunStatsAssembler() {
        super(RunTrackerEndpoint.class, RunStatsRM.class);
    }

    @Override
    public RunStatsRM toModel(UserStatsAggregationResponse response) {
        return new RunStatsRM(response.totalRuns(),
                response.averageSpeed(),
                response.totalDistance()
        );
    }
}

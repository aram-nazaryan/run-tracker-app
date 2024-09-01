package am.run.tracker.core.run;

import am.run.tracker.core.common.datatypes.PageResponse;
import am.run.tracker.core.common.datatypes.SearchGenericRequest;
import am.run.tracker.core.common.datatypes.run.RunSearchFilter;
import am.run.tracker.core.common.datatypes.run.RunSortProperty;
import am.run.tracker.core.common.datatypes.run.UpdateRunType;
import am.run.tracker.core.common.datatypes.user.UserStatsAggregationRequest;
import am.run.tracker.core.persistence.entities.run.Run;
import am.run.tracker.core.persistence.entities.user.User;
import am.run.tracker.core.user.UserNotFoundException;
import am.run.tracker.core.user.UserService;
import am.run.tracker.core.user.UserStatsAggregationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

@Service
public class RunManagementServiceImpl implements RunManagementService {

    private static final Logger logger = LoggerFactory.getLogger(RunManagementServiceImpl.class);

    private final UserService userService;
    private final RunService runService;

    public RunManagementServiceImpl(final UserService userService,
                                    final RunService runService) {
        this.userService = userService;
        this.runService = runService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Run update(final UUID userId, final UpdateRunRequest updateRunRequest) {
        Assert.notNull(userId, "User ID must not be null");
        Assert.notNull(updateRunRequest, "Update request must not be null");
        logger.trace("Updating run by user ID: {}, and request: {}", userId, updateRunRequest);
        Run updatedRun = null;
        final User runningUser = userService.get(userId);
        final Optional<Run> activeRunOpt = runService.findActiveRun(runningUser.getId());
        if (updateRunRequest.getType() == UpdateRunType.START) {
            final StartRunRequest startRunRequest = (StartRunRequest) updateRunRequest;
            if (activeRunOpt.isPresent()) {
                final Run activeRun = activeRunOpt.get();
                runService.finish(activeRun.getId(), new FinishRunRequest(startRunRequest));
            }
            updatedRun = runService.create(userId, startRunRequest);
        } else if (updateRunRequest.getType() == UpdateRunType.FINISH) {
            final FinishRunRequest finishRunRequest = (FinishRunRequest) updateRunRequest;
            if (activeRunOpt.isEmpty()) {
                throw new NoActiveRunException(userId);
            }
            updatedRun = runService.finish(activeRunOpt.get().getId(), finishRunRequest);
        }
        logger.debug("Done updating run with ID: {}", runningUser);
        return updatedRun;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<Run> searchRun(final UUID userId, final SearchGenericRequest<RunSearchFilter, RunSortProperty> request) {
        Assert.notNull(userId, "User ID must not be null");
        Assert.notNull(request, "Request must not be null");
        logger.trace("Searching for user runs by user ID: {}, request: {}", userId, request);
        final Optional<User> optionalUser = userService.find(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        final PageResponse<Run> pagrRun = runService.search(userId, request);
        logger.debug("Done searching for user runs by user ID: {}, request: {}", userId, request);
        return pagrRun;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserStatsAggregationResponse aggregateUserStats(UUID userId, UserStatsAggregationRequest request) {
        logger.trace("Aggregating user run stats by user ID: {}, request: {}", userId, request);
        final User user = userService.get(userId);
        final UserStatsAggregationResponse aggregateUserStats = runService.aggregateUserStats(user.getId(), request);
        logger.debug("Done aggregating user run stats by user ID: {}, request: {}", userId, request);
        return aggregateUserStats;
    }
}

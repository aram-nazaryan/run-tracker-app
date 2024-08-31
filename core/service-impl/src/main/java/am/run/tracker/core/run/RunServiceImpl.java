package am.run.tracker.core.run;

import am.run.tracker.core.common.datatypes.PageResponse;
import am.run.tracker.core.common.datatypes.SearchGenericRequest;
import am.run.tracker.core.common.datatypes.run.PointGeography;
import am.run.tracker.core.common.datatypes.run.RunSearchFilter;
import am.run.tracker.core.common.datatypes.run.RunSortProperty;
import am.run.tracker.core.common.datatypes.user.UserStatsAggregationRequest;
import am.run.tracker.core.persistence.entities.run.Run;
import am.run.tracker.core.persistence.repositories.run.RunRepository;
import am.run.tracker.core.persistence.repositories.run.UserRunAggregationProjection;
import am.run.tracker.core.user.UserStatsAggregationResponse;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RunServiceImpl implements RunService {

    private final static Logger logger = LoggerFactory.getLogger(RunServiceImpl.class);

    private final RunRepository runRepository;
    private final GeometryFactory geometryFactory;

    public RunServiceImpl(final RunRepository runRepository,
                          final GeometryFactory geometryFactory) {
        this.runRepository = runRepository;
        this.geometryFactory = geometryFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Run create(final UUID userId, final StartRunRequest startRunRequest) {
        logger.trace("Creating run for user ID: {}, and request: {}", userId, startRunRequest);
        final Run run = new Run();
        run.setStartPosition(toPoint(startRunRequest.getPosition()));
        run.setUserId(userId);
        run.setStartTime(startRunRequest.getTime());

        final Run saved = runRepository.save(run);
        logger.trace("Done creating run with ID: {}", saved.getId());
        return saved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Run finish(final UUID runId, final FinishRunRequest finishRunRequest) {
        logger.trace("Finishing active run for with ID: {}, request: {}", runId, finishRunRequest);
        final Run run = this.get(runId);
        if (finishRunRequest.getTime().isBefore(run.getStartTime())) {
            throw new MalformedRunFinishRequestException(finishRunRequest.getTime(), run.getStartTime());
        }
        final Point finishPoint = toPoint(finishRunRequest.getPosition());
        run.setFinishPosition(finishPoint);
        run.setFinishTime(finishRunRequest.getTime());
        Integer distance = finishRunRequest.getDistance();
        if (distance == null) {
            distance = runRepository.calculateDistance(run.getStartPosition(), finishPoint);
            run.setDistance(distance);
        }
        long runTimeInSeconds = ChronoUnit.SECONDS.between(run.getStartTime(), finishRunRequest.getTime());
        run.setSpeed((double) distance / runTimeInSeconds);

        final Run finishedRun = runRepository.save(run);
        logger.debug("Done finishing active run for user ID: {}, request: {}", runId, finishRunRequest);
        return finishedRun;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Run> findActiveRun(final UUID userId) {
        Assert.notNull(userId, "User ID must not be null");
        logger.trace("Finding active run for user ID: {}", userId);
        Optional<Run> userActiveRun = runRepository.findUserActiveRun(userId);
        logger.debug("Done searching for active run by user ID: {}", userId);
        return userActiveRun;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserStatsAggregationResponse aggregateUserStats(final UUID userId, final UserStatsAggregationRequest request) {
        Assert.notNull(userId, "User ID must not be null");
        Assert.notNull(request, "Request must not be null");
        logger.trace("Starting user stats aggregation user ID: {}, request: {}", userId, request);
        final UserRunAggregationProjection userStats = runRepository.aggregateStats(userId,
                request.fromDateTime(),
                request.toDateTime());
        logger.debug("Done user stats aggregation user ID: {}, request: {}", userId, request);
        return new UserStatsAggregationResponse(userStats.getRunCount(), userStats.getAverageSpeed(), userStats.getTotalDistance());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<Run> search(final UUID userId, final SearchGenericRequest<RunSearchFilter, RunSortProperty> request) {
        Assert.notNull(userId, "User ID must not be null");
        logger.trace("Searching for user's runs by ID: {}, request: {}", userId, request);
        final Page<Run> runPage = runRepository.search(userId,
                request.filter().fromDatetime(),
                request.filter().toDatetime(),
                PageRequest.of(request.pageRequest().page(),
                        request.pageRequest().size(),
                        Sort.Direction.valueOf(request.pageRequest().sortDirection().name()),
                        request.pageRequest().sortProperty().getName()));
        logger.trace("Done searching for user's runs by ID: {}, request: {}", userId, request);
        return new PageResponse<>(
                runPage.getTotalElements(),
                runPage.getTotalPages(),
                runPage.getNumber(),
                runPage.getSize(),
                request.pageRequest().sortProperty(),
                request.pageRequest().sortDirection(),
                runPage.getContent());
    }


    private Run get(final UUID runId) {
        return runRepository.getRunByIdAndDeletedIsNull(runId)
                .orElseThrow(() -> new RunNotFoundException(runId));
    }

    private Point toPoint(final PointGeography position) {
        final Coordinate coordinate = new Coordinate(position.longitude(), position.latitude());
        return geometryFactory.createPoint(coordinate);
    }
}

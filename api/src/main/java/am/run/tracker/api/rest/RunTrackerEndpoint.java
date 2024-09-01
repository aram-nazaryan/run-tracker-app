package am.run.tracker.api.rest;

import am.run.tracker.api.common.run.*;
import am.run.tracker.api.common.user.*;
import am.run.tracker.api.rest.mapper.PagedResponseResourcesAssembler;
import am.run.tracker.api.rest.mapper.run.RunAssembler;
import am.run.tracker.api.rest.mapper.run.RunMapper;
import am.run.tracker.api.rest.mapper.run.RunStatsAssembler;
import am.run.tracker.api.rest.mapper.user.UserAssembler;
import am.run.tracker.api.rest.mapper.user.UserDetailedAssembler;
import am.run.tracker.api.rest.mapper.user.UserMapper;
import am.run.tracker.core.common.datatypes.PageResponse;
import am.run.tracker.core.common.datatypes.SearchGenericRequest;
import am.run.tracker.core.common.datatypes.run.RunSearchFilter;
import am.run.tracker.core.common.datatypes.run.RunSortProperty;
import am.run.tracker.core.common.datatypes.user.*;
import am.run.tracker.core.persistence.entities.run.Run;
import am.run.tracker.core.persistence.entities.user.User;
import am.run.tracker.core.run.*;
import am.run.tracker.core.user.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;


@RestController
@ExposesResourceFor(UserRM.class)
@RequestMapping("/users")
public class RunTrackerEndpoint {

    private final static Logger logger = LoggerFactory.getLogger(RunTrackerEndpoint.class);

    private final UserMapper userMapper;
    private final UserAssembler userAssembler;
    private final UserDetailedAssembler userDetailedAssembler;
    private final PagedResponseResourcesAssembler<User> userPagedResponseResourcesAssembler;

    private final RunMapper runMapper;
    private final RunAssembler runAssembler;
    private final RunStatsAssembler runStatsAssembler;
    private final PagedResponseResourcesAssembler<Run> runPagedResponseResourcesAssembler;


    private final UserService userService;
    private final RunManagementService runManagementService;

    public RunTrackerEndpoint(final UserMapper userMapper,
                              final UserAssembler userAssembler,
                              final UserDetailedAssembler userDetailedAssembler,
                              final PagedResponseResourcesAssembler<User> userPagedResponseResourcesAssembler,
                              final RunMapper runMapper,
                              final RunAssembler runAssembler,
                              final RunStatsAssembler runStatsAssembler,
                              final PagedResponseResourcesAssembler<Run> runPagedResponseResourcesAssembler,
                              final UserService userService,
                              final RunManagementService runManagementService) {
        this.userMapper = userMapper;
        this.userAssembler = userAssembler;
        this.userDetailedAssembler = userDetailedAssembler;
        this.userPagedResponseResourcesAssembler = userPagedResponseResourcesAssembler;
        this.runMapper = runMapper;
        this.runAssembler = runAssembler;
        this.runStatsAssembler = runStatsAssembler;
        this.runPagedResponseResourcesAssembler = runPagedResponseResourcesAssembler;
        this.userService = userService;
        this.runManagementService = runManagementService;
    }

    @PostMapping()
    public ResponseEntity<EntityModel<UserDetailedRM>> create(@Valid @RequestBody UserCreationRequestDto creationRequest) {
        final UserCreationRequest userCreationRequest = userMapper.toUserCreationRequest(creationRequest);
        final User user = userService.create(userCreationRequest);
        final UserDetailedRM userResponseModel = userDetailedAssembler.toModel(user);
        logger.info("Done creating user: {}", userResponseModel);
        return ResponseEntity.ok(EntityModel.of(userResponseModel));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<EntityModel<UserDetailedRM>> patch(@PathVariable("userId") UUID userId, @Valid @RequestBody UserPatchRequestDto patchRequest) {
        try {
            final UserPatchRequest userPatchRequest = userMapper.toUserPatchRequest(patchRequest);
            final User user = userService.patch(userId, userPatchRequest);
            final UserDetailedRM userResponseModel = userDetailedAssembler.toModel(user);
            logger.info("Done patching user: {}", userResponseModel);
            return ResponseEntity.ok(EntityModel.of(userResponseModel));
        } catch (UserNotFoundException e) {
            throw new UserNotFoundExceptionDto(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<EntityModel<UserDetailedRM>> get(@PathVariable("userId") UUID userId) {
        try {
            final User user = userService.get(userId);
            final UserDetailedRM userResponseModel = userDetailedAssembler.toModel(user);
            logger.info("Done getting user: {}", userResponseModel);
            return ResponseEntity.ok(EntityModel.of(userResponseModel));
        } catch (UserNotFoundException e) {
            throw new UserNotFoundExceptionDto(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<UserRM>> search(@RequestParam(value = "query", required = false) String query,
                                                     @RequestParam(value = "genders", required = false) Set<Gender> genders,
                                                     @RequestParam(value = "bornBefore", required = false) Instant bornBefore,
                                                     @RequestParam(value = "bornAfter", required = false) Instant bornAfter,
                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "20") int size,
                                                     @RequestParam(value = "sortProperty", defaultValue = "CREATED") UserSortProperty sortProperty,
                                                     @RequestParam(value = "sortDirection", defaultValue = "DESC") PageRequest.SortDirection sortDirection) {
        final PageResponse<User> userPageResponse = userService.search(
                new SearchGenericRequest<>(query,
                        new UserSearchFilter(genders, bornBefore, bornAfter),
                        new PageRequest<>(page, size, sortProperty, sortDirection)
                )
        );

        final PagedModel<UserRM> userRMS = userPagedResponseResourcesAssembler.toModel(userPageResponse, userAssembler);
        logger.info("Done searching users with query: {}, page: {}, size: {}, sortProperty: {}, sortDirection: {}", query, page, size, sortProperty, sortDirection);
        return ResponseEntity.ok(userRMS);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<EntityModel<UserDetailedRM>> delete(@PathVariable("userId") UUID userId) {
        try {
            final User user = userService.delete(userId);
            final UserDetailedRM userResponseModel = userDetailedAssembler.toModel(user);
            logger.info("Done deleting user: {}", userResponseModel);
            return ResponseEntity.ok(EntityModel.of(userResponseModel));
        } catch (UserNotFoundException e) {
            throw new UserNotFoundExceptionDto(e.getMessage());
        }
    }

    @PostMapping("/{userId}/runs")
    public ResponseEntity<EntityModel<RunRM>> runUpdate(@PathVariable("userId") UUID userId, @Valid @RequestBody UpdateRunRequestDto runRequestDto) {
        try {
            final UpdateRunRequest updateRunRequest = runMapper.toUpdateRunRequest(runRequestDto);
            final Run updatedRun = runManagementService.update(userId, updateRunRequest);
            final RunRM runRM = runAssembler.toModel(updatedRun);
            return ResponseEntity.ok(EntityModel.of(runRM));
        } catch (UserNotFoundException e) {
            throw new UserNotFoundExceptionDto(e.getMessage());
        } catch (NoActiveRunException e) {
            throw new NoActiveRunExceptionDto(e.getMessage());
        } catch (MalformedRunFinishRequestException e) {
            throw new MalformedRunFinishRequestExceptionDto(e.getMessage());
        }
    }

    @GetMapping("/{userId}/runs")
    public ResponseEntity<PagedModel<RunRM>> getUserRuns(@PathVariable("userId") UUID userId,
                                                         @RequestParam(value = "from_datetime", required = false) Instant fromDatetime,
                                                         @RequestParam(value = "to_datetime", required = false) Instant toDatetime,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "20") int size,
                                                         @RequestParam(value = "sortProperty", defaultValue = "CREATED") RunSortProperty sortProperty,
                                                         @RequestParam(value = "sortDirection", defaultValue = "DESC") PageRequest.SortDirection sortDirection) {
        try {
            final PageResponse<Run> runs = runManagementService.searchRun(userId,
                    new SearchGenericRequest<>(null,
                            new RunSearchFilter(fromDatetime, toDatetime),
                            new PageRequest<>(page, size, sortProperty, sortDirection)
                    ));
            final PagedModel<RunRM> runRMS = runPagedResponseResourcesAssembler.toModel(runs, runAssembler);
            return ResponseEntity.ok(runRMS);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundExceptionDto(e.getMessage());
        }
    }

    @GetMapping("/{userId}/runs/stats")
    public ResponseEntity<EntityModel<RunStatsRM>> getUserStats(@PathVariable("userId") UUID userId,
                                                                @RequestParam(value = "from_datetime", required = false) Instant fromDatetime,
                                                                @RequestParam(value = "to_datetime", required = false) Instant toDatetime) {
        try {
            final UserStatsAggregationResponse userStatsAggregationResponse = runManagementService.aggregateUserStats(userId,
                    new UserStatsAggregationRequest(fromDatetime, toDatetime));
            final RunStatsRM statsRM = runStatsAssembler.toModel(userStatsAggregationResponse);
            return ResponseEntity.ok(EntityModel.of(statsRM));
        } catch (UserNotFoundException e) {
            throw new UserNotFoundExceptionDto(e.getMessage());
        }
    }
}

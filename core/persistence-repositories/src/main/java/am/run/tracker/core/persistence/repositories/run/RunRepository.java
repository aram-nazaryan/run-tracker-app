package am.run.tracker.core.persistence.repositories.run;

import am.run.tracker.core.persistence.entities.run.Run;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RunRepository extends CrudRepository<Run, UUID> {

    @Query("""
            select r
            from Run r
            where r.deleted is null
            and r.userId = :userId
            and r.finishPosition is null
            and r.finishTime is null
            order by r.created desc
            limit 1
            """)
    Optional<Run> findUserActiveRun(@Param("userId") UUID userId);

    Set<Run> getByUserIdAndDeletedIsNull(UUID userId);

    Optional<Run> getRunByIdAndDeletedIsNull(UUID runId);

    @Query("""
            select distance(geography(:start), geography(:finish))
            """)
    Integer calculateDistance(@Param("start") Point start, @Param("finish") Point finish);

    @Query("""
            select count(r) as runCount,
                    sum(r.distance) as totalDistance,
                    avg(r.speed) filter (where r.finishPosition is not null) as averageSpeed
            from Run r
            where (coalesce(:fromDatetime) is null or r.startTime >= :fromDatetime)
            and (coalesce(:toDatetime) is null or r.startTime <= :toDatetime)
            and r.deleted is null
            and r.userId = :userId
            """)
    UserRunAggregationProjection aggregateStats(@Param("userId") UUID userId,
                                                @Param("fromDatetime") Instant fromDatetime,
                                                @Param("toDatetime") Instant toDatetime);

    @Query("""
            select r
            from Run r
            where r.deleted is null 
            and r.userId = :userId
            and (coalesce(:fromDatetime) is null or r.startTime >= :fromDatetime)
            and (coalesce(:toDatetime) is null or r.startTime <= :toDatetime)
            """)
    Page<Run> search(@Param("userId") UUID userId,
                     @Param("fromDatetime") Instant fromDatetime,
                     @Param("toDatetime") Instant toDatetime,
                     PageRequest page);
}
